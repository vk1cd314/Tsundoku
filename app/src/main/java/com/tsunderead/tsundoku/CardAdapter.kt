package com.tsunderead.tsundoku

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.tsunderead.tsundoku.databinding.CardCellBinding

class CardAdapter (private val mangas: ArrayList<Manga>)
    : RecyclerView.Adapter<CardViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = CardCellBinding.inflate(from, parent, false)
        return CardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bindBook(mangas[position])
        holder.itemView.setOnClickListener {
            val intent = Intent(it.context, MangaDetailActivity::class.java)
            intent.putExtra("Cover", mangas[position].cover)
            intent.putExtra("Author", mangas[position].author)
            intent.putExtra("Title", mangas[position].title)
            it.context.startActivity(intent)
            println("Hello?")
        }
    }

    override fun getItemCount(): Int {
        return mangas.size
    }
    class NViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cover : ImageView = itemView.findViewById(R.id.cover)
        val title : TextView = itemView.findViewById(R.id.title)
    }

}