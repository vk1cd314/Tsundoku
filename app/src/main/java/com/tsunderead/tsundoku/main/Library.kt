package com.tsunderead.tsundoku.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tsunderead.tsundoku.R
import com.tsunderead.tsundoku.manga_card_cell.CardCellAdapter
import com.tsunderead.tsundoku.manga_card_cell.Manga

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Library.newInstance] factory method to
 * create an instance of this fragment.
 */
class Library : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var adapter: CardCellAdapter
    private lateinit var recyclerView : RecyclerView

    private lateinit var mangaList : ArrayList<Manga>
    private lateinit var covers : Array<Int>

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
        return inflater.inflate(R.layout.fragment_library, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Library.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Library().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        dataInit()
        val layoutManager = LinearLayoutManager(context)
        recyclerView = view.findViewById(R.id.library_recyler_view)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        adapter = CardCellAdapter(mangaList)
        recyclerView.adapter = adapter
    }

    private fun dataInit() {
        mangaList = arrayListOf<Manga>()
        covers = arrayOf(
            R.drawable.ginnosaji,
            R.drawable.ginnosaji,
            R.drawable.ginnosaji
        )
//        for (i in covers.indices) {
        val manga1 = Manga("https:\\/\\/uploads.mangadex.org\\/covers\\/f9b82990-7198-4131-84bb-c952830f5ea7\\/6754b3ba-a9cd-4f07-89a5-ff4145f24605.jpg", "Nisio Isin", "Bakemonogatari", "1")
        mangaList.add(manga1)
        val manga2 = Manga("https:\\/\\/uploads.mangadex.org\\/covers\\/f9b82990-7198-4131-84bb-c952830f5ea7\\/6754b3ba-a9cd-4f07-89a5-ff4145f24605.jpg", "Hiromu Arakawa", "Ginnosaji", "2")
        mangaList.add(manga2)
        val manga3 = Manga("https:\\/\\/uploads.mangadex.org\\/covers\\/f9b82990-7198-4131-84bb-c952830f5ea7\\/6754b3ba-a9cd-4f07-89a5-ff4145f24605.jpg", "Kentaro Miura", "Berserk", "3")
        mangaList.add(manga3)


//        }
    }
}