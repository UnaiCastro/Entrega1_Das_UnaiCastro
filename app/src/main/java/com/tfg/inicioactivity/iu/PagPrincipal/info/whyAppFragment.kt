package com.tfg.inicioactivity.iu.PagPrincipal.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tfg.inicioactivity.R
import com.tfg.inicioactivity.databinding.FragmentVerStatsBinding
import com.tfg.inicioactivity.databinding.FragmentWhyAppBinding


class whyAppFragment : Fragment() { //PANTALLA QUE MUESTRA UNA INFORMACION de qque es ell padel

    private var _binding: FragmentWhyAppBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentWhyAppBinding.inflate(
            layoutInflater,
            container,
            false
        )
        return binding.root
    }


}