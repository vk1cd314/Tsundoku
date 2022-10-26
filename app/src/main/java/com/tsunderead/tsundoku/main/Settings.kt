package com.tsunderead.tsundoku.main

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tsunderead.tsundoku.account.LoginActivity
import com.tsunderead.tsundoku.R

class Settings : Fragment(R.layout.fragment_settings) {
    private lateinit var viewOfLayout: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        viewOfLayout = inflater.inflate(R.layout.fragment_settings, container, false)
        val logout = viewOfLayout.findViewById<TextView>(R.id.log_out)
        assert(logout != null)
        logout!!.setOnClickListener {
            activity?.let {
                val intent = Intent(it, LoginActivity::class.java)
                it.startActivity(intent)
                it.finish()
            }
        }
        val contactButton = viewOfLayout.findViewById<TextView>(R.id.contact_us)
        contactButton!!.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                val stringarr = arrayOf("mdnokib2000@gmail.com")
                putExtra(Intent.EXTRA_EMAIL, stringarr)
                putExtra(Intent.EXTRA_SUBJECT, "Tsundoku Bug")
            }
            try{
                startActivity(intent)
            }catch(e: ActivityNotFoundException){
                print("how the fuck do you not have a mail app?")
            }
        }
        return viewOfLayout
    }
}

