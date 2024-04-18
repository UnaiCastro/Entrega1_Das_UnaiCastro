package com.tfg.inicioactivity.iu.PagPrincipal.info

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.tfg.inicioactivity.R
import com.tfg.inicioactivity.databinding.FragmentInformacionBinding
import com.tfg.inicioactivity.iu.Inicio.IniciarSesionActivity
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class InformacionFragment : Fragment() {

    private var _binding: FragmentInformacionBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth:FirebaseAuth
    private val REQUEST_IMAGE_CAPTURE = 1
    private lateinit var currentPhotoPath: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentInformacionBinding.inflate(
            layoutInflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()

        // Crear un callback para manejar la navegación hacia atrás
        val callback = object :
            OnBackPressedCallback(true) { //Funcion para que el boton e atras no funcione y solamente se pueda salir dandole a cerrar sesion
            override fun handleOnBackPressed() {
                // Mostrar un Toast con el mensaje
                Toast.makeText(requireContext(), "Por favor, cierre sesión", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        // Agregar el callback al lifecycle owner del fragmento
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    private fun initListener() {
        hacerFoto()
        //Navegacion del CarView del why
        val cardViewWhyFragment = binding.informacionCvWhyapp
        cardViewWhyFragment.setOnClickListener {
            // Navegar a WhyFragment utilizando la acción
            findNavController().navigate(R.id.action_informacionFragment_to_whyAppFragment)
        }

        //Navegacion del CarView del what
        val cardViewWhatFragment = binding.informacionCvWhatispadel
        cardViewWhatFragment.setOnClickListener {
            // Navegar a WhyFragment utilizando la acción
            findNavController().navigate(R.id.action_informacionFragment_to_whatIsPadelFragment)
        }

        //Navegacion del CarView del who
        val cardViewWhoFragment = binding.informacionCvWhocreateapp
        cardViewWhoFragment.setOnClickListener {

            //Dialogo para avisar al usuario que se le llevara a un sitio web
            AlertDialog.Builder(context)
                .setMessage("¿Quieres ir al buscador?")
                .setPositiveButton("Aceptar") { _, _ ->
                    // Crear un Intent para abrir el enlace de LinkedIn
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://www.linkedin.com/in/unai-castro-3458132a6/")
                    )
                    startActivity(intent)

                }
                .setNegativeButton("Cancelar") { dialog, _ ->
                    // Cerrar el diálogo
                    dialog.dismiss()
                }
                .show()

        }


        //Navegacion del CarView del ranking
        val cardViewRankingFragment = binding.informacionCvRankingURL
        cardViewRankingFragment.setOnClickListener {

            //Dialogo para avisar al usuario que le llevamos a un sitio web
            AlertDialog.Builder(context)
                .setMessage("¿Quieres ir al buscador?")
                .setPositiveButton("Aceptar") { _, _ ->
                    // Crear un Intent para abrir el enlace de LinkedIn
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://www.padelfip.com/es/ranking-masculino/")
                    )
                    startActivity(intent)

                }
                .setNegativeButton("Cancelar") { dialog, _ ->
                    // Cerrar el diálogo
                    dialog.dismiss()
                }
                .show()
        }

        // Establecer un OnClickListener para el botón
        val button = binding.informacionBtnCierresesion
        button.setOnClickListener {//Iremos de nuevo a la pantalla login
            // Crear un Intent para abrir LoginActivity
            auth = FirebaseAuth.getInstance()
            auth.signOut()
            val intent = Intent(requireContext(), IniciarSesionActivity::class.java)
            // Iniciar la actividad usando el Intent
            startActivity(intent)
        }
    }

    private fun hacerFoto() {
        val btnFoto = binding.informacionBoton
        btnFoto!!.setOnClickListener{
            Log.i("hacerFoto","EStoy dentro del listener")
            dispatchTakePictureIntent()
        }
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(requireContext().packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Manejar error al crear el archivo de imagen
                    null
                }
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        requireContext(),
                        "com.example.android.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }
    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val file = File(currentPhotoPath)
            val photoURI: Uri = Uri.fromFile(file)

            // Guardar la foto en Firebase Firestore
            guardarFotoEnFirestore(photoURI)
        }
    }

    private fun guardarFotoEnFirestore(photoURI: Uri) {
        val db = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()
        val storageRef = FirebaseStorage.getInstance().reference

        val photoRef = storageRef.child("fotos/${photoURI.lastPathSegment}")
        val uploadTask = photoRef.putFile(photoURI)

        uploadTask.addOnSuccessListener {
            // Foto subida exitosamente
            // Obtener la URL de descarga de la foto
            photoRef.downloadUrl.addOnSuccessListener { uri ->
                // Guardar la URL en Firestore o realizar cualquier otra acción que necesites
                val fotoUrl = uri.toString()
                val userRef=db.collection("users").whereEqualTo("userId",auth.currentUser!!.uid).get().addOnSuccessListener {
                    if (!it.isEmpty){
                        val documentSnapshot = it.documents.first()
                        documentSnapshot.reference.set(fotoUrl, SetOptions.merge())
                    }
                }



            }.addOnFailureListener {
                // Manejar el error al obtener la URL de descarga
            }
        }.addOnFailureListener {
            // Manejar el error al subir la foto
        }
    }


}