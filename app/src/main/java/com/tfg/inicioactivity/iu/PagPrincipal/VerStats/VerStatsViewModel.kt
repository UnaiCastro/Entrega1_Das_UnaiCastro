package com.tfg.inicioactivity.iu.PagPrincipal.VerStats

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tfg.inicioactivity.data.VerStats.VerStatsRepository
import com.tfg.inicioactivity.data.model.Partido

class VerStatsViewModel : ViewModel() {

    private val partidosRepository = VerStatsRepository()
    private val _partidos = MutableLiveData<List<Partido>>()
    val partidos: LiveData<List<Partido>> = _partidos

    fun obtenerPartidos() {
        partidosRepository.obtenerPartidos(
            onSuccess = { partidos ->
                _partidos.value = partidos
            },
            onFailure = { exception ->
                Log.e("VerStatsViewModel", "Error al obtener partidos: ${exception.message}")
            }
        )
    }
}
