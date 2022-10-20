package com.tsunderead.tsundoku.main

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil.setContentView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tsunderead.tsundoku.R
import com.tsunderead.tsundoku.databinding.FragmentLibraryBinding
import com.tsunderead.tsundoku.manga_card_cell.CardCellAdapter
import com.tsunderead.tsundoku.manga_card_cell.Manga
import com.tsunderead.tsundoku.offlinedb.LibraryDBHelper

class Library : Fragment() {
    private var fragmentLibraryBinding: FragmentLibraryBinding? = null

    private lateinit var adapter: CardCellAdapter
    private lateinit var recyclerView : RecyclerView

    private lateinit var mangaList : ArrayList<Manga>
    private lateinit var covers : Array<Int>

    private lateinit var libraryDBHandler : LibraryDBHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentLibraryBinding.inflate(inflater, container, false)
        fragmentLibraryBinding = binding
        return binding.root
    }

    override fun onDestroyView() {
        fragmentLibraryBinding = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataInit()
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerView = fragmentLibraryBinding?.libraryRecylerView ?: recyclerView
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        adapter = CardCellAdapter(mangaList)
        recyclerView.adapter = adapter

        fragmentLibraryBinding?.libraryToolbar?.inflateMenu(R.menu.library_toolbar_menu)
        fragmentLibraryBinding?.libraryToolbar?.title = "Library"
        fragmentLibraryBinding?.libraryToolbar?.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.library_search -> {
                    true
                }
                else -> false
            }
        }
    }

    @SuppressLint("Range", "UseRequireInsteadOfGet")
    private fun dataInit() {
        mangaList = arrayListOf<Manga>()

        libraryDBHandler = this@Library.context?.let { LibraryDBHelper(it, null) }!!
        val cursor = libraryDBHandler.getAllManga()
        cursor.moveToFirst()

        while (!cursor.isAfterLast) {
            val manga = Manga("1", "2", "3", "4")
            manga.mangaId = cursor.getString(cursor.getColumnIndex(LibraryDBHelper.COLUMN_MANGAID))
            manga.author = cursor.getString(cursor.getColumnIndex(LibraryDBHelper.COLUMN_AUTHOR))
            manga.cover = cursor.getString(cursor.getColumnIndex(LibraryDBHelper.COLUMN_COVER))
            manga.title = cursor.getString(cursor.getColumnIndex(LibraryDBHelper.COLUMN_TITLE))
            mangaList.add(manga)

            cursor.moveToNext()
        }
    }
}