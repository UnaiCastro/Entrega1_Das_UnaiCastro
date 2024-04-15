package com.tfg.inicioactivity.iu.PagPrincipal

import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.tfg.inicioactivity.data.Partido
import com.tfg.inicioactivity.databinding.FragmentRegistarStatsBinding
import com.tfg.inicioactivity.iu.Inicio.IniciarSesionActivity


class RegistarStatsFragment : Fragment() {

    private var _binding: FragmentRegistarStatsBinding? = null
    private lateinit var auth:FirebaseAuth
    private lateinit var db:FirebaseFirestore

    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?


    ): View {
        _binding = FragmentRegistarStatsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val button = binding.registrarBtnCierreSesion
        val btn_guardar = binding.registrarStatsBtnGuardar
        // Establecer un OnClickListener para el botón
        button.setOnClickListener {
            // Crear un Intent para abrir LoginActivity
            auth = FirebaseAuth.getInstance()
            auth.signOut()
            val intent = Intent(requireContext(), IniciarSesionActivity::class.java)
            // Iniciar la actividad usando el Intent
            startActivity(intent)
        }

        btn_guardar.setOnClickListener {
            val et_compañero = binding.registrarStatsEtCompaEro.text.toString()
            val et_lugar = binding.registrarStatsEtLugar.text.toString()
            val spinner_resultado = binding.registrarStatsSpinnerResultado.selectedItem.toString()
            Log.i("et_compañero", "$et_compañero")
            Log.i("et_lugar", "$et_lugar")
            Log.i("spinner_resultado", "$spinner_resultado")



            if (et_compañero.isEmpty() || spinner_resultado.isEmpty() || et_lugar.isEmpty()) {
                // Mostrar un mensaje de error indicando que todos los campos son obligatorios
                AlertDialog.Builder(context)
                    .setMessage("Todos los campos son obligatorios")
                    .setPositiveButton("Aceptar", null)
                    .show()
                return@setOnClickListener
            }

            insertarPartido(et_compañero, spinner_resultado, et_lugar)


        }
        // Crear un callback para manejar la navegación hacia atrás en este fragmento
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Mostrar un Toast con el mensaje
                Toast.makeText(requireContext(), "Por favor, cierre sesión", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        // Agregar el callback al lifecycle owner del fragmento
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    private fun insertarPartido(etCompañero: String, spinnerResultado: String, etLugar: String) {
        auth = FirebaseAuth.getInstance()
        val nombre = auth.currentUser!!.uid
        db = FirebaseFirestore.getInstance()
        val partido = Partido(spinnerResultado,nombre,etCompañero,etLugar)
        db.collection("partidos").add(partido).addOnSuccessListener {
            guardarPartidoenJugador(it.id)
        }
    }

    private fun guardarPartidoenJugador(id: String){
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        val userID=auth.currentUser!!.uid
        db.collection("users").whereEqualTo("userId", userID).get().addOnSuccessListener {
            if (!it.isEmpty){
                val documentSnapshot = it.documents.first()
                val partidos = documentSnapshot.get("partidos") as ArrayList<String>
                partidos.add(id)

                val data = hashMapOf("partidos" to partidos)
                documentSnapshot.reference.set(data, SetOptions.merge()).addOnSuccessListener {
                    Log.d(
                        "CrearPartidoUsuario",
                        "Partido agregado correctamente al usuario $userID"
                    )
                }.addOnFailureListener { e ->
                    Log.e("CrearPartidoUsuario", "Error al agregar partido al usuario $userID: $e")
                }
            }
        }
    }


}



