package com.tsunderead.tsundoku.manga_detail

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tsunderead.tsundoku.R
import com.tsunderead.tsundoku.api.MangaChapterList
import com.tsunderead.tsundoku.api.NetworkCaller
import com.tsunderead.tsundoku.chapter.Chapter
import com.tsunderead.tsundoku.chapter.ChapterAdapter
import com.tsunderead.tsundoku.manga_card_cell.Manga
import com.tsunderead.tsundoku.offlinedb.LibraryDBHelper
import org.json.JSONObject

class MangaDetailActivity : AppCompatActivity(), NetworkCaller<JSONObject>{
    private lateinit var libraryDBHandler : LibraryDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manga_detail)
        val cover = intent.getStringExtra("Cover")
        val author = intent.getStringExtra("Author")
        val title = intent.getStringExtra("Title")
        println("Cover is $cover")
        println("Title is $title")
        println("Author is $author")
        val authorId = findViewById<TextView>(R.id.author)
        val titleId = findViewById<TextView>(R.id.title)
        val coverId = findViewById<ImageView>(R.id.mangacover)
        authorId.text = author
        titleId.text = title
        val mangaId = intent.getStringExtra("MangaID")
        if (mangaId != null) {
            Log.d("mangaID", mangaId)
        }
        libraryDBHandler = LibraryDBHelper(this, null)
        findViewById<ImageButton>(R.id.addToLibrary).setOnClickListener {
            val manga = Manga(cover!!, author!!, title!!, mangaId!!)
            libraryDBHandler.insertManga(manga)
        }
        Glide.with(this@MangaDetailActivity).load(cover).placeholder(R.drawable.placeholder).into(coverId)
        mangaId?.let { MangaChapterList(this, it) }?.execute(0)
    }

    override fun onCallSuccess(result: JSONObject?) {
        Log.i("MangaDetailActivity", result.toString())
        val recyclerView = findViewById<RecyclerView>(R.id.chapterRecyclerView)
        val layoutManager = LinearLayoutManager(this@MangaDetailActivity)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        val chapters = ArrayList<Chapter>()
        for (key in result!!.keys()) {
            val chapter = Chapter(result.getJSONObject(key).getInt("chapterNo"), result.getJSONObject(key).getString("chapterId"))
            chapters.add(chapter)
        }
        recyclerView.adapter = ChapterAdapter(chapters)
    }
}