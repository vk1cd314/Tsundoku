package com.tsunderead.tsundoku

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.tsunderead.tsundoku.databinding.CardCellBinding

class CardAdapter (private val mangas: List<Manga>)
    : RecyclerView.Adapter<CardViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = CardCellBinding.inflate(from, parent, false)
        return CardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        //holder.bindBook(mangas[position])
    }

    override fun getItemCount(): Int {
        return mangas.size
    }
    class NViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cover : ImageView = itemView.findViewById(R.id.cover)
        val title: TextView = itemView.findViewById(R.id.title)
    }

}