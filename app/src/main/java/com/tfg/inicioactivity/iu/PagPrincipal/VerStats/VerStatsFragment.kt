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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tfg.inicioactivity.data.Partido
import com.tfg.inicioactivity.databinding.FragmentVerStatsBinding
import com.tfg.inicioactivity.iu.Inicio.IniciarSesionActivity

class VerStatsFragment : Fragment() {


    private var _binding: FragmentVerStatsBinding? = null
    private val binding get() = _binding!!

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

        val recyclerView: RecyclerView = binding.verStatsRvPartidos
        val partidos: MutableList<Partido> = obtenerPartidosDesdeBaseDeDatos()
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = PartidosAdapter(partidos)

        val button = binding.verStatsBtnCierreSesion
        button.setOnClickListener {
            // Crear un Intent para abrir LoginActivity
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

    private fun obtenerPartidosDesdeBaseDeDatos(): MutableList<Partido> { //Funcion que obtiene todos los partidos que tengo en la base de datos
        return mutableListOf()
    }


}

