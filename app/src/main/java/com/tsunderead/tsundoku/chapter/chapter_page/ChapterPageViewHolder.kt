package com.tsunderead.tsundoku.chapter.chapter_page
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.util.Log
import android.view.MotionEvent
import androidx.core.view.ViewCompat.canScrollHorizontally
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.tsunderead.tsundoku.R
import com.tsunderead.tsundoku.databinding.ChapterImageViewBinding
import kotlinx.coroutines.*

class ChapterPageViewHolder(private val chapterPageBinding: ChapterImageViewBinding)
    : RecyclerView.ViewHolder(chapterPageBinding.root){

    @SuppressLint("ClickableViewAccessibility")
    fun bindChapter(chapterPage: ChapterPage, chapterPageViewHolder: ChapterPageViewHolder) {
        val imgUrl = chapterPage.chapterPageId
        val bitmap = Glide.with(chapterPageViewHolder.itemView.context).load(imgUrl).placeholder(R.drawable.placeholder).into(chapterPageBinding.chapterPageImageView)
        bitmap.getSize { width, height ->
            Log.i("BitMap Info", "$width and $height")
            Log.i("Image Info", chapterPageBinding.chapterPageImageView.layoutParams.width.toString() + " and " + chapterPageBinding.chapterPageImageView.layoutParams.height.toString())
            chapterPageBinding.chapterPageImageView.layoutParams.width = width
            chapterPageBinding.chapterPageImageView.layoutParams.height = height
        }
    }
}
