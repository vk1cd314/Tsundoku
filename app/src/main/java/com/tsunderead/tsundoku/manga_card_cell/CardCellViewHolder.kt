package com.tsunderead.tsundoku.manga_card_cell

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tsunderead.tsundoku.manga_detail.MangaDetailActivity
import com.tsunderead.tsundoku.R
import com.tsunderead.tsundoku.databinding.CardCellBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class CardCellViewHolder(private val cardCellBinding: CardCellBinding) : RecyclerView.ViewHolder(cardCellBinding.root){
    fun bindBook(book: Manga, cardCellViewHolder: CardCellViewHolder){
        Log.d("CardCellViewHolder", book.cover)
        Glide.with(cardCellViewHolder.itemView.context).load(book.cover).placeholder(R.drawable.placeholder).into(cardCellBinding.mangacover)
        doAfter(book, cardCellViewHolder)
        cardCellBinding.title.text = book.title
    }

    private fun doAfter(manga : Manga, cardCellViewHolder: CardCellViewHolder) {
        cardCellViewHolder.itemView.findViewById<CardView>(R.id.cardview).setOnClickListener {
            val intent = Intent(it.context, MangaDetailActivity::class.java)
            intent.putExtra("Cover", manga.cover)
            intent.putExtra("Author", manga.author)
            intent.putExtra("Title", manga.title)
            intent.putExtra("MangaID", manga.mangaId)
            it.context.startActivity(intent)
        }
    }
}