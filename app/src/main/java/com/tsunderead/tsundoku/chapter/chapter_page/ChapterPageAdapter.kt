package com.tsunderead.tsundoku.chapter.chapter_page

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tsunderead.tsundoku.chapter.chapter_page.ChapterPage
import com.tsunderead.tsundoku.chapter.chapter_page.ChapterPageViewHolder
import com.tsunderead.tsundoku.databinding.ChapterImageViewBinding

class ChapterPageAdapter (private val chapterPages: ArrayList<ChapterPage>)
    : RecyclerView.Adapter<ChapterPageViewHolder>() {
    override fun getItemCount(): Int {
//        TODO("Not yet implemented")
        return chapterPages.size
    }

    override fun onBindViewHolder(holder: ChapterPageViewHolder, position: Int) {
//        TODO("Not yet implemented")
        holder.bindChapter(chapterPages[position], holder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChapterPageViewHolder {
//        TODO("Not yet implemented")
        val from = LayoutInflater.from(parent.context)
        val binding = ChapterImageViewBinding.inflate(from, parent, false)
        return ChapterPageViewHolder(binding)
    }


}