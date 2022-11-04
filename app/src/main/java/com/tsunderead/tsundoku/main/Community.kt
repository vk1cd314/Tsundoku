package com.tsunderead.tsundoku.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.tsunderead.tsundoku.R
import com.tsunderead.tsundoku.community_card_cell.CommunityPost
import com.tsunderead.tsundoku.community_card_cell.CommunityPostAdapter
import com.tsunderead.tsundoku.community_helper.NewPost
import com.tsunderead.tsundoku.databinding.FragmentCommunityBinding
import java.net.URI

class Community : Fragment() {

    private lateinit var binding: FragmentCommunityBinding
    private lateinit var skeleton: Skeleton
    private var postList = ArrayList<CommunityPost>()
    private val db = Firebase.firestore
    private var postListFetched = false
    private var updootFetched = false
    private var bookmarkFetched = false

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCommunityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnCommunityAdd.setOnClickListener {
            val intent = Intent(activity, NewPost::class.java)
            activity?.startActivity(intent)
        }
        binding.rViewCommunity.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rViewCommunity.setHasFixedSize(false)

        skeleton = binding.rViewCommunity.applySkeleton(R.layout.community_card_cell)
        skeleton.showSkeleton()
        getPost()
    }

    private fun getPost() {

        postListFetched = false
        updootFetched = false
        bookmarkFetched = false

        db.collection("community").orderBy("timestamp", Query.Direction.DESCENDING).limit(20).get()
            .addOnSuccessListener {
                postList = ArrayList()

                val postCount = it.size()

                if (postCount == 0) {
                    skeleton.showOriginal()
                    binding.rViewCommunity.adapter = CommunityPostAdapter(postList)
                }

                var accountRetrieved = 0;

                for (document in it) {

                    var communityPost: CommunityPost? = null;
                    db.collection("account")
                        .whereEqualTo("email", document.data["email"] as String)
                        .get()
                        .addOnSuccessListener { accountSnapshot ->
                            accountRetrieved++
                            for (account in accountSnapshot) {
                                var photoUri: URI? = null
                                if (account.data["photoUri"] != null)
                                    photoUri = URI(account.data["photoUri"] as String)
                                communityPost = CommunityPost(
                                    document.reference,
                                    photoUri,
                                    account.data["username"] as String,
                                    document.data["title"] as String,
                                    document.data["description"] as String,
                                    document.data["timestamp"] as String,
                                    document.data["vote"].toString().toInt()
                                )
                                break
                            }
                            try {
                                postList.add(communityPost!!)
                            } catch (_: Exception) {
                            }
                            if (accountRetrieved == postCount) {
                                skeleton.showOriginal()
                                binding.rViewCommunity.adapter = CommunityPostAdapter(postList)
                            }
                        }
                }

            }
            .addOnFailureListener {
                Log.e(tag, "Post loading failed: $it")
            }
    }

}