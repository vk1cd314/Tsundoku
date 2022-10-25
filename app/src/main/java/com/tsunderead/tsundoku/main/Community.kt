package com.tsunderead.tsundoku.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tsunderead.tsundoku.R
import com.tsunderead.tsundoku.databinding.FragmentCommunityBinding
import com.tsunderead.tsundoku.helperclasses.OnSwipeTouchListener

class Community : Fragment(){

    private lateinit var binding: FragmentCommunityBinding

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCommunityBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnCommunityAdd.setOnClickListener{

        }

    }

}