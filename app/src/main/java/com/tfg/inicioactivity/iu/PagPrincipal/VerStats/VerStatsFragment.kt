package com.tfg.inicioactivity.iu.PagPrincipal.VerStats

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tfg.inicioactivity.data.model.Partido
import com.tfg.inicioactivity.databinding.FragmentVerStatsBinding
import com.tfg.inicioactivity.iu.Inicio.IniciarSesionActivity

/*class VerStatsFragment : Fragment() {


    private var _binding: FragmentVerStatsBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var partidoAdapter: PartidosAdapter
    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVerStatsBinding.inflate(
            layoutInflater,
            container,
            false
        ) //Con binding podemos conectar directamente el fragment con la vista
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = FirebaseFirestore.getInstance()

        val rvPartidos = binding.verStatsRvPartidos
        rvPartidos.layoutManager = LinearLayoutManager(requireContext())
        partidoAdapter = PartidosAdapter(mutableListOf())
        rvPartidos.adapter = partidoAdapter
        obtenerPartidosDesdeBaseDeDatos()

        val button = binding.verStatsBtnCierreSesion
        button.setOnClickListener {
            // Crear un Intent para abrir LoginActivity
            auth = FirebaseAuth.getInstance()
            auth.signOut()
            val intent = Intent(requireContext(), IniciarSesionActivity::class.java)

            // Iniciar la actividad usando el Intent
            startActivity(intent)
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

    private fun obtenerPartidosDesdeBaseDeDatos() { //Funcion que obtiene todos los partidos que tengo en la base de datos
        db = FirebaseFirestore.getInstance()
        db.collection("partidos")
            .get()
            .addOnSuccessListener { result ->
                val partidosList = mutableListOf<Partido>()
                for (document in result) {
                    val id = document.id
                    println(id)

                    val lugar = document.data["lugar"].toString()
                    val companero = document.data["nombreCompanero"].toString()
                    val resultado = document.data["resultado"].toString()
                    val usuario = document.data["usuario"].toString()

                    val partido = Partido(
                        resultado,
                        usuario,
                        companero,
                        lugar
                    )

                    println(partido)
                    partidosList.add(partido)
                }

                partidoAdapter.apply {
                    partidos = partidosList
                    notifyDataSetChanged()
                }

            }
            .addOnFailureListener { e ->
                Log.e("BUscarPartido", "${e.message}")

            }
    }


}*/

class VerStatsFragment : Fragment() {

    private var _binding: FragmentVerStatsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: VerStatsViewModel
    private lateinit var partidoAdapter: PartidosAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVerStatsBinding.inflate(
            layoutInflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[VerStatsViewModel::class.java]

        val rvPartidos = binding.verStatsRvPartidos
        rvPartidos.layoutManager = LinearLayoutManager(requireContext())
        partidoAdapter = PartidosAdapter(mutableListOf())
        rvPartidos.adapter = partidoAdapter

        viewModel.partidos.observe(viewLifecycleOwner, Observer { partidos ->
            partidoAdapter.partidos = partidos.toMutableList()
            partidoAdapter.notifyDataSetChanged()
        })

        viewModel.obtenerPartidos()

        // Resto del código para el botón de cerrar sesión y el callback de retroceso
    }

    // Resto del código del Fragment
}

