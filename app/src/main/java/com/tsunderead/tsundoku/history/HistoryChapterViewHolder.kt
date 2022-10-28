package com.tsunderead.tsundoku.history

import android.annotation.SuppressLint
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tsunderead.tsundoku.R
import com.tsunderead.tsundoku.databinding.HistoryCellBinding
import com.tsunderead.tsundoku.manga_reader.MangaReaderActivity

class HistoryChapterViewHolder(private val historyCellBinding: HistoryCellBinding)
    : RecyclerView.ViewHolder(historyCellBinding.root){
    @SuppressLint("SetTextI18n")
    fun bindHistoryChapter(mangaWithChapter: MangaWithChapter, historyChapterViewHolder: HistoryChapterViewHolder) {
        val imgUrl = mangaWithChapter.manga.cover
        Glide.with(historyChapterViewHolder.itemView.context).load(imgUrl).placeholder(R.drawable.placeholder).into(historyCellBinding.mangaHistoryImageView)
        historyCellBinding.mangaNameTextView.text = mangaWithChapter.manga.title
        historyCellBinding.chapterIDTextView.text = "Chapter ${mangaWithChapter.chapter.chapterNumber}"
        historyCellBinding.resumeChapterButton.setOnClickListener {
//            val intent = Intent(it.context, MangaReaderActivity::class.java)
//            intent.putExtra("ChapterId", mangaWithChapter.chapter.chapterHash)
//            it.context.startActivity(intent)
        }
    }
}