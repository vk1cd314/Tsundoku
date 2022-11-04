package com.tsunderead.tsundoku.main

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.tsunderead.tsundoku.ConstData
import com.tsunderead.tsundoku.R
import com.tsunderead.tsundoku.api.MangaWithCover
import com.tsunderead.tsundoku.api.NetworkCaller
import com.tsunderead.tsundoku.databinding.FragmentSearchBinding
import com.tsunderead.tsundoku.manga_card_cell.CardCellAdapter
import com.tsunderead.tsundoku.manga_card_cell.Manga
import org.json.JSONObject

class Search : Fragment(), NetworkCaller<JSONObject> {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var view1: View
    private lateinit var chipGroupGenre: ChipGroup
    private var chipFilters: MutableSet<String> = mutableSetOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        view1 = binding.root
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initSearchButton()
        initRecyclerView()
        initChipGroupGenre()
        initToggleButtons()

        val callApi = MangaWithCover(this)
        callApi.execute(0)
    }

    override fun onCallSuccess(result: JSONObject?) {
        binding.includedFront.searchProgressIndicator.isIndeterminate = false
        val mangaList = ArrayList<Manga>()
        for (i in result!!.keys()) {
            val manga1 = Manga(
                result.getJSONObject(i).getString("cover_art"),
                result.getJSONObject(i).getString("author"),
                result.getJSONObject(i).getString("name"),
                result.getJSONObject(i).getString("id")
            )
            mangaList.add(manga1)
        }

        val adapter = CardCellAdapter(mangaList)
        binding.includedFront.exploreRecylcerView.adapter = adapter
    }

    override fun onCallFail() {
        Log.i("Search", "Indeed failed")
    }

    private fun initRecyclerView() {
        val recyclerView = binding.includedFront.exploreRecylcerView

        recyclerView.layoutManager =
            StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.setHasFixedSize(true)
    }

    private fun initSearchButton() {
        binding.searchSearchView.queryHint = "Search for Manga"
        binding.searchSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val filterMap = HashMap<String, ArrayList<String>>()
//                val searchBox = binding.searchSearchView
                if (query != null) {
                    Log.i("Searching", query)
                }
                filterMap["title"] = query?.let { arrayListOf(it) }!!
                val checkedChipList = Array(chipFilters.size) { "" }
                var i = 0
                chipFilters.forEach {
                    checkedChipList[i++] = it
                }
                filterMap["includedTags%5B%5D"] = checkedChipList.toCollection(ArrayList())
                for (key in filterMap.keys) {
                    val arr = filterMap[key]
                    if (arr != null) {
                        for (a in arr) Log.i(key, a)
                    }
                }
                binding.mangaSearchBackdrop.close()
                binding.includedFront.searchProgressIndicator.isIndeterminate = true
                binding.includedFront.exploreRecylcerView.adapter = CardCellAdapter(ArrayList())
                Log.i("Chips", "Logging Chips")
                chipFilters.forEach {
                    Log.i("Chips", it)
                }
                MangaWithCover(this@Search, filterMap).execute(0)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    if (newText.isEmpty()) {
                        val filterMap = HashMap<String, ArrayList<String>>()
//                        val searchBox = binding.searchSearchView
                        Log.i("Searching", newText)
                        filterMap["title"] = arrayListOf(newText)
                        val checkedChipList = Array(chipFilters.size) { "" }
                        var i = 0
                        chipFilters.forEach {
                            checkedChipList[i++] = it
                        }
                        filterMap["includedTags%5B%5D"] = checkedChipList.toCollection(ArrayList())
                        for (key in filterMap.keys) {
                            val arr = filterMap[key]
                            if (arr != null) {
                                for (a in arr) Log.i(key, a)
                            }
                        }
                        binding.mangaSearchBackdrop.close()
                        binding.includedFront.searchProgressIndicator.isIndeterminate = true
                        binding.includedFront.exploreRecylcerView.adapter =
                            CardCellAdapter(ArrayList())
                        Log.i("Chips", "Logging Chips")
                        chipFilters.forEach {
                            Log.i("Chips", it)
                        }
                        MangaWithCover(this@Search, filterMap).execute(0)
                    }
                }
                return false
            }
        })
    }

    private fun initToggleButtons() {
        val includedBack = binding.includedBack

        includedBack.tagToggleFormat.setOnClickListener {
            chipGroupGenre.removeAllViews()
            val s = ConstData().tagListGrouped["format"]!!
            for (str in s) {
                val newChip = Chip(context)
                newChip.text = str
                newChip.isClickable = true
                newChip.isCheckable = true
                if (chipFilters.contains(str)) {
                    newChip.isChecked = true
                }
                newChip.chipBackgroundColor = ColorStateList(
                    arrayOf(
                        intArrayOf(android.R.attr.state_checked),
                        intArrayOf(-android.R.attr.state_checked)
                    ),
                    intArrayOf(
                        R.style.AppTheme,
                        Color.parseColor("#EBEBEB")
                    )
                )

                newChip.setOnClickListener {
                    if (chipFilters.contains(str)) {
                        chipFilters.remove(str)
                    } else {
                        chipFilters.add(str)
                    }
                }
                chipGroupGenre.addView(newChip)
            }
        }
        includedBack.tagToggleGenre.setOnClickListener {
            chipGroupGenre.removeAllViews()
            val s = ConstData().tagListGrouped["genre"]!!
            for (str in s) {
                val newChip = Chip(context)
                newChip.text = str
                newChip.isClickable = true
                newChip.isCheckable = true
                if (chipFilters.contains(str)) {
                    newChip.isChecked = true
                }
                newChip.chipBackgroundColor = ColorStateList(
                    arrayOf(
                        intArrayOf(android.R.attr.state_checked),
                        intArrayOf(-android.R.attr.state_checked)
                    ),
                    intArrayOf(
                        R.style.AppTheme,
                        Color.parseColor("#EBEBEB")
                    )
                )

                newChip.setOnClickListener {
                    if (chipFilters.contains(str)) {
                        chipFilters.remove(str)
                    } else {
                        chipFilters.add(str)
                    }
                }
                chipGroupGenre.addView(newChip)
            }
        }
        includedBack.tagToggleTheme.setOnClickListener {
            chipGroupGenre.removeAllViews()
            val s = ConstData().tagListGrouped["theme"]!!
            for (str in s) {
                val newChip = Chip(context)
                newChip.text = str
                newChip.isClickable = true
                newChip.isCheckable = true
                if (chipFilters.contains(str)) {
                    newChip.isChecked = true
                }
                newChip.chipBackgroundColor = ColorStateList(
                    arrayOf(
                        intArrayOf(android.R.attr.state_checked),
                        intArrayOf(-android.R.attr.state_checked)
                    ),
                    intArrayOf(
                        R.style.AppTheme,
                        Color.parseColor("#EBEBEB")
                    )
                )

                newChip.setOnClickListener {
                    if (chipFilters.contains(str)) {
                        chipFilters.remove(str)
                    } else {
                        chipFilters.add(str)
                    }
                }
                chipGroupGenre.addView(newChip)
            }
        }
//        includedBack.tagToggleButtons.addOnButtonCheckedListener{ toggleButton, checkedId, isChecked ->
//            viewLifecycleOwner.lifecycleScope.launch {
//                var s: Array<String> = arrayOf()
//                Log.i("Removing", "Removing")
//                //            if (!isChecked) {
//                //                chipGroupGenre.removeAllViews()
//                //                Log.i("Removed", "Removed")
//                //                return@addOnButtonCheckedListener
//                //            }
//                chipGroupGenre.removeAllViews()
//                Log.i("Removed", "Removed")
//                when (checkedId) {
//                    includedBack.tagToggleFormat.id -> s = ConstData().tagListGrouped["format"]!!
//                    includedBack.tagToggleGenre.id -> s = ConstData().tagListGrouped["genre"]!!
//                    includedBack.tagToggleTheme.id -> s = ConstData().tagListGrouped["theme"]!!
//                }
//                Log.i("HMMMMM", s.toString())
//                for (str in s) {
//                    val newChip = Chip(context)
//                    newChip.text = str
//                    newChip.isClickable = true
//                    newChip.isCheckable = true
//                    newChip.chipBackgroundColor = ColorStateList(
//                        arrayOf(
//                            intArrayOf(android.R.attr.state_checked),
//                            intArrayOf(-android.R.attr.state_checked)
//                        ),
//                        intArrayOf(
//                            R.style.AppTheme,
//                            Color.parseColor("#EBEBEB")
//                        )
//                    )
//                    chipGroupGenre.addView(newChip)
//                }
//            }
//        }
    }

    private fun initChipGroupGenre() {
        chipGroupGenre = binding.includedBack.chipGroupGenre
//        val s = ConstData().tagList
//        for(str in s) {
//            val newChip = Chip(context)
//            newChip.text = str
//            newChip.isClickable = true
//            newChip.isCheckable = true
//            newChip.chipBackgroundColor = ColorStateList(
//                arrayOf(
//                    intArrayOf(android.R.attr.state_checked),
//                    intArrayOf(-android.R.attr.state_checked)
//                ),
//                intArrayOf(
//                    R.style.AppTheme,
//                    Color.parseColor("#EBEBEB")
//                )
//            )
//            chipGroupGenre.addView(newChip)
//        }
    }
}