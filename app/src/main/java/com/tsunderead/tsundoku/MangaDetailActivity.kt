package com.tsunderead.tsundoku

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MangaDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manga_detail)
        val cover = intent.getIntExtra("Cover", 69)
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
        coverId.setImageResource(cover)
        val recyclerView = findViewById<RecyclerView>(R.id.chapterRecyclerView)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        val chapter = Chapter(1, "Hello", "World")
        val chapters = ArrayList<Chapter>()
        chapters.add(chapter)
        chapter.chapterNumber = 2
        chapters.add(chapter)
        recyclerView.adapter = ChapterAdapter(chapters)
    }
}