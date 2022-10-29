package com.tsunderead.tsundoku.community_card_cell

import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.tsunderead.tsundoku.R
import com.tsunderead.tsundoku.databinding.CommunityCardCellBinding

class CommunityPostViewHolder(private val communityPostBinding: CommunityCardCellBinding) :
    RecyclerView.ViewHolder(communityPostBinding.root) {
    private val db = Firebase.firestore
    private val user = Firebase.auth.currentUser
    private val userInteractionCollection = "user_interaction"
    private val communityCollection = "community"
    private lateinit var parentAdapter: CommunityPostAdapter

    fun bindPost(post: CommunityPost, parentAdapter: CommunityPostAdapter) {

        this.parentAdapter = parentAdapter

        val postedOn =
            "Posted on ${post.timestamp.substring(0, 10)} ${post.timestamp.substring(11, 16)}"
        communityPostBinding.textViewUserName.text = post.username
        communityPostBinding.textViewPostTitle.text = post.title
        communityPostBinding.textViewPostDescription.text = post.description
        communityPostBinding.textViewTimestamp.text = postedOn
        communityPostBinding.textViewVoteCounter.text = post.voteCount.toString()

        modifyLooks(post)

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
        if (user == null) Toast.makeText(
            communityPostBinding.root.context,
            "You Have To Login First",
            Toast.LENGTH_SHORT
        ).show()
        else {
            db.collection(userInteractionCollection)
                .whereEqualTo("user", user.email)
                .whereEqualTo("type", "updoot")
                .whereEqualTo("docId", post.docRef.id)
                .get()
                .addOnSuccessListener {
                    if (it.isEmpty) {
                        voteIncrementUI()
                        post.docRef.update("vote", FieldValue.increment(1))

                        val interaction = mapOf(
                            "user" to user.email,
                            "type" to "updoot",
                            "docId" to post.docRef.id
                        )
                        db.collection(userInteractionCollection).add(interaction)

                    } else {
                        post.docRef.update("vote", FieldValue.increment(-1))
                        voteDecrementUI()
                        for (document in it.documents)
                            db.collection(userInteractionCollection).document(document.id).delete()
                        communityPostBinding.btnCommunityUpdoot.setImageResource(R.drawable.ic_baseline_arrow_upward_24)
                        communityPostBinding.btnCommunityDownvote.setImageResource(R.drawable.ic_baseline_arrow_downward_24)
                    }
                }
        }
    }

    private fun downvote(post: CommunityPost) {
        if (user == null) Toast.makeText(
            communityPostBinding.root.context,
            "You Have To Login First",
            Toast.LENGTH_SHORT
        ).show()
        else {
            db.collection(userInteractionCollection)
                .whereEqualTo("user", user.email)
                .whereEqualTo("type", "downvote")
                .whereEqualTo("docId", post.docRef.id)
                .get()
                .addOnSuccessListener {
                    if (it.isEmpty) {
                        voteDecrementUI()

                        post.docRef.update("vote", FieldValue.increment(-1))

                        val interaction = mapOf(
                            "user" to user.email,
                            "type" to "downvote",
                            "docId" to post.docRef.id
                        )
                        db.collection(userInteractionCollection).add(interaction)

                    } else {
                        post.docRef.update("vote", FieldValue.increment(1))
                        voteIncrementUI()
                        for (document in it.documents)
                            db.collection(userInteractionCollection).document(document.id).delete()
                        communityPostBinding.btnCommunityUpdoot.setImageResource(R.drawable.ic_baseline_arrow_upward_24)
                        communityPostBinding.btnCommunityDownvote.setImageResource(R.drawable.ic_baseline_arrow_downward_24)
                    }
                }
        }
    }

    private fun bookmark(post: CommunityPost) {
        if (user == null) Toast.makeText(
            communityPostBinding.root.context,
            "You Have To Login First",
            Toast.LENGTH_SHORT
        ).show()
        else {
            db.collection(userInteractionCollection)
                .whereEqualTo("user", user.email)
                .whereEqualTo("type", "bookmark")
                .whereEqualTo("docId", post.docRef.id)
                .get()
                .addOnSuccessListener {
                    if (it.isEmpty) {
                        communityPostBinding.btnCommunityBookmark.setImageResource(R.drawable.ic_baseline_bookmark_24)
                        val interaction = mapOf(
                            "user" to user.email,
                            "type" to "bookmark",
                            "docId" to post.docRef.id
                        )
                        db.collection(userInteractionCollection).add(interaction)
                    } else {
                        communityPostBinding.btnCommunityBookmark.setImageResource(R.drawable.ic_baseline_bookmark_border_24)
                        for (document in it.documents)
                            db.collection(userInteractionCollection).document(document.id).delete()
                    }
                }
        }
    }

    private fun voteIncrementUI() {
        val voteCount =
            ((communityPostBinding.textViewVoteCounter.text as String).toInt() + 1).toString()
        communityPostBinding.textViewVoteCounter.text = voteCount
        communityPostBinding.btnCommunityUpdoot.setImageResource(R.drawable.ic_baseline_arrow_circle_up_24)
    }

    private fun voteDecrementUI() {
        val voteCount =
            ((communityPostBinding.textViewVoteCounter.text as String).toInt() - 1).toString()
        communityPostBinding.textViewVoteCounter.text = voteCount
        communityPostBinding.btnCommunityDownvote.setImageResource(R.drawable.ic_baseline_arrow_circle_down_24)
    }

    private fun modifyLooks(post: CommunityPost) {
        if (user != null && post.username == user.email) {
            communityPostBinding.btnCommunityOptions.setOnClickListener {
                deletePost(post)
            }
            communityPostBinding.btnCommunityOptions.visibility = android.view.View.VISIBLE
        }
        isUpdooted(post)
        isDownvoted(post)
        isBookmarked(post)
    }

    private fun isUpdooted(post: CommunityPost) {
        if (user != null) {
            db.collection(userInteractionCollection)
                .whereEqualTo("user", user.email)
                .whereEqualTo("type", "updoot")
                .whereEqualTo("docId", post.docRef.id)
                .get()
                .addOnSuccessListener {
                    if (!it.isEmpty)
                        communityPostBinding.btnCommunityUpdoot.setImageResource(R.drawable.ic_baseline_arrow_circle_up_24)
                }
        }
    }

    private fun isDownvoted(post: CommunityPost) {
        if (user != null) {
            db.collection(userInteractionCollection)
                .whereEqualTo("user", user.email)
                .whereEqualTo("type", "downvote")
                .whereEqualTo("docId", post.docRef.id)
                .get()
                .addOnSuccessListener {
                    if (!it.isEmpty)
                        communityPostBinding.btnCommunityDownvote.setImageResource(R.drawable.ic_baseline_arrow_circle_down_24)
                }
        }
    }

    private fun isBookmarked(post: CommunityPost) {
        if (user != null) {
            db.collection(userInteractionCollection)
                .whereEqualTo("user", user.email)
                .whereEqualTo("type", "bookmark")
                .whereEqualTo("docId", post.docRef.id)
                .get()
                .addOnSuccessListener {
                    if (!it.isEmpty)
                        communityPostBinding.btnCommunityBookmark.setImageResource(R.drawable.ic_baseline_bookmark_24)
                }
        }
    }

    private fun deletePost(post: CommunityPost) {

        MaterialAlertDialogBuilder(communityPostBinding.root.context)
            .setTitle("Delete Post?")
            .setMessage("Post will be deleted Permanently")
            .setNegativeButton("Cancel") { _, _ ->

            }
            .setPositiveButton("Delete") { _, _ ->
                db.collection(communityCollection).document(post.docRef.id).delete()
                    .addOnSuccessListener {
                        Toast.makeText(
                            communityPostBinding.root.context,
                            "Post Deleted Successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        parentAdapter.deletePost(layoutPosition)
                    }
                    .addOnFailureListener {
                        Toast.makeText(
                            communityPostBinding.root.context,
                            "Post Could Not Be Deleted",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
            .show()

    }
}