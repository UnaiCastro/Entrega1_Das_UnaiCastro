package com.tfg.inicioactivity.data.RegistrarStats

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.tfg.inicioactivity.data.model.Partido
import kotlinx.coroutines.tasks.await


class RegistrarStatsRepository {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    suspend fun guardarPartido(partido: Partido) {
        val userID = auth.currentUser?.uid ?: return
        try {
            val documentReference = db.collection("partidos").add(partido).await()
            val partidoId = documentReference.id
            guardarPartidoEnJugador(userID, partidoId)
        } catch (e: Exception) {
            throw Exception("Error al guardar partido: ${e.message}")
        }
    }

    private suspend fun guardarPartidoEnJugador(userID: String, partidoId: String) {
        try {
            val snapshot = db.collection("users").whereEqualTo("userId", userID).get().await()
            if (!snapshot.isEmpty) {
                val documentSnapshot = snapshot.documents.first()
                val partidos = documentSnapshot.get("partidos") as? ArrayList<String> ?: arrayListOf()
                partidos.add(partidoId)

                val data = hashMapOf("partidos" to partidos)
                documentSnapshot.reference.set(data, SetOptions.merge()).await()
            }
        } catch (e: Exception) {
            throw Exception("Error al guardar partido en jugador: ${e.message}")
        }
    }

}
