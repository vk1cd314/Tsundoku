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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.tsunderead.tsundoku.R
import com.tsunderead.tsundoku.community_card_cell.CommunityPost
import com.tsunderead.tsundoku.community_card_cell.CommunityPostAdapter
import com.tsunderead.tsundoku.community_helper.NewPost
import com.tsunderead.tsundoku.databinding.FragmentCommunityBinding

class Community : Fragment() {

    private lateinit var binding: FragmentCommunityBinding
    private lateinit var skeleton: Skeleton
    var postList = ArrayList<CommunityPost>()
    private val db = Firebase.firestore
    private var postListFetched = false;
    private var updootFetched = false;
    private var bookmarkFetched = false;

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
        Log.i(tag, Firebase.auth.currentUser!!.email!!)
        binding.btnCommunityAdd.setOnClickListener {
            val intent = Intent(activity, NewPost::class.java)
            activity?.startActivity(intent)
        }
        binding.rViewCommunity.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rViewCommunity.setHasFixedSize(false)

        skeleton = binding.rViewCommunity.applySkeleton(R.layout.community_card_cell)
        skeleton.showSkeleton()
        getPost()
    }

    override fun onResume() {
        super.onResume()
        getPost()
    }

    private fun getPost() {

        postListFetched = false
        updootFetched = false
        bookmarkFetched = false

        db.collection("community").orderBy("timestamp", Query.Direction.DESCENDING).limit(20).get()
            .addOnSuccessListener {
                postList = ArrayList()
                for (document in it) {
                    val communityPost = CommunityPost(
                        document.reference,
                        null,
                        document.data["email"] as String,
                        document.data["title"] as String,
                        document.data["description"] as String,
                        document.data["timestamp"] as String,
                        document.data["vote"].toString().toInt()
                    )

                    postList.add(communityPost)
                }

                skeleton.showOriginal()
                binding.rViewCommunity.adapter = CommunityPostAdapter(postList)
            }
            .addOnFailureListener {
                Log.e(tag, "Post loading failed: $it")
            }
    }

}