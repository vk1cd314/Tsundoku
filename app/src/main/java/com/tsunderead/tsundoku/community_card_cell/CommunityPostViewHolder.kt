package com.tsunderead.tsundoku.community_card_cell

import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.tsunderead.tsundoku.databinding.CommunityCardCellBinding

class CommunityPostViewHolder(private val communityPostBinding: CommunityCardCellBinding): RecyclerView.ViewHolder(communityPostBinding.root) {
    private val db = Firebase.firestore
    fun bindPost(post: CommunityPost) {
        communityPostBinding.textViewUserName.text = post.username
        communityPostBinding.textViewPostTitle.text = post.title
        communityPostBinding.textViewPostDescription.text = post.description
        communityPostBinding.textViewTimestamp.text = "Posted on ${post.timestamp.substring(0, 10)} ${post.timestamp.substring(11, 16)}"
        communityPostBinding.textViewVoteCounter.text = post.voteCount.toString()

        communityPostBinding.btnCommunityUpdoot.setOnClickListener {
            post.docRef.update("vote", FieldValue.increment(1))
        }
        communityPostBinding.btnCommunityDownvote.setOnClickListener {
            post.docRef.update("vote", FieldValue.increment(-1))
        }
    }
}