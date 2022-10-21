package com.tsunderead.tsundoku.manga_reader

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tsunderead.tsundoku.R
import com.tsunderead.tsundoku.api.MangaChapter
import com.tsunderead.tsundoku.api.NetworkCaller
import com.tsunderead.tsundoku.chapter.chapter_page.ChapterPage
import com.tsunderead.tsundoku.chapter.chapter_page.ChapterPageAdapter
import org.json.JSONObject

class MangaReaderActivity : AppCompatActivity(), NetworkCaller<JSONObject> {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.manga_reader)
        val chapterId = intent.getStringExtra("ChapterId")
        chapterId?.let { MangaChapter(this, it) }?.execute()
    }

    override fun onCallSuccess(result: JSONObject?) {
        Log.i("MangaReaderActivity", result.toString())
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