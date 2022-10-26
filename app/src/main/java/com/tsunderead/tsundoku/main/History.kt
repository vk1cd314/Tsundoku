package com.tsunderead.tsundoku.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton
import com.tsunderead.tsundoku.R
import com.tsunderead.tsundoku.chapter.Chapter
import com.tsunderead.tsundoku.databinding.FragmentHistoryBinding
import com.tsunderead.tsundoku.history.HistoryChapterAdapter
import com.tsunderead.tsundoku.history.MangaWithChapter
import com.tsunderead.tsundoku.manga_card_cell.Manga
import com.tsunderead.tsundoku.offlinedb.LibraryDBHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class History : Fragment() {
    private var fragmentHistoryBinding: FragmentHistoryBinding? = null
    private lateinit var libraryDBHandler : LibraryDBHelper
    private lateinit var historyChapterList: ArrayList<MangaWithChapter>
    private lateinit var recyclerView: RecyclerView
    private lateinit var skeleton: Skeleton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding = FragmentHistoryBinding.inflate(inflater, container, false)
        fragmentHistoryBinding = binding
        return binding.root
    }
    @SuppressLint("UseRequireInsteadOfGet")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = fragmentHistoryBinding?.historyToolbar
        toolbar?.inflateMenu(R.menu.history_toolbar_menu)
        toolbar?.title="History"
        toolbar?.setOnMenuItemClickListener{
            //IN CASE WE NEED TO ADD NEW STUFF TO THIS MENU
            when(it.itemId){
                R.id.delete_history -> {
                    Log.i("Delete", "History")
                    libraryDBHandler = this@History.context?.let { it1 -> LibraryDBHelper(it1, null) }!!
                    libraryDBHandler.deleteHistory()
                    libraryDBHandler.close()
                    true
                }
                else -> false
            }
        }
        recyclerView = fragmentHistoryBinding?.historyRecyclerview!!
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        skeleton = recyclerView.applySkeleton(R.layout.history_cell)
        skeleton.showSkeleton()
        lifecycleScope.launch {
            getHistoryData()
        }
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            getHistoryData()
        }
    }

    @SuppressLint("UseRequireInsteadOfGet", "Range")
    suspend fun getHistoryData() {
        historyChapterList = ArrayList<MangaWithChapter>()
        libraryDBHandler = this@History.context?.let { LibraryDBHelper(it, null) }!!
        val cursor = libraryDBHandler.getAllMangaWithHistory()
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
            Log.i("Manga", manga.toString())
            Log.i("Chapter", chapter.toString())
            if (chapter.chapterHash != "-1" && chapter.chapterNumber != -1) historyChapterList.add(MangaWithChapter(manga, chapter))
            cursor.moveToNext()
        }
        libraryDBHandler.close()
        delay(500)
        skeleton.showOriginal()
        recyclerView.adapter = HistoryChapterAdapter(historyChapterList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentHistoryBinding = null
    }
}