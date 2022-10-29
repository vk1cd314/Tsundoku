package com.tsunderead.tsundoku.account

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.tsunderead.tsundoku.R
import com.tsunderead.tsundoku.main.MainActivity

class LoginActivity : AppCompatActivity() {

    private val TAG = "LoginActivity"

    private val db = Firebase.firestore
    private val auth = Firebase.auth

    private lateinit var email: TextInputEditText
    private lateinit var password: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        email = findViewById(R.id.loginEmail)
        password = findViewById(R.id.loginPassword)

        val extras = intent.extras
        if (extras != null) {
            email.setText(extras.getString("email"))
            password.setText(extras.getString("password"))
        }

        initSignup()
        initForgotPass()
        initSignIn()

    }

    private fun initSignup() {
        val signupButton = findViewById<Button>(R.id.signup_button)
        signupButton.setOnClickListener {
            val intent = Intent(this, CreateAccountActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun initForgotPass() {
        val forgotPassword = findViewById<Button>(R.id.forgot_password_button)
        forgotPassword.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun initSignIn() {
        val loginButton = findViewById<Button>(R.id.loginButton)
        loginButton.setOnClickListener {
            login(email.text.toString(), password.text.toString())
        }
    }

    private fun badLogin(): Boolean {
        var error = ""
        if (email.text.isNullOrEmpty()) error = "Please enter username or email"
        else if (password.text.isNullOrEmpty()) error = "Please enter Password"
        if (error.isNotEmpty()) Toast.makeText(baseContext, error, Toast.LENGTH_SHORT).show()
        return error.isNotEmpty()
    }

    private fun login(loginName: String, loginPass: String) {
        if (badLogin()) return
        if (!Patterns.EMAIL_ADDRESS.matcher(loginName).matches()) {
            db.collection("account").whereEqualTo("username", loginName).get()
                .addOnSuccessListener {
                    if (it.size() > 0) firebaseAuth(
                        it.documents[0].data?.get("email").toString(), loginPass
                    )
                    else Toast.makeText(baseContext, "No Username Found", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Log.e(TAG, "failed", it)
                }
        } else firebaseAuth(loginName, loginPass)
    }

    private fun firebaseAuth(loginMail: String, loginPass: String) {
        auth.signInWithEmailAndPassword(loginMail, loginPass).addOnCompleteListener(this) {
            if (it.isSuccessful) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Log.w(TAG, "Could not sign in", it.exception)
                Toast.makeText(
                    baseContext, "Sign In Failed: No Matching email or password", Toast.LENGTH_SHORT
                ).show()
            }

        }
    }
}
