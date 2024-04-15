package com.tfg.inicioactivity.iu.Inicio

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tfg.inicioactivity.databinding.ActivityBienvenidaBinding

class BienvenidaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBienvenidaBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBienvenidaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initListener()

    }

    private fun initListener() {

        val btn_Registarse = binding.BienvenidaBtnRegistrarse
        btn_Registarse.setOnClickListener {

            val i = Intent(this, RegistroActivity::class.java)
            startActivity(i)
        }

        val btn_IniciarSesion = binding.BienvenidaBtnIniciarSesion
        btn_IniciarSesion.setOnClickListener {
            val i = Intent(this, IniciarSesionActivity::class.java)
            startActivity(i)
        }

    }


}