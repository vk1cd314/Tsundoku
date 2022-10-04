package com.tsunderead.tsundoku

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tsunderead.tsundoku.api.MangaChapter
import com.tsunderead.tsundoku.api.NetworkCaller
import org.json.JSONObject

class MangaReaderActivity : AppCompatActivity(), NetworkCaller<JSONObject> {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.manga_reader)
        val chapterId = intent.getStringExtra("ChapterId")
        chapterId?.let { MangaChapter(this, it) }?.execute()
    }

    override fun onCallSuccess(result: JSONObject?) {
//        TODO("Not yet implemented")
        Log.i("ok1", result.toString())
        val chapterPages = ArrayList<ChapterPage>()
        for (key in result!!.keys()) {
            val chapterPage = ChapterPage(key.toInt(), result.getString(key))
            chapterPages.add(chapterPage)
        }
        val recyclerView = findViewById<RecyclerView>(R.id.mangaReaderRecyclerView)
        val layoutManager = LinearLayoutManager(this@MangaReaderActivity)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = ChapterPageAdapter(chapterPages)
    }
}