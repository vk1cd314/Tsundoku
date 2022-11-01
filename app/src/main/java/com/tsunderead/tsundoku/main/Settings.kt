package com.tsunderead.tsundoku.main

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton
import com.faltenreich.skeletonlayout.createSkeleton
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.tsunderead.tsundoku.R
import com.tsunderead.tsundoku.account.LoginActivity
import com.tsunderead.tsundoku.community_card_cell.CommunityPost
import com.tsunderead.tsundoku.community_card_cell.ReducedCommunityPostAdapter
import com.tsunderead.tsundoku.databinding.FragmentSettingsBinding

class Settings : Fragment(R.layout.fragment_settings) {
    private lateinit var binding: FragmentSettingsBinding
    private var user : FirebaseUser? = null
    private val db = Firebase.firestore
    private lateinit var profileSkeleton: Skeleton
    private lateinit var recyclerViewSkeleton: Skeleton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)

        user = Firebase.auth.currentUser

        if (user == null) {
            try {
                Firebase.auth.signOut()
            } catch (_: Exception) {
            }
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            requireActivity().startActivity(intent)
        }
        else signedIn()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        user = Firebase.auth.currentUser
        if (user == null)
            Navigation.findNavController(requireView()).navigate(R.id.action_profile3_to_library3)
    }

    private fun signedIn() {
        profileSkeleton = binding.skeletonLayoutProfile.createSkeleton()
        profileSkeleton.showSkeleton()

        binding.btnLogout.setOnClickListener {
            Firebase.auth.signOut()
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            requireActivity().startActivity(intent)
        }

        binding.btnContactUs.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf("mdnokib2000@gmail.com"))
                putExtra(Intent.EXTRA_SUBJECT, "Tsundoku Bug")
            }
            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(
                    context,
                    "You have no email app. How did this happen, how do you have no email app? Open a browser and mail us at mdnokib2000@gmail.com. We need to discuss the lack of mail app first.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        populateProfile()
    }

    private fun populateProfile () {

        if (user == null) {
            Toast.makeText(context, "Could not get user profile information. Please check your connection", Toast.LENGTH_SHORT).show()
            return
        }

        val recyclerView = binding.rViewProfile
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)


        binding.tagToggleLike.setOnClickListener{
            getHistoryData()
        }

        binding.tagToggleBookmark.setOnClickListener {
            getBookmarkData()
        }

        binding.tagTogglePost.setOnClickListener {
            getPostsData()
        }

        db.collection("account").whereEqualTo("email", user?.email).get()
            .addOnSuccessListener {
                for (document in it) {
                    binding.txtUsername.text = document.data["username"] as CharSequence?
                    binding.txtEmail.text = document.data["email"] as CharSequence?
                    break
                }
                profileSkeleton.showOriginal()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Could not get user profile information. Please check your connection", Toast.LENGTH_SHORT).show()
            }
    }

    private fun getHistoryData() {

        if (user == null) {
            Toast.makeText(context, "Could not get user profile information. Please check your connection", Toast.LENGTH_SHORT).show()
            return
        }

        recyclerViewSkeleton = binding.rViewProfile.applySkeleton(R.layout.history_cell)
        recyclerViewSkeleton.showSkeleton()

        db.collection("user_interaction")
            .whereEqualTo("user", user?.email)
            .whereEqualTo("type", "like")
            .get()
            .addOnSuccessListener {
                for (document in it) {
                    val data = document.data
                    // TODO data["mangaId"] has mangaId. what now?
                }
                recyclerViewSkeleton.showOriginal()
            }
    }

    private fun getBookmarkData() {
        if (user == null) {
            Toast.makeText(context, "Could not get user profile information. Please check your connection", Toast.LENGTH_SHORT).show()
            return
        }

        recyclerViewSkeleton = binding.rViewProfile.applySkeleton(R.layout.community_card_cell)
        recyclerViewSkeleton.showSkeleton()

        db.collection("user_interaction")
            .whereEqualTo("user", user?.email)
            .whereEqualTo("type", "bookmark")
            .get()
            .addOnSuccessListener {

                val postList = ArrayList<CommunityPost>()

                val bookmarks = it.documents.size
                var fetched = 0

                for (document in it) {
                    db.collection("community").document(document.data["docId"].toString()).get()
                        .addOnSuccessListener { post ->
                            fetched++
                            if (post != null && post.data != null) {
                                val communityPost = CommunityPost(
                                    post.reference,
                                    null,
                                    post.data!!["email"] as String,
                                    post.data!!["title"] as String,
                                    post.data!!["description"] as String,
                                    post.data!!["timestamp"] as String,
                                    post.data!!["vote"].toString().toInt()
                                )
                                postList.add(communityPost)
                            }
                            if (fetched == bookmarks) {
                                recyclerViewSkeleton.showOriginal()
                                binding.rViewProfile.adapter = ReducedCommunityPostAdapter(postList)
                            }
                        }
                }

            }
    }

    private fun getPostsData() {
        if (user == null) {
            Toast.makeText(context, "Could not get user profile information. Please check your connection", Toast.LENGTH_SHORT).show()
            return
        }

        recyclerViewSkeleton = binding.rViewProfile.applySkeleton(R.layout.community_card_cell)
        recyclerViewSkeleton.showSkeleton()

        db.collection("community")
            .whereEqualTo("email", user?.email)
            .get()
            .addOnSuccessListener {

                val postList = ArrayList<CommunityPost>()

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
                recyclerViewSkeleton.showOriginal()
                binding.rViewProfile.adapter = ReducedCommunityPostAdapter(postList)
            }
    }
}

