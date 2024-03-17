package com.tfg.inicioactivity.iu.PagPrincipal.VerStats

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tfg.inicioactivity.R
import com.tfg.inicioactivity.data.Partido

class PartidoViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) { //Clase integra del adapater que la he sacado por comodidad. Clase que ayuda a printear la informacion
    private val resultadoTextView: TextView = itemView.findViewById(R.id.CVpartido_resultado)
    private val emailUsuarioTextView: TextView = itemView.findViewById(R.id.CVpartido_usuario)
    private val nombreCompaneroTextView: TextView = itemView.findViewById(R.id.CVpartido_compañero)
    private val lugarTextView: TextView = itemView.findViewById(R.id.CVpartido_lugar)

    fun render(partido: Partido) {
        resultadoTextView.text = partido.resultado
        if (partido.resultado == "Victoria") {
            resultadoTextView.setTextColor(resultadoTextView.context.getColor(R.color.win))
        }

        if (partido.resultado == "Derrota") {
            resultadoTextView.setTextColor(resultadoTextView.context.getColor(R.color.lose))
        }

        if (partido.resultado == "Empate") {
            resultadoTextView.setTextColor(resultadoTextView.context.getColor(R.color.draw))
        }
        emailUsuarioTextView.text = partido.emailUsuario
        nombreCompaneroTextView.text = partido.nombreCompanero
        lugarTextView.text = partido.lugar
    }
}