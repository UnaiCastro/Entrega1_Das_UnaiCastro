package com.tfg.inicioactivity.data.model

data class Usuario(
    val userID: String,
    val nombre: String,
    val correo: String,
    val contrasena: String,
    val partidos: MutableList<String>
    ) {
    fun toMap():MutableMap<String, Any>{
        return mutableMapOf(
            "userId" to userID,
            "nombre" to nombre,
            "correo" to correo,
            "contrasena" to contrasena,
            "partidos" to partidos
        )
    }

}
