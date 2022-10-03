package com.tsunderead.tsundoku

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tsunderead.tsundoku.api.ApiCall
import com.tsunderead.tsundoku.api.MangaWithCover
import com.tsunderead.tsundoku.api.NetworkCaller
import org.json.JSONObject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Search.newInstance] factory method to
 * create an instance of this fragment.
 */
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        val apicallbutton = view.findViewById<Button>(R.id.apicallbutton)
//        apicallbutton.setOnClickListener {
//            val srch = MangaWithCover(this)
//            srch.execute(6)
//        }
        val callApi = MangaWithCover(this)
        callApi.execute(0)

    }

    override fun onCallSuccess(result: JSONObject?) {
        Log.i("ok", result.toString())
        val mangaList = ArrayList<Manga>()
//        val covers = arrayOf(
//            R.drawable.bakemonogatari,
//            R.drawable.ginnosaji,
//            R.drawable.berserk40
//        )
//        val manga1 = Manga(covers[0], "Nisio Isin", "Bakemonogatari")
//        mangaList.add(manga1)
        for (i in 0..9) {
            val manga1 = Manga(result!!.getJSONObject(i.toString()).getString("cover_art"), result.getJSONObject(i.toString()).getString("author"), result.getJSONObject(i.toString()).getString("name"))
            mangaList.add(manga1)
        }
        val layoutManager = LinearLayoutManager(context)
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
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Search.
         */
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