package com.tsunderead.tsundoku.chapter.chapter_page

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.tsunderead.tsundoku.R
import com.tsunderead.tsundoku.databinding.ChapterImageViewBinding

class ChapterPageViewHolder(private val chapterPageBinding: ChapterImageViewBinding) :
    RecyclerView.ViewHolder(chapterPageBinding.root) {

    @SuppressLint("ClickableViewAccessibility")
    fun bindChapter(chapterPage: ChapterPage, chapterPageViewHolder: ChapterPageViewHolder) {
        val imgUrl = chapterPage.chapterPageId
        Glide.with(chapterPageViewHolder.itemView.context)
            .download(GlideUrl(imgUrl)).placeholder(R.drawable.placeholder)
            .into(ChapterImageView(chapterPageBinding.chapterPageImageView))
    }
}
