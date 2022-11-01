package com.tsunderead.tsundoku.manga_detail

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.tsunderead.tsundoku.R
import com.tsunderead.tsundoku.api.MangaChapterList
import com.tsunderead.tsundoku.api.NetworkCaller
import com.tsunderead.tsundoku.chapter.Chapter
import com.tsunderead.tsundoku.chapter.ChapterAdapter
import com.tsunderead.tsundoku.community_helper.NewPost
import com.tsunderead.tsundoku.databinding.ActivityMangaDetailBinding
import com.tsunderead.tsundoku.manga_card_cell.Manga
import com.tsunderead.tsundoku.manga_reader.MangaReaderActivity
import com.tsunderead.tsundoku.offlinedb.LibraryDBHelper
import org.json.JSONObject

class MangaDetailActivity : AppCompatActivity(), NetworkCaller<JSONObject> {

    private lateinit var libraryDBHandler: LibraryDBHelper
    private lateinit var binding: ActivityMangaDetailBinding
    private lateinit var manga: Manga
    private lateinit var cover: String
    private lateinit var mangaId: String
    private lateinit var author: String
    private lateinit var title: String
    private val db = Firebase.firestore
    private var liked = false
    private val user = Firebase.auth.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityMangaDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cover = intent.getStringExtra("Cover").toString()
        author = intent.getStringExtra("Author").toString()
        title = intent.getStringExtra("Title").toString()
        val authorId = findViewById<TextView>(R.id.author)
        val coverId = binding.mangacover
        authorId.text = author

        binding.mangaDetailCollapsebar.title = title
        binding.DescToolBar.inflateMenu(R.menu.detail_top_bar)

        //finding icon
        val inLibrary = binding.DescToolBar.menu.findItem(R.id.add2Library)
        mangaId = intent.getStringExtra("MangaID").toString()

        libraryDBHandler = LibraryDBHelper(this, null)
        manga = Manga(cover, author, title, mangaId)

        if (libraryDBHandler.isPresent(manga.mangaId)) {
            //updating icon
            inLibrary.setIcon(R.drawable.ic_sharp_check_24)
        }

        modifyLikeLook()

        binding.DescToolBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.add2Library -> {
                    //bugs for some reason
                    if (libraryDBHandler.isPresent(manga.mangaId)) {
                        libraryDBHandler.deleteManga(manga.mangaId)
                        inLibrary.setIcon(R.drawable.ic_sharp_add_24)
                    } else {
                        libraryDBHandler.insertManga(manga)
                        inLibrary.setIcon(R.drawable.ic_sharp_check_24)
                    }
                    true
                }
                R.id.shareManga -> {
                    shareManga()
                    true
                }
                R.id.likeManga -> {
                    likeManga()
                    true
                }
                else -> false
            }
        }

        Glide.with(this@MangaDetailActivity).load(cover).placeholder(R.drawable.placeholder)
            .into(coverId)
        MangaChapterList(this, mangaId).execute(0)
    }


    @SuppressLint("Range")
    override fun onCallSuccess(result: JSONObject?) {
//        Log.i("MangaDetailActivity", result.toString())
        val recyclerView = findViewById<RecyclerView>(R.id.chapterRecyclerView)
        val layoutManager = LinearLayoutManager(this@MangaDetailActivity)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        val chapters = ArrayList<Chapter>()
        for (key in result!!.keys()) {
            if (key.toIntOrNull() != null) {
                val chapter = Chapter(
                    result.getJSONObject(key).getInt("chapterNo"),
                    result.getJSONObject(key).getString("chapterId")
                )
                chapters.add(chapter)
            }
        }
        val res = result.getJSONArray("tags")
        for (i in 0 until res.length()) {
            Log.i("J son", res.getString(i).toString())
            val chip = Chip(this@MangaDetailActivity)
            chip.text = res.getString(i)
            binding.mangaIdChipgroup.addView(chip)
        }
        val details = result.getString("description")
        Log.i("DEEETS", details)
        binding.MangaDescription.text = details
        recyclerView.adapter = ChapterAdapter(manga, chapters)
        binding.continueReading.setOnClickListener {
            libraryDBHandler = LibraryDBHelper(this@MangaDetailActivity, null)
            if (libraryDBHandler.isPresent(manga.mangaId)) {
                val cursor = libraryDBHandler.getOneManga(manga.mangaId)
                cursor!!.moveToFirst()
                val chapterHash =
                    cursor.getString(cursor.getColumnIndex(LibraryDBHelper.COLUMN_LASTREADHASH))
                if (chapterHash != "-1") {
                    var position = -1
                    for (i in 0 until chapters.size) {
                        if (chapters[i].chapterHash == chapterHash) {
                            position = i
                            break
                        }
                    }
                    val chapterList = arrayListOf<String>()
                    val chapterNumlist = arrayListOf<String>()
                    for (item in chapters) {
                        chapterList.add(item.chapterHash)
                        chapterNumlist.add(item.chapterNumber.toString())
                    }
                    if (position != -1) {
                        val intent =
                            Intent(this@MangaDetailActivity, MangaReaderActivity::class.java)
                        intent.putExtra("MangaId", mangaId)
                        intent.putExtra("chapterList", chapterList)
                        intent.putExtra("position", position)
                        intent.putExtra("ChapterId", chapterHash)
                        intent.putExtra("ChapterNum", chapters[position].chapterNumber.toString())
                        intent.putExtra("chapterNumlist", chapterNumlist)
                        startActivity(intent)
                    } else {
                        Log.i("THIS IS A PROBLEM", "WELP")
                    }
                }
            }
        }
    }

    private fun modifyLikeLook() {
        val likeButton = binding.DescToolBar.menu.findItem(R.id.likeManga)

        if (user != null) {
            db.collection("user_interaction")
                .whereEqualTo("user", user.email)
                .whereEqualTo("type", "like")
                .whereEqualTo("mangaId", mangaId)
                .get()
                .addOnSuccessListener {
                    liked = if (!it.isEmpty) {
                        likeButton.setIcon(R.drawable.ic_baseline_favorite_24)
                        true
                    } else {
                        likeButton.setIcon(R.drawable.ic_baseline_favorite_border_24)
                        false
                    }
                }
        }
    }

    private fun shareManga() {
        val intent = Intent(this, NewPost::class.java)
        intent.putExtra("title", title)
        intent.putExtra("author", author)
        startActivity(intent)
    }

    private fun likeManga() {
        if (user == null) {
            Toast.makeText(
                baseContext,
                "You must sign in to add manga to favorites",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        val likeButton = binding.DescToolBar.menu.findItem(R.id.likeManga)
        liked = if (liked) {
            db.collection("user_interaction")
                .whereEqualTo("user", user.email)
                .whereEqualTo("type", "like")
                .whereEqualTo("mangaId", mangaId)
                .get()
                .addOnSuccessListener {
                    for (document in it)
                        db.collection("user_interaction").document(document.id).delete()
                }
            likeButton.setIcon(R.drawable.ic_baseline_favorite_border_24)
            false
        } else {
            val interaction = mapOf(
                "user" to user.email,
                "type" to "like",
                "mangaId" to mangaId
            )
            db.collection("user_interaction").add(interaction)
                .addOnFailureListener {
                    Toast.makeText(baseContext, "Could not add to favorites", Toast.LENGTH_SHORT)
                        .show()
                    likeButton.setIcon(R.drawable.ic_baseline_favorite_border_24)
                }
            likeButton.setIcon(R.drawable.ic_baseline_favorite_24)
            true
        }
    }
}