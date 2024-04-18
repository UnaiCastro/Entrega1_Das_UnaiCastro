package com.tfg.inicioactivity.iu.PagPrincipal.info

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
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






}