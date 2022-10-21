package com.tsunderead.tsundoku.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.tsunderead.tsundoku.R
import com.tsunderead.tsundoku.databinding.FragmentHistoryBinding

class History : Fragment() {
    private var fragmentHistoryBinding: FragmentHistoryBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding = FragmentHistoryBinding.inflate(inflater, container, false)
        fragmentHistoryBinding = binding
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = fragmentHistoryBinding?.historyToolbar
        toolbar?.inflateMenu(R.menu.history_toolbar_menu)
        toolbar?.title="History"
        toolbar?.setOnMenuItemClickListener{
            //IN CASE WE NEED TO ADD NEW STUFF TO THIS MENU
            when(it.itemId){
                R.id.delete_history -> {
                    true
                }
                else -> false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentHistoryBinding = null
    }
}