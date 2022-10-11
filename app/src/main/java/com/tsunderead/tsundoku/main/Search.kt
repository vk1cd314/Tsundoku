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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import kotlinx.coroutines.*
import org.json.JSONObject

// TODO: Rename parameter arguments, choose names that match
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Search : Fragment(), NetworkCaller<JSONObject> {
//    init {
//        lifecycleScope.launchWhenStarted {
//            try {
//                // Call some suspend functions.
//                doApiCall()
//            } finally {
//                // This line might execute after Lifecycle is DESTROYED.
////                if (lifecycle.state >= STARTED) {
////                    // Here, since we've checked, it is safe to run any
////                    // Fragment transactions.
////                }
//                Log.i("Init", "Did Api Call")
//            }
//        }
//    }

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var result: JSONObject
    private var mangaList : ArrayList<Manga> = ArrayList<Manga>()
    private lateinit var backdrop: BackdropLayout
    private lateinit var chipGroupGenre: ChipGroup
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchButton: Button
    private lateinit var searchText: TextInputEditText
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var searchProgressIndicator: LinearProgressIndicator

    private lateinit var binding: FragmentSearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

//    override fun onDestroy() {
//        super.onDestroy()
//        cancel()
//    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    //        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mangaList = ArrayList<Manga>()

        initBackdrop(view)
        initSearchButton(view)
        initSearchText(view)
        initChipGroupGenre(view)
        initRecyclerView(view)
        initToolbar(view)
        initSearchProgressIndicator(view)


//        val adapter = CardCellAdapter(mangaList)
//        recyclerView.adapter = adapter

//        val job = async {
//            withContext(Dispatchers.Main) {
//                doApiCall().await()
//            }
//        }
//        viewLifecycleOwner.lifecycleScope.launch {
//            doApiCall().await()
//        }
        val context = this
        CoroutineScope(Dispatchers.Main).launch {
            MangaWithCover(context).execute(0)
        }
        Log.i("onViewCreated", "In this thing")
//        recyclerView.setHasFixedSize(false)
    }

//    private suspend fun doApiCall(): Deferred<Unit> {
//        return apiCallJob
//    }

    override fun onCallSuccess(result: JSONObject?) {
        Log.i("ok", result.toString())
        searchProgressIndicator.isIndeterminate = false
        mangaList = ArrayList<Manga>()
        for (i in 0..9) {
            val manga1 = Manga(result!!.getJSONObject(i.toString()).getString("cover_art"),
                result.getJSONObject(i.toString()).getString("author"), result.getJSONObject(i.toString()).getString("name"),
                result.getJSONObject(i.toString()).getString("id"))
            mangaList.add(manga1)
        }
        val adapter = CardCellAdapter(mangaList)
        adapter.setHasStableIds(true)
        recyclerView.adapter = adapter
    }

    override fun onCallFail() {
        Log.i("ok", "indeed")
    }

    private fun initBackdrop (view: View) {
        backdrop = view.findViewById(R.id.mangaSearchBackdrop)
    }

    private fun initChipGroupGenre (view: View) {
        chipGroupGenre = view.findViewById(R.id.chip_group_genre)

        val s = ConstData().tagList
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
                    R.style.Theme_Tsundoku,
                    Color.parseColor("#EBEBEB")
                )
            )
            chipGroupGenre.addView(newChip)
        }
    }
    private fun initRecyclerView (view: View) {
        recyclerView = view.findViewById(R.id.exploreRecylcerView)

        recyclerView.layoutManager = GridLayoutManager(context, 3)
        val adapter = CardCellAdapter(mangaList)
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)
    }
    private fun initSearchButton (view: View) {
        searchButton = view.findViewById(R.id.mangaSearchButton)

        searchButton.setOnClickListener {
            searchProgressIndicator.isIndeterminate = true
            val filterMap = HashMap<String, Array<String>>()
            if (!searchText.text.isNullOrEmpty()) filterMap["title"] = arrayOf(searchText.text.toString())
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
            backdrop.close()
            recyclerView.adapter = CardCellAdapter(ArrayList())
            MangaWithCover(this, filterMap).execute(0)
        }
    }
    private fun initSearchText (view: View) {
        searchText = view.findViewById(R.id.mangaSearchBox)
    }

    private fun initToolbar(view: View) {
        toolbar = view.findViewById(R.id.toolbar)
    }

    private fun initSearchProgressIndicator(view: View) {
        searchProgressIndicator = view.findViewById(R.id.searchProgressIndicator)
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