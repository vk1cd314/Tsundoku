package com.tsunderead.tsundoku.community_card_cell

import com.google.firebase.firestore.DocumentReference
import java.net.URI

data class CommunityPost(
    val docRef: DocumentReference,
    val photoURL: URI?,
    val username: String,
    val title: String,
    val description: String,
    val timestamp: String,
    var voteCount: Int
)
