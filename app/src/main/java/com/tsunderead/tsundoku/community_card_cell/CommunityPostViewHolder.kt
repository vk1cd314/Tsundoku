package com.tsunderead.tsundoku.community_card_cell

import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.tsunderead.tsundoku.R
import com.tsunderead.tsundoku.databinding.CommunityCardCellBinding

class CommunityPostViewHolder(private val communityPostBinding: CommunityCardCellBinding): RecyclerView.ViewHolder(communityPostBinding.root) {
    private val db = Firebase.firestore
    val user = Firebase.auth.currentUser

    fun bindPost(post: CommunityPost) {
        val postedOn = "Posted on ${post.timestamp.substring(0, 10)} ${post.timestamp.substring(11, 16)}"
        communityPostBinding.textViewUserName.text = post.username
        communityPostBinding.textViewPostTitle.text = post.title
        communityPostBinding.textViewPostDescription.text = post.description
        communityPostBinding.textViewTimestamp.text = postedOn
        communityPostBinding.textViewVoteCounter.text = post.voteCount.toString()

        communityPostBinding.btnCommunityUpdoot.setOnClickListener {
            updoot(post)
        }
        communityPostBinding.btnCommunityDownvote.setOnClickListener {
            downvote(post)
        }
        communityPostBinding.btnCommunityBookmark.setOnClickListener {
            bookmark(post)
        }
    }

    private fun updoot(post: CommunityPost) {
        if (user == null) Toast.makeText(communityPostBinding.root.context, "You Have To Login First", Toast.LENGTH_SHORT).show()
        else {
            voteIncrement()
            post.docRef.update("vote", FieldValue.increment(1))
                .addOnSuccessListener {
                    val interaction = mapOf(
                        "user" to user.email,
                        "type" to "updoot",
                        "docId" to post.docRef.id
                    )
                    db.collection("user_interaction").add(interaction)
                }
                .addOnFailureListener {
                    voteDecrement()
                }
        }
    }

    private fun downvote (post: CommunityPost) {
        if (user == null) Toast.makeText(communityPostBinding.root.context, "You Have To Login First", Toast.LENGTH_SHORT).show()
        else {
            voteDecrement()
            post.docRef.update("vote", FieldValue.increment(-1))
                .addOnSuccessListener {
                    val interaction = mapOf(
                        "user" to user.email,
                        "type" to "downvote",
                        "docId" to post.docRef.id
                    )
                    db.collection("user_interaction").add(interaction)
                }
                .addOnFailureListener {
                    voteIncrement()
                }
        }
    }

    private fun bookmark (post: CommunityPost) {
        if (user == null) Toast.makeText(communityPostBinding.root.context, "You Have To Login First", Toast.LENGTH_SHORT).show()
        else {
            val interaction = mapOf(
                "user" to user.email,
                "type" to "bookmark",
                "docId" to post.docRef.id
            )
            db.collection("user_interaction").add(interaction)
                .addOnSuccessListener {
                    communityPostBinding.btnCommunityBookmark.setBackgroundResource(R.drawable.ic_baseline_bookmark_24)
                }
        }
    }

    private fun voteIncrement() {
        val voteCount = ((communityPostBinding.textViewVoteCounter.text as String).toInt() + 1).toString()
        communityPostBinding.textViewVoteCounter.text = voteCount
        communityPostBinding.btnCommunityUpdoot.setBackgroundResource(R.drawable.ic_baseline_arrow_circle_up_24)
    }

    private fun voteDecrement() {
        val voteCount = ((communityPostBinding.textViewVoteCounter.text as String).toInt() - 1).toString()
        communityPostBinding.textViewVoteCounter.text = voteCount
        communityPostBinding.btnCommunityDownvote.setBackgroundResource(R.drawable.ic_baseline_arrow_circle_down_24)
    }

}