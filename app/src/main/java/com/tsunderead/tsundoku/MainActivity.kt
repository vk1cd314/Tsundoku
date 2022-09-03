package com.tsunderead.tsundoku

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.tsunderead.tsundoku.api_com.Mangadex

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val fetchButton: Button = findViewById(R.id.mangadexfetch)
        fetchButton.setOnClickListener {
            val mangadex= Mangadex(this)
            mangadex.getReq("/cover")
        }
    }
}