package com.tsunderead.tsundoku.chapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.tsunderead.tsundoku.chapter.Chapter
import com.tsunderead.tsundoku.databinding.ChapterCellBinding

class ChapterViewHolder(
    private val chapterCellBinding: ChapterCellBinding
) : RecyclerView.ViewHolder(chapterCellBinding.root) {
    @SuppressLint("SetTextI18n")
    fun bindChapter(chapter: Chapter) {
//        cardCellBinding.
//        cardCellBinding.chapterNumber =
        chapterCellBinding.chapterTextView.text = "Chapter ${chapter.chapterNumber}"
    }
}