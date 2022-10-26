package com.tsunderead.tsundoku.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.MenuItem.OnMenuItemClickListener
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tsunderead.tsundoku.R
import com.tsunderead.tsundoku.databinding.FragmentLibraryBinding
import com.tsunderead.tsundoku.manga_card_cell.CardCellAdapter
import com.tsunderead.tsundoku.manga_card_cell.Manga
import com.tsunderead.tsundoku.offlinedb.LibraryDBHelper
import java.util.*
import kotlin.collections.ArrayList

class Library : Fragment() {
    private var fragmentLibraryBinding: FragmentLibraryBinding? = null

    private lateinit var adapter: CardCellAdapter
    private lateinit var recyclerView : RecyclerView

    private lateinit var mangaList : ArrayList<Manga>

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
//        (activity as AppCompatActivity?)!!.setSupportActionBar(fragmentLibraryBinding?.libraryToolbar as Toolbar?)
        fragmentLibraryBinding?.libraryToolbar?.inflateMenu(R.menu.library_toolbar_menu)
        fragmentLibraryBinding?.libraryToolbar?.title = "Library"
        fragmentLibraryBinding?.libraryToolbar?.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.library_search -> {
                    Log.i("Inside", "Lib search")
                    val searchButton = it.actionView as SearchView
                    searchButton.queryHint = "Search"
                    searchButton.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(query: String?): Boolean {
                            return false
                        }
                        override fun onQueryTextChange(newText: String?): Boolean {
                            // filter here
                            if (newText != null) {
                                filter(newText)
                            }
                            return false
                        }
                    })
                    true
                }
                else -> false
            }
        }
        setRecyclerViewAdapter()
        dataInit()
    }


    override fun onResume() {
        super.onResume()
        dataInit()
    }

    private fun filter(query: String) {
        Log.i("Filtering", query)
        val filteredList: ArrayList<Manga> = arrayListOf()
        for (item in mangaList) {
            if (item.title.lowercase().contains(query.lowercase())) {
                filteredList.add(item)
            }
        }
        adapter.changeList(filteredList)
    }

    @SuppressLint("Range", "UseRequireInsteadOfGet")
    private fun dataInit() {
        mangaList = arrayListOf<Manga>()

        libraryDBHandler = this@Library.context?.let { LibraryDBHelper(it, null) }!!
        val cursor = libraryDBHandler.getAllManga()
        cursor!!.moveToFirst()

        while (!cursor.isAfterLast) {
            val manga = Manga("1", "2", "3", "4")
            manga.mangaId = cursor.getString(cursor.getColumnIndex(LibraryDBHelper.COLUMN_MANGAID))
            manga.author = cursor.getString(cursor.getColumnIndex(LibraryDBHelper.COLUMN_AUTHOR))
            manga.cover = cursor.getString(cursor.getColumnIndex(LibraryDBHelper.COLUMN_COVER))
            manga.title = cursor.getString(cursor.getColumnIndex(LibraryDBHelper.COLUMN_TITLE))
            mangaList.add(manga)

            cursor.moveToNext()
        }
        libraryDBHandler.close()
        adapter.changeList(mangaList)
    }

    private fun setRecyclerViewAdapter() {
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerView = fragmentLibraryBinding?.libraryRecylerView ?: recyclerView
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        mangaList = arrayListOf()
        adapter = CardCellAdapter(mangaList)
        recyclerView.adapter = adapter
    }
}