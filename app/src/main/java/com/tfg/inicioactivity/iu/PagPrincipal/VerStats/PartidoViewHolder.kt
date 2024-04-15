package com.tfg.inicioactivity.iu.PagPrincipal.VerStats

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tfg.inicioactivity.R
import com.tfg.inicioactivity.data.Partido
import com.tfg.inicioactivity.databinding.ItemPartidosBinding

class PartidoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) { //Clase integra del adapater que la he sacado por comodidad. Clase que ayuda a printear la informacion

    private val binding = ItemPartidosBinding.bind(itemView)

    fun render(partido: Partido) {
        binding.CVpartidoResultado.text = partido.resultado
        if (partido.resultado == "Victoria") {
            binding.CVpartidoResultado.setTextColor(binding.CVpartidoResultado.context.getColor(R.color.win))
        }

        if (partido.resultado == "Derrota") {
            binding.CVpartidoResultado.setTextColor(binding.CVpartidoResultado.context.getColor(R.color.lose))
        }

        if (partido.resultado == "Empate") {
            binding.CVpartidoResultado.setTextColor(binding.CVpartidoResultado.context.getColor(R.color.draw))
        }
        binding.CVpartidoUsuario.text = partido.emailUsuario
        binding.CVpartidoCompaEro.text = partido.nombreCompanero
        binding.CVpartidoLugar.text = partido.lugar
    }
}