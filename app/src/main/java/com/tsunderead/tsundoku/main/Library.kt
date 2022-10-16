package com.tsunderead.tsundoku.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil.setContentView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tsunderead.tsundoku.R
import com.tsunderead.tsundoku.databinding.FragmentLibraryBinding
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
    private var fragmentLibraryBinding: FragmentLibraryBinding? = null
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var adapter: CardCellAdapter
    private lateinit var recyclerView : RecyclerView

    private lateinit var mangaList : ArrayList<Manga>
    private lateinit var covers : Array<Int>

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
////        arguments?.let {
////            param1 = it.getString(ARG_PARAM1)
////            param2 = it.getString(ARG_PARAM2)
////        }
//        setContentView(fragmentLibraryBinding?.root)
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding = FragmentLibraryBinding.inflate(inflater, container, false)
        fragmentLibraryBinding = binding
        return binding.root
        //return inflater.inflate(R.layout.fragment_library, container, false)
    }

    override fun onDestroyView() {
        fragmentLibraryBinding = null
        super.onDestroyView()
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
        val layoutManager = StaggeredGridLayoutManager( 2, StaggeredGridLayoutManager.VERTICAL)
        recyclerView = fragmentLibraryBinding?.libraryRecylerView ?: recyclerView
        recyclerView.layoutManager = layoutManager
//        recyclerView.setHasFixedSize(true)
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

    private fun dataInit() {
        mangaList = arrayListOf<Manga>()
        covers = arrayOf(
            R.drawable.ginnosaji,
            R.drawable.ginnosaji,
            R.drawable.ginnosaji
        )

        val manga1 = Manga("https://uploads.mangadex.org/covers/4265c437-7d57-4d31-9b1d-0e574a07b7b7/3ae9eed7-b8a7-47b4-945e-09fd55e267ec.jpg", "Nisio Isin", "Bakemonogatari", "4265c437-7d57-4d31-9b1d-0e574a07b7b7")
        mangaList.add(manga1)
        val manga2 = Manga("https://uploads.mangadex.org/covers/3316f5cb-c828-4ad4-b350-5bb474da9542/159fe55f-260a-4597-9dbf-8ae06e786b29.jpg", "Arakawa Hiromu", "Silver Spoon", "3316f5cb-c828-4ad4-b350-5bb474da9542")
        mangaList.add(manga2)
        val manga3 = Manga("https://uploads.mangadex.org/covers/801513ba-a712-498c-8f57-cae55b38cc92/2a61abcb-8e6e-460d-8551-1caa93e09e39.jpg", "Kentaro Miura", "Berserk", "801513ba-a712-498c-8f57-cae55b38cc92")
        mangaList.add(manga3)


//        }
    }
}