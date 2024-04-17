package com.tfg.inicioactivity.iu.PagPrincipal.registrarStats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.tfg.inicioactivity.data.RegistrarStats.RegistrarStatsRepository
import com.tfg.inicioactivity.data.model.Partido
import kotlinx.coroutines.launch

class RegistarStatsViewModel : ViewModel() {
    private val repository = RegistrarStatsRepository()
    private lateinit var auth:FirebaseAuth

    fun guardarPartido(companero: String, lugar: String, resultado: String) {
        auth= FirebaseAuth.getInstance()
        val usuario = auth.currentUser!!.uid
        viewModelScope.launch {
            try {
                repository.guardarPartido(Partido(resultado,usuario, companero, lugar))
                // Éxito: mostrar mensaje
            } catch (e: Exception) {
                // Error: mostrar mensaje de error
            }
        }
    }

}
