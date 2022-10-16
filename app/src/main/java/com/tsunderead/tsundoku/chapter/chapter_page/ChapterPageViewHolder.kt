package com.tsunderead.tsundoku.chapter.chapter_page

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tsunderead.tsundoku.databinding.ChapterImageViewBinding

class ChapterPageViewHolder(private val chapterPageBinding: ChapterImageViewBinding)
    : RecyclerView.ViewHolder(chapterPageBinding.root){

    fun bindChapter(chapterPage: ChapterPage, chapterPageViewHolder: ChapterPageViewHolder) {
        val imgUrl = chapterPage.chapterPageId
        Glide.with(chapterPageViewHolder.itemView.context).load(imgUrl).into(chapterPageBinding.chapterPageImageView)
    }
}
