package com.tsunderead.tsundoku.chapter.chapter_page

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.recyclerview.widget.RecyclerView
import com.ortiz.touchview.TouchImageView
import com.tsunderead.tsundoku.R
import com.tsunderead.tsundoku.databinding.ChapterImageViewBinding

class ChapterPageAdapter (private val chapterPages: ArrayList<ChapterPage>)
    : RecyclerView.Adapter<ChapterPageViewHolder>() {
    override fun getItemCount(): Int {
        return chapterPages.size
    }

    override fun onBindViewHolder(holder: ChapterPageViewHolder, position: Int) {
        holder.bindChapter(chapterPages[position], holder)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChapterPageViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = ChapterImageViewBinding.inflate(from, parent, false)
        binding.chapterPageImageView.apply {
            setOnTouchListener { view, event ->
                var result = true
                if (event.pointerCount >= 2 || view.canScrollHorizontally(1) && canScrollHorizontally(-1)) {
                    result = when (event.action) {
                        MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                            parent.requestDisallowInterceptTouchEvent(true)
                            false
                        }
                        MotionEvent.ACTION_UP -> {
                            parent.requestDisallowInterceptTouchEvent(false)
                            true
                        }
                        else -> true
                    }
                }
                result
            }
        }
        return ChapterPageViewHolder(binding)
    }
}