package com.tsunderead.tsundoku.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.tsunderead.tsundoku.NewPost
import com.tsunderead.tsundoku.databinding.FragmentCommunityBinding

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
            NewPost().show(childFragmentManager, "New Post")
//            val fragmentManger =  childFragmentManager
//            val transaction = fragmentManger.beginTransaction()
//            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
//            transaction.add(android.R.id.content, NewPost()).addToBackStack(null).commit()
        }

    }

}