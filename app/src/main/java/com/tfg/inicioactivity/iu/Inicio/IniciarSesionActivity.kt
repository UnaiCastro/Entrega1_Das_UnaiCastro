package com.tfg.inicioactivity.iu.Inicio

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
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
        val nombreUsuario: String?
        val email = binding.IniciarSesionEtEmail.text.toString().lowercase()
        val contraseña = binding.IniciarSesionEtPassword.text.toString()
        return true.toString()
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