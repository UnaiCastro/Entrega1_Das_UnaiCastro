package com.tfg.inicioactivity.data.VerStats

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tfg.inicioactivity.data.model.Partido

class VerStatsRepository {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun obtenerPartidos(onSuccess: (List<Partido>) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("partidos")
            .get()
            .addOnSuccessListener { result ->
                val partidosList = mutableListOf<Partido>()
                for (document in result) {
                    val usuario = document.data["usuario"].toString()
                    if (usuario==auth.currentUser!!.uid){
                        val lugar = document.data["lugar"].toString()
                        val companero = document.data["nombreCompanero"].toString()
                        val resultado = document.data["resultado"].toString()
                        val partido = Partido(
                            resultado,
                            usuario,
                            companero,
                            lugar
                        )
                        partidosList.add(partido)
                    }
                }
                onSuccess(partidosList)
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }
}