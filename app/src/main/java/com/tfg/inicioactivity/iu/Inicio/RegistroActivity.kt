package com.tfg.inicioactivity.iu.Inicio

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tfg.inicioactivity.R
import com.tfg.inicioactivity.data.model.Usuario
import com.tfg.inicioactivity.databinding.ActivityRegistroBinding
import java.security.MessageDigest


class RegistroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroBinding
    private lateinit var auth:FirebaseAuth
    private lateinit var db:FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Tener conectados la vista con su activity
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initListener() //Funcion que se encarga de gestionar los lstener de los componentes
    }

    private fun initListener() {
        val btn_Registrarse = binding.REgistroBtnRegistro
        btn_Registrarse.setOnClickListener {
                registrarEnBaseDeDatos()
        }
    }

    private fun registrarEnBaseDeDatos() {
        val correo = binding.RegistroEtEmail.text.toString()
        val nombre = binding.RegistroEtNom.text.toString()
        val contrasena = binding.RegistroEtContraseA.text.toString()


        if (correo.isEmpty() || nombre.isEmpty() || contrasena.isEmpty()) {// Verificar si todos los campos estan escritos
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(correo,contrasena).addOnCompleteListener {
            if (it.isSuccessful){
                activarNotificacion()
                cambioPantalla()
                crearUsuarioEnFirestore()
            }
        }.addOnFailureListener { e->
            mostrarDialogo(e.message)
            Log.e("Registro","${e.message}")

        }


    }

    private fun mostrarDialogo(message: String?) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                // You can add any action you want when the user clicks OK
                dialog.dismiss()
            }
        builder.create().show()
    }

    private fun crearUsuarioEnFirestore() {
        db= FirebaseFirestore.getInstance()
        val nombre = binding.RegistroEtNom.text.toString()
        val correo = binding.RegistroEtEmail.text.toString()
        val contrasenaV = binding.RegistroEtContraseA.text.toString()
        val contrasena2 = hashString(contrasenaV)
        auth = FirebaseAuth.getInstance()
        val usuarioID = auth.currentUser?.uid
        val user = Usuario (
            userID = usuarioID.toString(),
            nombre = nombre,
            correo = correo,
            contrasena = contrasena2,
            partidos = mutableListOf()
        ).toMap()

        db.collection("users").add(user).addOnSuccessListener {
            Log.i("Registro","Creado usuario $usuarioID")
        }.addOnFailureListener {
            Log.e("Registro","Creacion de Usuario $usuarioID fallida ${it.message}")
        }
    }

    private fun activarNotificacion() {//Funcion para mostrar notificacion
        val et_nombre = binding.RegistroEtNom
        val titulo = "Registro correcto"
        val contenido = "${et_nombre.text} bienvenido a PaLaStats"
        mostrarNotificacion(this, titulo, contenido)
    }

    private fun cambioPantalla() { //Cambia la pantalla al login al pulsar aceptar en el dialogo Alert
        val i = Intent(this, IniciarSesionActivity::class.java)
        startActivity(i)
    }

    private fun mostrarNotificacion(
        context: Context,
        titulo: String,
        contenido: String
    ) { //Funcion que crea la notificacion y la muestra
        // Crear un ID único para la notificación
        val notificationId = 1

        // Crear un canal de notificación
        val channelId = "mi_canal"
        val channelName = "Mi Canal de Notificación"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val notificationChannel = NotificationChannel(channelId, channelName, importance)

        // Configurar el gestor de notificaciones
        val notificationManager = getSystemService(this, NotificationManager::class.java)
        notificationManager?.createNotificationChannel(notificationChannel)

        // Crear y configurar la notificación
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_jugar) // Icono de la notificación
            .setContentTitle(titulo) // Título de la notificación
            .setContentText(contenido) // Contenido de la notificación
            .setPriority(NotificationCompat.PRIORITY_DEFAULT) // Prioridad de la notificación

        // Mostrar la notificación
        notificationManager?.notify(notificationId, builder.build())
    }

    private fun hashString(input: String): String { //Funcion que hashea la contraseña
        val HEX_CHARS = "0123456789ABCDEF"
        val bytes = MessageDigest.getInstance("SHA-256").digest(input.toByteArray())
        val result = StringBuilder(bytes.size * 2)
        bytes.forEach {
            val i = it.toInt()
            result.append(HEX_CHARS[i shr 4 and 0x0f])
            result.append(HEX_CHARS[i and 0x0f])
        }
        return result.toString()
    }

}