package com.tsunderead.tsundoku

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.tsunderead.tsundoku.databinding.CardCellBinding

class CardViewHolder(private val cardCellBinding: CardCellBinding) : RecyclerView.ViewHolder(cardCellBinding.root){

    lateinit var cardViewHolder1 : CardViewHolder
    lateinit var manga: Manga

    fun bindBook(book: Manga, cardViewHolder: CardViewHolder){
        cardViewHolder1 = cardViewHolder
        manga = book

        @Suppress("DEPRECATION")
        ImageFromInternet(cardCellBinding.mangacover).execute(book.cover)
        cardCellBinding.title.text = book.title
    }
    @SuppressLint("StaticFieldLeak")
    @Suppress("DEPRECATION")
    private inner class ImageFromInternet(var imageView: ImageView) : android.os.AsyncTask<String, Void, Bitmap?>() {
        init {
            Log.e("Hello", "Working")
        }

        @Deprecated("Deprecated in Java")
        override fun doInBackground(vararg urls: String): Bitmap? {
            val imageURL = urls[0]
            var image: Bitmap? = null
            try {
                val `in` = java.net.URL(imageURL).openStream()
                image = BitmapFactory.decodeStream(`in`)
            }
            catch (e: Exception) {
                Log.e("Error Message", e.message.toString())
                e.printStackTrace()
            }
            return image
        }
        @Deprecated("Deprecated in Java", ReplaceWith("imageView.setImageBitmap(result)"))
        override fun onPostExecute(result: Bitmap?) {
            imageView.setImageBitmap(result)
            doAfter(manga, cardViewHolder1)
        }
    }
    fun doAfter(manga : Manga, cardViewHolder: CardViewHolder) {
//        cardViewHolder.itemView.findViewById<CardView>(R.id.cardview).findViewById<ImageView>(R.id.mangacover).setClickable
        cardViewHolder.itemView.findViewById<CardView>(R.id.cardview).setOnClickListener {
//        holder.itemView.findViewById<CardView>(R.id.cardview).setOnClickListener {
            val intent = Intent(it.context, MangaDetailActivity::class.java)
            intent.putExtra("Cover", manga.cover)
            intent.putExtra("Author", manga.author)
            intent.putExtra("Title", manga.title)
            intent.putExtra("MangaID", manga.mangaId)
            it.context.startActivity(intent)
//            (it.context as Activity).finish()
//            ^ how to finish if I want to
            println("Hello?")
        }
    }
}