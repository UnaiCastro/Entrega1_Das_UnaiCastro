package com.tfg.inicioactivity.iu.PagPrincipal.perfil


import android.Manifest
import android.content.Context
import android.provider.ContactsContract
import android.util.Log
import android.widget.Toast
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.google.firebase.messaging.FirebaseMessaging
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.tfg.inicioactivity.FirebaseMessageService
import com.tfg.inicioactivity.FirebaseNotificacion
import com.tfg.inicioactivity.databinding.FragmentPerfilBinding
import java.io.ByteArrayOutputStream
import java.util.UUID

class PerfilFragment : Fragment() {

    private var _binding: FragmentPerfilBinding? = null
    private val binding get() = _binding!!
    private lateinit var storageReference: StorageReference
    private lateinit var databaseReference: FirebaseFirestore

    private val takePicture =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imageBitmap = BitmapFactory.decodeStream(
                    requireActivity().contentResolver.openInputStream(imageUri!!)
                )
                binding.PerfilFoto.setImageBitmap(imageBitmap)

                // Guardar la foto en Firebase Storage
                val storage = FirebaseStorage.getInstance()
                storageReference = storage.getReference()
                val imageRef = storageReference.child("images/${UUID.randomUUID()}.jpg")
                println("Printeo imageREf$imageRef")

                val baos = ByteArrayOutputStream()
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val imageData = baos.toByteArray()
                println("prineto imageData $imageData")

                val uploadTask = imageRef.putBytes(imageData)
                uploadTask.addOnSuccessListener { taskSnapshot ->
                    imageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                        val imageUrl = downloadUrl.toString()
                        Log.i("Url Descargada", imageUrl)
                        enviarMensajeFirebase()

                        // Guardar la referencia de la foto en Firestore
                        guardarReferenciaFirebase(imageUrl)
                    }
                }.addOnFailureListener {
                    // Manejar cualquier error durante la carga de la imagen
                }
            }
        }

    private fun enviarMensajeFirebase() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("fcm", "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }

            // Conseguir el token
            val token = task.result

            Log.d("fcm", "El token del dispositivo: $token")

            val data = Data.Builder()
                .putString("token", token)
                .build()
            val otwr = OneTimeWorkRequest.Builder(FirebaseNotificacion::class.java)
                .setInputData(data)
                .build()
            context?.let { WorkManager.getInstance(it).enqueue(otwr) }
            WorkManager.getInstance(requireContext()).getWorkInfoByIdLiveData(otwr.id)
                .observe(this) { workInfo ->
                    if (workInfo != null && workInfo.state.isFinished) {
                        val resultado = workInfo.outputData.getString("result")
                        // Si el php devuelve que se ha identificado CORRECTAMENTE
                        Log.d("fcm", "notificacionFirebase.php devuelve $resultado")
                    }
                }
        }.addOnFailureListener { e ->
            Toast.makeText(context, "Firebase upload failed", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPerfilBinding.inflate(
            layoutInflater,
            container,
            false
        )
        initUI()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        initListener()

    }

    private fun initUI() {
        obtenerURLImagen()
    }

    private fun obtenerURLImagen() {
        Log.i("CargarFoto", "Iniciando obtención de URL de imagen")
        val auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser!!.uid
        databaseReference = FirebaseFirestore.getInstance()
        databaseReference.collection("users")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { documents ->
                Log.i("CargarFoto", "Consulta exitosa, documentos encontrados: ${documents.size()}")
                if (!documents.isEmpty) {
                    val documentSnapshot = documents.documents.first()
                    val url = documentSnapshot.getString("imagenPerfil")
                    if (url != null) {
                        Log.i("CargarFoto", "URL de imagen obtenida correctamente: $url")
                        // Cargar la imagen en el ImageView usando Picasso
                        Picasso.with(requireContext())
                            .load(url)
                            .into(binding.PerfilFoto, object : Callback {
                                override fun onSuccess() {
                                    Log.i("CargarFoto", "Imagen cargada exitosamente")
                                }

                                override fun onError() {
                                    Log.e("CargarFoto", "Error al cargar la imagen")
                                }
                            })
                    } else {
                        Log.e("CargarFoto", "URL de imagen es nula")
                    }
                } else {
                    Log.e("CargarFoto", "No se encontraron documentos")
                }
            }
            .addOnFailureListener { exception ->
                Log.e("CargarFoto", "Error al obtener URL de imagen", exception)
            }
    }

    private fun initListener() {
        val btn = binding.PerfilBoton
        btn.setOnClickListener {
            abrirCamara()
        }
    }

    private fun abrirCamara() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CAMERA),
                REQUEST_CAMERA_PERMISSION
            )
        } else {
            dispatchTakePictureIntent()
        }
    }


    private fun dispatchTakePictureIntent() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "Nueva imagen")
        values.put(MediaStore.Images.Media.DESCRIPTION, "Desde la cámara")
        imageUri = requireActivity().contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            values
        )

        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        takePicture.launch(takePictureIntent)
    }


    private fun guardarReferenciaFirebase(downloadUrl: String) {

        databaseReference = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser!!.uid
        val usuariosRef = databaseReference.collection("users")

        // Buscar el documento del usuario por su userId
        usuariosRef.whereEqualTo("userId", userId).get().addOnSuccessListener { documents ->
            for (document in documents) {
                // Actualizar el campo imagenPerfil con la URL de la imagen en Firebase Storage
                document.reference.update("imagenPerfil", downloadUrl)
                Log.i("GuardarImagen", "Se ha guardado correcatmente")
            }
        }.addOnFailureListener { exception ->
            // Manejar el error al buscar el usuario
        }
    }

    companion object {
        private const val REQUEST_CAMERA_PERMISSION = 101
        private var imageUri: android.net.Uri? = null
    }


}