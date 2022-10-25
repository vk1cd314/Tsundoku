package com.tsunderead.tsundoku.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tsunderead.tsundoku.databinding.HistoryCellBinding

class HistoryChapterAdapter(private val historChapters: ArrayList<MangaWithChapter>)
    : RecyclerView.Adapter<HistoryChapterViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryChapterViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = HistoryCellBinding.inflate(from, parent, false)
        return HistoryChapterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryChapterViewHolder, position: Int) {
        holder.bindHistoryChapter(historChapters[position], holder)
    }

    override fun getItemCount(): Int {
        return historChapters.size
    }

}