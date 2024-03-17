package com.tfg.inicioactivity.iu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton
import com.tfg.inicioactivity.R
import com.tfg.inicioactivity.iu.Inicio.BienvenidaActivity
import com.tfg.inicioactivity.iu.Inicio.IniciarSesionActivity
import com.tfg.inicioactivity.iu.Inicio.RegistroActivity
import com.tfg.inicioactivity.iu.PagPrincipal.MainActivity

class MenuAPPActivity : AppCompatActivity() {

    private lateinit var btn_Bienvenida:AppCompatButton
    private lateinit var btn_IniciarSesion:AppCompatButton
    private lateinit var btn_Registrarse:AppCompatButton
    private lateinit var btn_PaginaPrincipal:AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_appactivity)
        initComponents()
        initListener()
    }

    private fun initListener() {
        btn_Bienvenida.setOnClickListener {
            val i= Intent(this,BienvenidaActivity::class.java)
            startActivity(i)

        }
        btn_IniciarSesion.setOnClickListener {
            val i= Intent(this,IniciarSesionActivity::class.java)
            startActivity(i)

        }
        btn_Registrarse.setOnClickListener {
            val i= Intent(this,RegistroActivity::class.java)
            startActivity(i)

        }
        btn_PaginaPrincipal.setOnClickListener {
            val i= Intent(this,MainActivity::class.java)
            startActivity(i)

        }
    }

    private fun initComponents() {
        btn_Bienvenida=findViewById(R.id.Menu_btnBienvenida)
        btn_IniciarSesion=findViewById(R.id.Menu_btnIniciarSesion)
        btn_Registrarse=findViewById(R.id.Menu_btnRegistrarse)
        btn_PaginaPrincipal=findViewById(R.id.Menu_btnPagPrincipal)
    }
}