package com.tsunderead.tsundoku

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tsunderead.tsundoku.api.ApiCall
import com.tsunderead.tsundoku.api.MangaWithCover
import com.tsunderead.tsundoku.api.NetworkCaller
import org.json.JSONObject

// TODO: Rename parameter arguments, choose names that match
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Search : Fragment(), NetworkCaller<JSONObject> {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
        val layoutManager = GridLayoutManager(context, 3)
        val recyclerView = view?.findViewById<RecyclerView>(R.id.exploreRecylcerView)
        if (recyclerView != null) {
            recyclerView.layoutManager = layoutManager
        }
        recyclerView?.setHasFixedSize(true)
        val adapter = CardAdapter(mangaList)
        if (recyclerView != null) {
            recyclerView.adapter = adapter
        }
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