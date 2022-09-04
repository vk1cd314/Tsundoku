package com.tsunderead.tsundoku.explore

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tsunderead.tsundoku.R

class ExploreActivity: AppCompatActivity() {
    var coverCardRecycler: RecyclerView? = null
    var mangaCover: ArrayList<MangaCover>? = null
    var adapter: MangaCoverAdapter? = null
    var title: Array<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_explore)
        coverCardRecycler = findViewById(R.id.cover_card_recycler)
        coverCardRecycler!!.layoutManager = LinearLayoutManager(this)
        coverCardRecycler!!.setHasFixedSize(true)

        mangaCover = ArrayList<MangaCover>()
        adapter = MangaCoverAdapter(this, mangaCover!!)
        coverCardRecycler!!.adapter = adapter

        val titleList = arrayListOf<String>("a","b","c","d","e","f","g","h","i")
        for (title in titleList) mangaCover!!.add(MangaCover(title))
        adapter!!.notifyDataSetChanged()
    }

}