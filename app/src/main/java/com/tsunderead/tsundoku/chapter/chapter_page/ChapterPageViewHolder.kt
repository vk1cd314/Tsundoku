package com.tsunderead.tsundoku.chapter.chapter_page
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.tsunderead.tsundoku.R
import com.tsunderead.tsundoku.databinding.ChapterImageViewBinding

class ChapterPageViewHolder(private val chapterPageBinding: ChapterImageViewBinding)
    : RecyclerView.ViewHolder(chapterPageBinding.root){

    fun bindChapter(chapterPage: ChapterPage, chapterPageViewHolder: ChapterPageViewHolder) {
        val imgUrl = chapterPage.chapterPageId
        Glide.with(chapterPageViewHolder.itemView.context).load(imgUrl).placeholder(R.drawable.placeholder).into(chapterPageBinding.chapterPageImageView)
    }
}
