package com.tsunderead.tsundoku

import androidx.recyclerview.widget.RecyclerView
import com.tsunderead.tsundoku.databinding.CardCellBinding

class CardViewHolder(
    private val cardCellBinding: CardCellBinding
) : RecyclerView.ViewHolder(cardCellBinding.root){
    fun bindBook(book: Manga){
        cardCellBinding.cover.setImageResource(book.cover)
        cardCellBinding.title.text = book.title
    }
}