package com.tsunderead.tsundoku.account

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.tsunderead.tsundoku.R

class CreateAccountActivity : AppCompatActivity() {

    private val tag = "CreateAccountActivity"
    private lateinit var auth: FirebaseAuth
    private val db = Firebase.firestore

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

    private fun badAccount() : Boolean {
        var errorText = ""
        if (username.text.isNullOrEmpty()) errorText = "Please enter username"
        else if (email.text.isNullOrEmpty()) errorText = "Please enter email"
        else if (!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()) errorText = "Invalid email"
        else if (password.text.isNullOrEmpty()) errorText = "Please enter password"
        else if (password.text.toString() != confirmPassword.text.toString()) errorText = "Passwords Don't match"

        if (errorText.isNotEmpty()) Toast.makeText(baseContext, errorText, Toast.LENGTH_LONG).show()
        return errorText.isNotEmpty()
    }

    private fun createAccountIfUnique() {
        var uniqueEmail = false
        var uniqueUsername = false
        db.collection("account").whereEqualTo("email", email.text.toString()).get()
            .addOnSuccessListener {
                if (it.size() > 0) Toast.makeText(baseContext, "Email Already Registered", Toast.LENGTH_LONG).show()
                else {
                    uniqueEmail = true
                    if (uniqueEmail && uniqueUsername)
                        firebaseAuth()
                }
            }
        db.collection("account").whereEqualTo("username", username.text.toString()).get()
            .addOnSuccessListener {
                if (it.size() > 0) Toast.makeText(baseContext, "Username Already Registered", Toast.LENGTH_LONG).show()
                else {
                    uniqueUsername = true
                    if (uniqueEmail && uniqueUsername)
                        firebaseAuth()
                }
            }
    }

    private fun createAccount () {
        if (badAccount()) return
        createAccountIfUnique()
    }

    private fun firebaseAuth() {
        val user = hashMapOf(
            "username" to username.text.toString(),
            "email" to email.text.toString(),
            "password" to password.text.toString()
        )
        auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString()).addOnCompleteListener(this) {
            if (it.isSuccessful) {

                db.collection("account").add(user)
                    .addOnSuccessListener {
                        Log.i(tag, "user with info created")
                    }
                    .addOnFailureListener{
                        Log.e(tag, "user addition failed. user: , $user")
                    }

                val intent = Intent(this, LoginActivity::class.java)
                intent.putExtra("email", email.text.toString())
                intent.putExtra("password", password.text.toString())
                startActivity(intent)
                finish()
            }
            else {
                Log.e(tag, it.exception.toString())
            }
        }
    }

}