package com.tsunderead.tsundoku.chapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.tsunderead.tsundoku.R
import com.tsunderead.tsundoku.databinding.ChapterCellBinding
import com.tsunderead.tsundoku.history.MangaWithChapter
import com.tsunderead.tsundoku.manga_card_cell.Manga
import com.tsunderead.tsundoku.manga_reader.MangaReaderActivity
import com.tsunderead.tsundoku.offlinedb.LibraryDBHelper

class ChapterAdapter(private val manga: Manga, private val chapters: ArrayList<Chapter>) :
    RecyclerView.Adapter<ChapterViewHolder>() {
    private lateinit var libraryDBHandler: LibraryDBHelper
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChapterViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = ChapterCellBinding.inflate(from, parent, false)
        return ChapterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChapterViewHolder, position: Int) {
        holder.bindChapter(chapters[position])
        holder.itemView.findViewById<CardView>(R.id.chapterCardView).findViewById<TextView>(
            R.id.chapterTextView
        ).setOnClickListener {
            libraryDBHandler = LibraryDBHelper(it.context, null)
            val mangaWithChapter = MangaWithChapter(manga, chapters[position])
            libraryDBHandler.updateManga(manga.mangaId, mangaWithChapter)
            libraryDBHandler.close()
            val intent = Intent(it.context, MangaReaderActivity::class.java)
            intent.putExtra("MangaId", manga.mangaId)
            intent.putExtra("ChapterId", chapters[position].chapterHash)
            intent.putExtra("ChapterNum", chapters[position].chapterNumber.toString())
            val chapterList = arrayListOf<String>()
            for (item in chapters) chapterList.add(item.chapterHash)
            intent.putExtra("chapterList", chapterList)
            intent.putExtra("position", position)
            val chapterNumlist = arrayListOf<String>()
            for (item in chapters) chapterNumlist.add(item.chapterNumber.toString())
            intent.putExtra("chapterNumlist", chapterNumlist)
            it.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return chapters.size
    }

}