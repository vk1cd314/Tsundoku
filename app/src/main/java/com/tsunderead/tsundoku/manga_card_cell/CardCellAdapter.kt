package com.tsunderead.tsundoku.manga_card_cell

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tsunderead.tsundoku.databinding.CardCellBinding

class CardCellAdapter (private val mangas: ArrayList<Manga>): RecyclerView.Adapter<CardCellViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardCellViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = CardCellBinding.inflate(from, parent, false)
        return CardCellViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardCellViewHolder, position: Int) {
        holder.bindBook(mangas[position], holder)
    }

    override fun getItemCount(): Int {
        return mangas.size
    }
}