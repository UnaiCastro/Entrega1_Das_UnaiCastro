package com.tfg.inicioactivity.iu.Inicio

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.tfg.inicioactivity.databinding.ActivityIniciarSesionBinding
import com.tfg.inicioactivity.iu.PagPrincipal.MainActivity
import java.security.MessageDigest

class IniciarSesionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIniciarSesionBinding
    private lateinit var auth: FirebaseAuth


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
            val contrasenaV = binding.IniciarSesionEtPassword.text.toString()
            auth = FirebaseAuth.getInstance()
            auth.signInWithEmailAndPassword(
                binding.IniciarSesionEtEmail.text.toString(),
                contrasenaV
            ).addOnCompleteListener {
                if (it.isSuccessful) {
                    navegacion()
                }
            }.addOnFailureListener { e ->
                mostrarDialogo(e.message)
            }
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

    private fun navegacion() {
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
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