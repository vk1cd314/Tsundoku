package com.tsunderead.tsundoku.chapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.tsunderead.tsundoku.manga_reader.MangaReaderActivity
import com.tsunderead.tsundoku.R
import com.tsunderead.tsundoku.databinding.ChapterCellBinding

class ChapterAdapter (private val chapters: ArrayList<Chapter>)
    : RecyclerView.Adapter<ChapterViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChapterViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = ChapterCellBinding.inflate(from, parent, false)
        return ChapterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChapterViewHolder, position: Int) {
        holder.bindChapter(chapters[position])
        val thing = holder.itemView.findViewById<CardView>(R.id.chapterCardView).findViewById<TextView>(
            R.id.chapterTextView
        ).setOnClickListener() {
            val intent = Intent(it.context, MangaReaderActivity::class.java)
            intent.putExtra("ChapterId", chapters[position].chapterHash)
            it.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return chapters.size
    }

}