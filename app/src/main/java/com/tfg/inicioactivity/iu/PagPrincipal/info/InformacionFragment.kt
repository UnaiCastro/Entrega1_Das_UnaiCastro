package com.tfg.inicioactivity.iu.PagPrincipal.info

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.AppCompatButton
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.tfg.inicioactivity.R
import com.tfg.inicioactivity.databinding.FragmentInformacionBinding
import com.tfg.inicioactivity.databinding.FragmentVerStatsBinding
import com.tfg.inicioactivity.iu.Inicio.IniciarSesionActivity


class InformacionFragment : Fragment() {

    private var _binding: FragmentInformacionBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth:FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
        initListener(view)

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

    private fun initListener(view: View) {

        //Navegacion del CarView del why
        val cardViewWhyFragment = view.findViewById<CardView>(R.id.informacion_cvWhyapp)
        cardViewWhyFragment.setOnClickListener {
            // Navegar a WhyFragment utilizando la acción
            findNavController().navigate(R.id.action_informacionFragment_to_whyAppFragment)
        }

        //Navegacion del CarView del what
        val cardViewWhatFragment = view.findViewById<CardView>(R.id.informacion_cvWhatispadel)
        cardViewWhatFragment.setOnClickListener {
            // Navegar a WhyFragment utilizando la acción
            findNavController().navigate(R.id.action_informacionFragment_to_whatIsPadelFragment)
        }

        //Navegacion del CarView del who
        val cardViewWhoFragment = view.findViewById<CardView>(R.id.informacion_cvWhocreateapp)
        cardViewWhoFragment.setOnClickListener {

            //Dialogo para avisar al usuario que se le llevara a un sitio web
            AlertDialog.Builder(view.context)
                .setMessage("¿Quieres ir al buscador?")
                .setPositiveButton("Aceptar") { _, _ ->
                    // Crear un Intent para abrir el enlace de LinkedIn
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://www.linkedin.com/in/unai-castro-3458132a6/")
                    )
                    startActivity(intent)
                    // Comprobar si hay alguna actividad que pueda manejar este Intent
                    /* if (intent.resolveActivity(requireActivity().packageManager) != null) {
                         // Abrir el enlace en un navegador web
                         startActivity(intent)
                     }*/
                }
                .setNegativeButton("Cancelar") { dialog, _ ->
                    // Cerrar el diálogo
                    dialog.dismiss()
                }
                .show()

        }


        //Navegacion del CarView del ranking
        val cardViewRankingFragment = view.findViewById<CardView>(R.id.informacion_cvRankingURL)
        cardViewRankingFragment.setOnClickListener {

            //Dialogo para avisar al usuario que le llevamos a un sitio web
            AlertDialog.Builder(view.context)
                .setMessage("¿Quieres ir al buscador?")
                .setPositiveButton("Aceptar") { _, _ ->
                    // Crear un Intent para abrir el enlace de LinkedIn
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://www.padelfip.com/es/ranking-masculino/")
                    )
                    startActivity(intent)
                    // Comprobar si hay alguna actividad que pueda manejar este Intent
                    if (intent.resolveActivity(requireActivity().packageManager) != null) {
                        // Abrir el enlace en un navegador web
                        startActivity(intent)
                    }
                }
                .setNegativeButton("Cancelar") { dialog, _ ->
                    // Cerrar el diálogo
                    dialog.dismiss()
                }
                .show()
        }

        // Establecer un OnClickListener para el botón
        val button = view.findViewById<AppCompatButton>(R.id.informacion_btnCierresesion)
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