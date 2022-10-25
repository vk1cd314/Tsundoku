package com.tsunderead.tsundoku.community_helper

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.tsunderead.tsundoku.databinding.ActivityNewPostBinding
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.*


class NewPost : AppCompatActivity() {

    private val tag = "NewPost"
    private lateinit var binding: ActivityNewPostBinding
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewPostBinding.inflate(LayoutInflater.from(this))

        binding.btnSubmitPost.setOnClickListener{
            val user = Firebase.auth.currentUser
            if (user == null) {
                binding.textViewCommunityError.visibility = View.VISIBLE
                Log.e(tag, "messagething")
            }
            else {
                binding.textViewCommunityError.visibility = View.GONE
                var err = false;
                if (binding.txtPostTitle.text.isNullOrEmpty()) {
                    binding.txtParentPostTitle.error = "Add a title to your post"
                    err = true;
                }
                if (binding.txtPostDescription.text.isNullOrEmpty()) {
                    binding.txtParentPostDescription.error = "Add a description to your post"
                    err = true;
                }
                if (!err) {

                    val post = mapOf(
                        "email" to user.email.toString(),
                        "timestamp" to DateTimeFormatter.ISO_INSTANT.format(Instant.now()),
                        "title" to binding.txtPostTitle.text.toString(),
                        "description" to binding.txtPostDescription.text.toString(),
                        "vote" to "0"
                    )

                    db.collection("community").add(post)
                        .addOnSuccessListener {
                            finish()
                        }
                        .addOnFailureListener{
                            Log.e(tag, it.toString())
                            finish()
                        }
                }
            }
        }

        setContentView(binding.root)
    }
}