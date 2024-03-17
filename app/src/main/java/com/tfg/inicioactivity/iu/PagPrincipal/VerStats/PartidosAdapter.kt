package com.tfg.inicioactivity.iu.PagPrincipal.VerStats

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tfg.inicioactivity.R
import com.tfg.inicioactivity.data.Partido

class PartidosAdapter(private val partidos: MutableList<Partido>) :
    RecyclerView.Adapter<PartidoViewHolder>() {
    //  Adaptador para coger la informacion de la clase partido y printearla
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartidoViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_partidos, parent, false)
        return PartidoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PartidoViewHolder, position: Int) {
        val partido = partidos[position]
        holder.render(partido)
    }

    override fun getItemCount() = partidos.size
}