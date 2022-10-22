package com.tsunderead.tsundoku.main

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.tsunderead.tsundoku.R
import com.tsunderead.tsundoku.chapter.Chapter
import com.tsunderead.tsundoku.databinding.FragmentHistoryBinding
import com.tsunderead.tsundoku.history.HistoryChapterAdapter
import com.tsunderead.tsundoku.history.MangaWithChapter
import com.tsunderead.tsundoku.manga_card_cell.Manga
import com.tsunderead.tsundoku.offlinedb.LibraryDBHelper

class History : Fragment() {
    private var fragmentHistoryBinding: FragmentHistoryBinding? = null
    private lateinit var libraryDBHandler : LibraryDBHelper
    private lateinit var historyChapterList: ArrayList<MangaWithChapter>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding = FragmentHistoryBinding.inflate(inflater, container, false)
        fragmentHistoryBinding = binding
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = fragmentHistoryBinding?.historyToolbar
        toolbar?.inflateMenu(R.menu.history_toolbar_menu)
        toolbar?.title="History"
        toolbar?.setOnMenuItemClickListener{
            //IN CASE WE NEED TO ADD NEW STUFF TO THIS MENU
            when(it.itemId){
                R.id.delete_history -> {
                    true
                }
                else -> false
            }
        }
        getHistoryData()
        val recyclerView = fragmentHistoryBinding?.historyRecyclerview!!
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = HistoryChapterAdapter(historyChapterList)
    }

    @SuppressLint("UseRequireInsteadOfGet", "Range")
    fun getHistoryData() {
        historyChapterList = ArrayList<MangaWithChapter>()
        libraryDBHandler = this@History.context?.let { LibraryDBHelper(it, null) }!!
        val cursor = libraryDBHandler.getAllManga()
        cursor!!.moveToFirst()

        while (!cursor.isAfterLast) {
            val manga = Manga("1", "2", "3", "4")
            val chapter = Chapter(1, "-1")
            manga.mangaId = cursor.getString(cursor.getColumnIndex(LibraryDBHelper.COLUMN_MANGAID))
            manga.author = cursor.getString(cursor.getColumnIndex(LibraryDBHelper.COLUMN_AUTHOR))
            manga.cover = cursor.getString(cursor.getColumnIndex(LibraryDBHelper.COLUMN_COVER))
            manga.title = cursor.getString(cursor.getColumnIndex(LibraryDBHelper.COLUMN_TITLE))
            chapter.chapterNumber = cursor.getString(cursor.getColumnIndex(LibraryDBHelper.COLUMN_LASTREAD)).toInt()
            chapter.chapterHash = cursor.getString(cursor.getColumnIndex(LibraryDBHelper.COLUMN_LASTREADHASH))
            if (chapter.chapterHash != "-1" && chapter.chapterNumber != -1) historyChapterList.add(MangaWithChapter(manga, chapter))
            cursor.moveToNext()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentHistoryBinding = null
    }
}