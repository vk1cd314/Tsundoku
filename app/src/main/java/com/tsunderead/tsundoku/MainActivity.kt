package com.tsunderead.tsundoku

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.tsunderead.tsundoku.api.ApiCall

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val fetchButton: Button = findViewById(R.id.mangadexfetch)
        fetchButton.setOnClickListener {
            ApiCall().execute("manga")
        }
    }
}