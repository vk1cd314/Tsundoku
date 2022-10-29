package com.tsunderead.tsundoku.chapter.chapter_page

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tsunderead.tsundoku.databinding.ChapterImageViewBinding

class ChapterPageAdapter(private val chapterPages: ArrayList<ChapterPage>) :
    RecyclerView.Adapter<ChapterPageViewHolder>() {

    override fun getItemCount(): Int {
        return chapterPages.size
    }

    override fun onBindViewHolder(holder: ChapterPageViewHolder, position: Int) {
        holder.bindChapter(chapterPages[position], holder)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChapterPageViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = ChapterImageViewBinding.inflate(from, parent, false)
        return ChapterPageViewHolder(binding)
    }
}