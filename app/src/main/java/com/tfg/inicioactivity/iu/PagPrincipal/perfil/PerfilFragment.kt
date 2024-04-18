package com.tfg.inicioactivity.iu.PagPrincipal.perfil

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
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.tfg.inicioactivity.databinding.FragmentPerfilBinding
import java.io.ByteArrayOutputStream
import java.util.UUID
import android.Manifest
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

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
                storageReference = storage.reference
                val imageRef = storageReference.child("images/${UUID.randomUUID()}.jpg")

                val baos = ByteArrayOutputStream()
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val imageData = baos.toByteArray()

                val uploadTask = imageRef.putBytes(imageData)
                uploadTask.addOnSuccessListener { taskSnapshot ->
                    val downloadUrl = taskSnapshot.metadata?.reference?.downloadUrl.toString()
                    // Guardar la referencia de la foto en la base de datos de Firebase
                    guardarReferenciaFirebase(downloadUrl)
                }.addOnFailureListener {
                    // Manejar cualquier error durante la carga de la imagen
                }
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


    private fun guardarReferenciaFirebase(downloadUrl: String){

        databaseReference = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()
        val userId= auth.currentUser!!.uid
        val usuariosRef = databaseReference.collection("users")

        // Buscar el documento del usuario por su userId
        usuariosRef.whereEqualTo("userId", userId).get().addOnSuccessListener { documents ->
            for (document in documents) {
                // Actualizar el campo imagenPerfil con la URL de la imagen en Firebase Storage
                document.reference.update("imagenPerfil", downloadUrl)
                Log.i("GuardarImagen","Se ha guardado correcatmente")
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