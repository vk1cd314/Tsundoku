package com.tsunderead.tsundoku.community_helper

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.tsunderead.tsundoku.databinding.ActivityNewPostBinding
import java.time.Instant
import java.time.format.DateTimeFormatter


class NewPost : AppCompatActivity() {

    private val tag = "NewPost"
    private lateinit var binding: ActivityNewPostBinding
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewPostBinding.inflate(LayoutInflater.from(this))

        val extras = intent.extras
        if (extras != null) {
            val titleString = "Reading ${extras.getString("title")}"
            binding.txtPostTitle.setText(titleString)
        }

        binding.btnSubmitPost.setOnClickListener {
            submitPost()
        }

        setContentView(binding.root)
    }

    private fun submitPost() {
        val user = Firebase.auth.currentUser
        if (user == null) Toast.makeText(baseContext, "You must sign in to post", Toast.LENGTH_LONG)
            .show()
        else {
            binding.textViewCommunityError.visibility = View.GONE
            var err = false
            if (binding.txtPostTitle.text.isNullOrEmpty()) {
                binding.txtParentPostTitle.error = "Add a title to your post"
                err = true
            }
            if (binding.txtPostDescription.text.isNullOrEmpty()) {
                binding.txtParentPostDescription.error = "Add a description to your post"
                err = true
            }
            if (!err) {

                val post = mapOf(
                    "email" to user.email.toString(),
                    "timestamp" to DateTimeFormatter.ISO_INSTANT.format(Instant.now()),
                    "title" to binding.txtPostTitle.text.toString(),
                    "description" to binding.txtPostDescription.text.toString(),
                    "vote" to "0"
                )

                db.collection("community").add(post).addOnSuccessListener {
                    finish()
                }.addOnFailureListener {
                    Log.e(tag, it.toString())
                    finish()
                }
            }
        }
    }
}