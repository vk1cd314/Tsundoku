package com.tsunderead.tsundoku.account

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.tsunderead.tsundoku.R

class ForgotPasswordActivity : AppCompatActivity() {

    private val TAG = "ForgotPasswordActivity"
    private lateinit var auth: FirebaseAuth
    private lateinit var email: TextInputEditText
    private lateinit var password: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.forgot_pass)

        auth = Firebase.auth
        email = findViewById(R.id.forgotPasswordEmail)
        password = findViewById(R.id.forgotPasswordPassword)
        val recoverButton = findViewById<Button>(R.id.recover_button)

        recoverButton.setOnClickListener {
            val user = auth.currentUser

            user!!.updatePassword(password.text.toString())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(baseContext, "Password Update Successful", Toast.LENGTH_LONG)

                        val intent = Intent(this, LoginActivity::class.java)
                        intent.putExtra("email", email.text.toString())
                        intent.putExtra("password", password.text.toString())
                        startActivity(intent)
                        finish()
                    }
                }
        }
    }
}