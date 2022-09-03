package com.tsunderead.tsundoku.explore

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tsunderead.tsundoku.R

class MangaCoverAdapter(private val context: Context, private val coverArrayList: ArrayList<MangaCover>):
    RecyclerView.Adapter<MangaCoverAdapter.MangaCoverHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MangaCoverAdapter.MangaCoverHolder {
        val v: View = LayoutInflater.from(context).inflate(R.layout.manga_cover_card, parent, false)
        return MangaCoverHolder(v)
    }

    override fun onBindViewHolder(holder: MangaCoverAdapter.MangaCoverHolder, position: Int) {
        val mangaCover: MangaCover = coverArrayList.get(position)
        holder.title.setText(mangaCover.title)
    }

    override fun getItemCount(): Int {
        return coverArrayList.size
    }

    class MangaCoverHolder: RecyclerView.ViewHolder{

        var title: TextView? = null
//        val cover_art: ShapeableImageView? = null

        constructor(itemView: View) : super(itemView) {
            title = itemView.findViewById(R.id.cover_title)
        }

    }
}