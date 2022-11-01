package com.tsunderead.tsundoku.history

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tsunderead.tsundoku.R
import com.tsunderead.tsundoku.api.MangaChapterList
import com.tsunderead.tsundoku.api.NetworkCaller
import com.tsunderead.tsundoku.chapter.Chapter
import com.tsunderead.tsundoku.databinding.HistoryCellBinding
import com.tsunderead.tsundoku.manga_reader.MangaReaderActivity
import org.json.JSONObject

class HistoryChapterViewHolder(private val historyCellBinding: HistoryCellBinding) :
    RecyclerView.ViewHolder(historyCellBinding.root), NetworkCaller<JSONObject> {

    private lateinit var mangaChapInfo: MangaWithChapter

    @SuppressLint("SetTextI18n")
    fun bindHistoryChapter(
        mangaWithChapter: MangaWithChapter,
        historyChapterViewHolder: HistoryChapterViewHolder
    ) {
        val imgUrl = mangaWithChapter.manga.cover


        Glide.with(historyChapterViewHolder.itemView.context).load(imgUrl)
            .placeholder(R.drawable.placeholder).into(historyCellBinding.mangaHistoryImageView)

        historyCellBinding.mangaNameTextView.text = mangaWithChapter.manga.title
        historyCellBinding.chapterIDTextView.text =
            "Chapter ${mangaWithChapter.chapter.chapterNumber}"
        mangaChapInfo = mangaWithChapter
        MangaChapterList(this, mangaWithChapter.manga.mangaId).execute(0)
        historyCellBinding.resumeChapterButton.setOnClickListener {
            Toast.makeText(it.context, "Please Wait", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCallSuccess(result: JSONObject?) {
//        Log.i("HistoryCell", result.toString())
        val chapters = ArrayList<Chapter>()
        for (key in result!!.keys()) {
            if (key.toIntOrNull() != null) {
                val chapter = Chapter(
                    result.getJSONObject(key).getInt("chapterNo"),
                    result.getJSONObject(key).getString("chapterId")
                )
                chapters.add(chapter)
            }
        }
        var position = -1
        for (i in 0 until chapters.size) {
            if (chapters[i].chapterHash == mangaChapInfo.chapter.chapterHash) {
                position = i
                break
            }
        }
        val chapterList = arrayListOf<String>()
        val chapterNumlist = arrayListOf<String>()
        for (item in chapters) {
            chapterList.add(item.chapterHash)
            chapterNumlist.add(item.chapterNumber.toString())
        }

        if (position != -1) {
            historyCellBinding.resumeChapterButton.setOnClickListener {
                val intent = Intent(it.context, MangaReaderActivity::class.java)
                intent.putExtra("MangaId", mangaChapInfo.manga.mangaId)
                intent.putExtra("ChapterId", mangaChapInfo.chapter.chapterHash)
                intent.putExtra("ChapterNum", mangaChapInfo.chapter.chapterNumber.toString())
                intent.putExtra("chapterList", chapterList)
                intent.putExtra("position", position)
                intent.putExtra("chapterNumlist", chapterNumlist)
                it.context.startActivity(intent)
            }
        } else {
            Log.e("Well", "If this ain't a problem HMMMMMMMMMMMMMM")
        }
    }
}