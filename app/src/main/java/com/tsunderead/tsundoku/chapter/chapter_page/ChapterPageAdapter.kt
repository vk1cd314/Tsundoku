package com.tsunderead.tsundoku.chapter.chapter_page

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ortiz.touchview.TouchImageView
import com.tsunderead.tsundoku.R
import com.tsunderead.tsundoku.databinding.ChapterImageViewBinding
import com.tsunderead.tsundoku.databinding.MangaReaderBinding

class ChapterPageAdapter (private val chapterPages: ArrayList<ChapterPage>)
    : RecyclerView.Adapter<ChapterPageViewHolder>() {

    override fun getItemCount(): Int {
        return chapterPages.size
    }

    override fun onBindViewHolder(holder: ChapterPageViewHolder, position: Int) {
        holder.bindChapter(chapterPages[position], holder)
//        holder.itemView.setOnClickListener {
//            Log.i("ChapterPageAdapter", "Clicked item at $position")
//            fabNextChapter.hide()
//            fabPreviousChapter.hide()
//            fabGoBack.hide()
//        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChapterPageViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = ChapterImageViewBinding.inflate(from, parent, false)
//        fabNextChapter =  mangaReaderBinding.nextChapter
//        fabPreviousChapter =  mangaReaderBinding.previousChapter
//        fabGoBack =  mangaReaderBinding.goBack

//        fabNextChapter = mangaReaderBinding.
//        fabPreviousChapter = parent.findViewById(R.id.previous_chapter)
//        fabGoBack = parent.findViewById(R.id.go_back)

//        binding.chapterPageImageView.apply {
//            setOnTouchListener { view, event ->
//                var result = true
//                if (event.pointerCount >= 2 || view.canScrollHorizontally(1) && canScrollHorizontally(-1)) {
//                    result = when (event.action) {
//                        MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
//                            parent.requestDisallowInterceptTouchEvent(true)
//                            false
//                        }
//                        MotionEvent.ACTION_UP -> {
//                            parent.requestDisallowInterceptTouchEvent(false)
//                            true
//                        }
//                        else -> true
//                    }
//                }
//                result
//            }
//        }
        return ChapterPageViewHolder(binding)
    }
}