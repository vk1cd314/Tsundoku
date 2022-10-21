package com.tsunderead.tsundoku.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tsunderead.tsundoku.R
import com.tsunderead.tsundoku.databinding.FragmentHistoryBinding
import com.tsunderead.tsundoku.databinding.FragmentUpdatesBinding

class Updates : Fragment() {
    private var fragmentUpdatesBinding: FragmentUpdatesBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding = FragmentUpdatesBinding.inflate(inflater, container, false)
        fragmentUpdatesBinding = binding
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar = fragmentUpdatesBinding?.updatesToolbar
        toolbar?.inflateMenu(R.menu.updates_toolbar_menu)
        toolbar?.title = "Community"
        toolbar?.setOnMenuItemClickListener{
            // TODO: ADD NEW STUFF HERE
            when(it.itemId){
                R.id.refresh_feed -> {
                    true
                }
                else -> false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentUpdatesBinding = null
    }
}