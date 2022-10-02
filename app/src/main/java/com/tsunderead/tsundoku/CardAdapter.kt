package com.tsunderead.tsundoku

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
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
        val thing = holder.itemView.findViewById<CardView>(R.id.cardview).findViewById<ImageView>(R.id.mangacover).setOnClickListener {
//        holder.itemView.findViewById<CardView>(R.id.cardview).setOnClickListener {
            val intent = Intent(it.context, MangaDetailActivity::class.java)
            intent.putExtra("Cover", mangas[position].cover)
            intent.putExtra("Author", mangas[position].author)
            intent.putExtra("Title", mangas[position].title)
            it.context.startActivity(intent)
//            (it.context as Activity).finish()
//            ^ how to finish if I want to
            println("Hello?")
        }
    }

    override fun getItemCount(): Int {
        return mangas.size
    }
    class NViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cover : ImageView = itemView.findViewById(R.id.mangacover)
        val title : TextView = itemView.findViewById(R.id.title)
    }

}