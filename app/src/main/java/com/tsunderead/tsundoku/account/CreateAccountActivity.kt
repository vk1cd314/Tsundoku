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

class CreateAccountActivity : AppCompatActivity() {

    private val tag = "CreateAccountActivity"
    private lateinit var auth: FirebaseAuth

    private lateinit var username: TextInputEditText
    private lateinit var email: TextInputEditText
    private lateinit var password: TextInputEditText
    private lateinit var confirmPassword: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_account)

        auth = Firebase.auth
        username = findViewById(R.id.signupUsername)
        email = findViewById(R.id.signupEmail)
        password = findViewById(R.id.signupPassword)
        confirmPassword = findViewById(R.id.signupConfirmPassword)

        val signup = findViewById<Button>(R.id.signup)
        signup.setOnClickListener {
            createAccount()
        }
    }

    private fun badAccountHandling() : Boolean {
        var errorText = ""
        if (username.text.isNullOrEmpty()) errorText = "Please enter username"
        else if (email.text.isNullOrEmpty()) errorText = "Please enter email"
        else if (password.text.isNullOrEmpty()) errorText = "Please enter password"
        else if (password.text.toString() != confirmPassword.text.toString()) errorText = "Passwords Don't match"
        Log.i(tag, password.text.toString())
        Log.i(tag, confirmPassword.text.toString())
        Log.e(tag, (password.text != confirmPassword.text).toString())
        if (errorText.isNotEmpty()) Toast.makeText(baseContext, errorText, Toast.LENGTH_LONG).show()
        return errorText.isNotEmpty()
    }

    private fun createAccount () {
        if (badAccountHandling()) return
        auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString()).addOnCompleteListener(this) {
            if (it.isSuccessful) {
                val intent = Intent(this, LoginActivity::class.java)
                intent.putExtra("email", email.text.toString())
                intent.putExtra("password", password.text.toString())
                startActivity(intent)
                finish()
            }
            else {
                Log.e(tag, it.exception.toString())
                Toast.makeText(baseContext, "Please enter a valid email address", Toast.LENGTH_SHORT).show()
            }
        }
    }

}