package com.tfg.inicioactivity.iu.Inicio

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import com.tfg.inicioactivity.R
import com.tfg.inicioactivity.databinding.ActivityRegistroBinding
import java.security.MessageDigest


class RegistroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroBinding

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
            val et_nombre = binding.RegistroEtNom
            if (et_nombre.toString().isNotEmpty()) {
                registrarEnBaseDeDatos()
            }
        }
    }

    private fun registrarEnBaseDeDatos() {
        val correo = binding.RegistroEtEmail.text.toString()
        val nombre = binding.RegistroEtNom.text.toString()
        val contraseña = binding.RegistroEtContraseA.text.toString()


        if (correo.isEmpty() || nombre.isEmpty() || contraseña.isEmpty()) {// Verificar si todos los campos estan escritos
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

    }

    private fun onClickGuardar() {//Funcion para mostrar notificacion
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

    private fun correoYaRegistrado(correo: String): Boolean { //Mira si el correo que lo pasamos esta registrado o no y devuelve un boolean dependiendo
       return true
    }

}