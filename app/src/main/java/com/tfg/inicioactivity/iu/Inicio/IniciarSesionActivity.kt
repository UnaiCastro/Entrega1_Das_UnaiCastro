package com.tfg.inicioactivity.iu.Inicio

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.tfg.inicioactivity.data.DatabaseHelper
import com.tfg.inicioactivity.databinding.ActivityIniciarSesionBinding
import com.tfg.inicioactivity.iu.PagPrincipal.MainActivity
import java.security.MessageDigest

class IniciarSesionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIniciarSesionBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding =
            ActivityIniciarSesionBinding.inflate(layoutInflater) //Tener conectados la vista con su activity
        setContentView(binding.root)


        initListener()//Funcion que se encarga de gestionar los lstener de los componentes
    }

    private fun initListener() {
        val btn_IniciarSesion = binding.IniciarSesionBtnlogin
        //Lo que hace cuando se pulsa el boton iniciar sesion
        btn_IniciarSesion.setOnClickListener {

            Log.i("PruebaBundle", "Estoy antes del partidos de ejemplo")
            val estaRegistrado =
                mirarSiEstaRegistrado()//Si no devuelve null significa que el correo que se ha metido es valido
            if (estaRegistrado != null) {
                Log.i("PruebaBundle", "$estaRegistrado")
                val i = Intent(this, MainActivity::class.java)
                startActivity(i)
            } else {
                AlertDialog.Builder(this)
                    .setMessage("No coincide el correo o la contraseña")
                    .setPositiveButton("Aceptar", null)
                    .show()
            }
        }
    }

    private fun mirarSiEstaRegistrado(): String? { //Mira si existe el correo puesto y si coincide la contraseña
        val dbHelper = DatabaseHelper(this)
        val db = dbHelper.readableDatabase
        val nombreUsuario: String?
        val email = binding.IniciarSesionEtEmail.text.toString().lowercase()
        val contraseña = binding.IniciarSesionEtPassword.text.toString()

        val cursor = db.query(
            "Usuario",
            arrayOf("nombre", "contraseña"),  // Solo necesitamos el nombre del usuario
            "email = ?",        // Filtro por correo electrónico
            arrayOf(email),    // Argumento del filtro
            null,
            null,
            null
        )

        if (cursor.moveToFirst()) {
            // Obtener el nombre si el cursor no está vacío
            val contraseñaHasheadaEncontrada = cursor.getString(cursor.getColumnIndex("contraseña"))


            // Hashear la contraseña proporcionada por el usuario para compararla con la almacenada en la base de datos
            val contraseñaHasheadaProporcionada = hashString(contraseña)


            if (contraseñaHasheadaProporcionada == contraseñaHasheadaEncontrada) { // Verificar si la contraseña proporcionada coincide con la almacenada en la base de datos
                nombreUsuario = cursor.getString(cursor.getColumnIndex("nombre")).toString()
            } else {
                nombreUsuario = null // La contraseña proporcionada no coincide
            }

        } else {
            nombreUsuario = null // No se encontró ningún usuario con ese correo electrónico
        }

        cursor.close()
        db.close()

        return nombreUsuario
    }

    private fun hashString(input: String): String { //Metodo proporcionado por chatGPT para hashear la contraseña
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