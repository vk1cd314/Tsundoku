package com.tsunderead.tsundoku.main

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenCreated
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.android.material.textfield.TextInputEditText
import com.roacult.backdrop.BackdropLayout
import com.tsunderead.tsundoku.ConstData
import com.tsunderead.tsundoku.R
import com.tsunderead.tsundoku.api.MangaWithCover
import com.tsunderead.tsundoku.api.NetworkCaller
import com.tsunderead.tsundoku.databinding.FragmentSearchBinding
import com.tsunderead.tsundoku.manga_card_cell.CardCellAdapter
import com.tsunderead.tsundoku.manga_card_cell.Manga
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.json.JSONObject

class Search : Fragment(), NetworkCaller<JSONObject> {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var view1: View
    private lateinit var chipGroupGenre: ChipGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
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
                result.getJSONObject(i).getString("id"))
            mangaList.add(manga1)
        }

        val adapter = CardCellAdapter(mangaList)
        binding.includedFront.exploreRecylcerView.adapter = adapter
        viewLifecycleOwner.lifecycleScope.launch {
//            initChipGroupGenre()
        }
    }

    override fun onCallFail() {
        Log.i("Search", "Indeed failed")
    }

    private fun initRecyclerView () {
        val recyclerView = binding.includedFront.exploreRecylcerView

        recyclerView.layoutManager = StaggeredGridLayoutManager( 3, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.setHasFixedSize(true)
    }

    private fun initSearchButton () {
        val mangaSearchBox = binding.includedBack.mangaSearchBox

        binding.includedBack.mangaSearchButton.setOnClickListener {
            binding.includedFront.searchProgressIndicator.isIndeterminate = true
            val filterMap = HashMap<String, Array<String>>()
            if (!mangaSearchBox.text.isNullOrEmpty()) filterMap["title"] = arrayOf(mangaSearchBox.text.toString())
            val checkedChipIds = chipGroupGenre.checkedChipIds
            val checkedChipList = Array(checkedChipIds.size){""}
            for (i in 0 until checkedChipIds.size)
                checkedChipList[i] = chipGroupGenre.findViewById<Chip>(checkedChipIds[i]).text as String
            filterMap["includedTags%5B%5D"] = checkedChipList // %5B%5D = []; this is how you pass arrays
            for (key in filterMap.keys) {
                val arr = filterMap[key]
                if (arr != null) {
                    for (a in arr) Log.i(key, a)
                }
            }

            binding.mangaSearchBackdrop.close()
            binding.includedFront.exploreRecylcerView.adapter = CardCellAdapter(ArrayList())
            MangaWithCover(this, filterMap).execute(0)
        }
    }

    private fun initToggleButtons () {

        val includedBack = binding.includedBack

        includedBack.tagToggleButtons.addOnButtonCheckedListener{ toggleButton, checkedId, isChecked ->
            var s: Array<String> = arrayOf()
            when (checkedId) {
                includedBack.tagToggleFormat.id -> s = ConstData().tagListGrouped["format"]!!
                includedBack.tagToggleGenre.id -> s = ConstData().tagListGrouped["genre"]!!
                includedBack.tagToggleTheme.id -> s = ConstData().tagListGrouped["theme"]!!
            }
            chipGroupGenre.removeAllViews()
            for(str in s) {
                val newChip = Chip(context)
                newChip.text = str
                newChip.isClickable = true
                newChip.isCheckable = true
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
                chipGroupGenre.addView(newChip)
            }

        }
    }

    private fun initChipGroupGenre () {
        chipGroupGenre = binding.includedBack.chipGroupGenre
//
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