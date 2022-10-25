package com.tsunderead.tsundoku.community_card_cell

import com.google.firebase.firestore.DocumentReference
import java.net.URL

data class CommunityPost(
    val docRef: DocumentReference,
    val photoURL: URL?,
    val username: String,
    val title: String,
    val description: String,
    val timestamp: String,
    val voteCount: Int
)
