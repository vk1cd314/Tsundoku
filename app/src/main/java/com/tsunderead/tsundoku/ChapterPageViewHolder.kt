package com.tsunderead.tsundoku

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.tsunderead.tsundoku.databinding.ChapterCellBinding
import com.tsunderead.tsundoku.databinding.ChapterImageViewBinding

class ChapterPageViewHolder(private val chapterPageBinding: ChapterImageViewBinding)
    : RecyclerView.ViewHolder(chapterPageBinding.root){

    fun bindChapter(chapterPage: ChapterPage, chapterPageViewHolder: ChapterPageViewHolder) {
        ImageFromInternet(chapterPageBinding.chapterPageImageView).execute(chapterPage.chapterPageId)
    }
    @SuppressLint("StaticFieldLeak")
    @Suppress("DEPRECATION")
    private inner class ImageFromInternet(var imageView: ImageView) : AsyncTask<String, Void, Bitmap?>() {
        init {
//            Toast.makeText(this, "Please wait, it may take a few minute...", Toast.LENGTH_SHORT).show()
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
        }
    }
}
