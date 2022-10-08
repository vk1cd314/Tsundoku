package com.tsunderead.tsundoku.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.roacult.backdrop.BackdropLayout
import com.tsunderead.tsundoku.ConstData
import com.tsunderead.tsundoku.R
import com.tsunderead.tsundoku.api.MangaWithCover
import com.tsunderead.tsundoku.api.NetworkCaller
import com.tsunderead.tsundoku.manga_card_cell.CardCellAdapter
import com.tsunderead.tsundoku.manga_card_cell.Manga
import org.json.JSONObject

// TODO: Rename parameter arguments, choose names that match
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Search : Fragment(), NetworkCaller<JSONObject> {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var backdrop: BackdropLayout
    private lateinit var chipGroupGenre: ChipGroup
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchButton: Button
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchButton = view.findViewById(R.id.mangaSearchButton)
        searchView = view.findViewById(R.id.mangaSearchView)
        backdrop = view.findViewById(R.id.mangaSearchBackdrop)
        chipGroupGenre = view.findViewById(R.id.chip_group_genre)

        recyclerView = backdrop.getFrontLayout().findViewById(R.id.exploreRecylcerView)
        if (recyclerView != null)
            recyclerView.layoutManager = GridLayoutManager(context, 3)
        recyclerView?.setHasFixedSize(true)

        val s = ConstData().tagList
        for(str in s) {
            val newChip = Chip(context)
            newChip.text = str
            newChip.isClickable = true
            newChip.isCheckable = true
            chipGroupGenre.addView(newChip)
        }

        searchButton.setOnClickListener {
            val filterMap = HashMap<String, Array<String>>()
            filterMap["title"] = arrayOf(searchButton.text as String)
            val checkedChipIds = chipGroupGenre.checkedChipIds
            val checkedChipList = Array(checkedChipIds.size){""}
            for (i in 0 until checkedChipIds.size)
                checkedChipList[i] = chipGroupGenre.findViewById<Chip>(checkedChipIds[i]).text as String
            filterMap["includedTags[]"] = checkedChipList
            Log.i(tag, filterMap.keys.toString())
            for (key in filterMap.keys) {
                val arr = filterMap[key]
                if (arr != null) {
                    for (a in arr) Log.i(key, a)
                }
            }
        }

        val callApi = MangaWithCover(this)
        callApi.execute(0)
    }

    override fun onCallSuccess(result: JSONObject?) {
        Log.i("ok", result.toString())
        val mangaList = ArrayList<Manga>()
        for (i in 0..9) {
            val manga1 = Manga(result!!.getJSONObject(i.toString()).getString("cover_art"),
                result.getJSONObject(i.toString()).getString("author"), result.getJSONObject(i.toString()).getString("name"),
                result.getJSONObject(i.toString()).getString("id"))
            mangaList.add(manga1)
        }

        val adapter = CardCellAdapter(mangaList)
        recyclerView.adapter = adapter
    }

    override fun onCallFail() {
        Log.i("ok", "indeed")
    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Search().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}