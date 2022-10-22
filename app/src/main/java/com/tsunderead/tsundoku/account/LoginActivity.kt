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
import com.tsunderead.tsundoku.main.MainActivity

class LoginActivity : AppCompatActivity() {

    private val TAG = "LoginActivity"
    private lateinit var auth: FirebaseAuth
    private lateinit var email: TextInputEditText
    private lateinit var password: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        auth = Firebase.auth
        email = findViewById(R.id.loginEmail)
        password = findViewById(R.id.loginPassword)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val signupButton = findViewById<Button>(R.id.signup_button)
        val forgotPassword = findViewById<Button>(R.id.forgot_password_button)

        val extras = intent.extras
        if (extras != null) {
            email.setText(extras.getString("email"))
            password.setText(extras.getString("password"))
        }

        loginButton.setOnClickListener {
            auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString()).addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }else {
                    Log.w(TAG, "Could not sign in", it.exception)
                    Toast.makeText(baseContext, "Sign In Failed: No Matching email or password", Toast.LENGTH_SHORT).show()
                }

            }
        }
        signupButton.setOnClickListener {
            val intent = Intent(this, CreateAccountActivity::class.java)
            startActivity(intent)
            finish()
        }
        forgotPassword.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}