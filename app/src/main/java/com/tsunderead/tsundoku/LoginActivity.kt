package com.tsunderead.tsundoku

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)
        val loginButton = findViewById<Button>(R.id.login_button)
        loginButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        val signupButton = findViewById<Button>(R.id.signup_button)
        signupButton.setOnClickListener {
            val intent = Intent(this, CreateAccountActivity::class.java)
            startActivity(intent)
            finish()
        }
        val forgotPassword = findViewById<Button>(R.id.forgot_password_button)
        forgotPassword.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}