package com.tfg.inicioactivity.iu.PagPrincipal.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tfg.inicioactivity.R


class whyAppFragment : Fragment() { //PANTALLA QUE MUESTRA UNA INFORMACION de qque es ell padel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_why_app, container, false)
    }


}