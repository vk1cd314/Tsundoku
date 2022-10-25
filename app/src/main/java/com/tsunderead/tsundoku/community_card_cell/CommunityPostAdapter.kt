package com.tsunderead.tsundoku.community_card_cell

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tsunderead.tsundoku.databinding.CommunityCardCellBinding

class CommunityPostAdapter(private val postList: ArrayList<CommunityPost>) :
    RecyclerView.Adapter<CommunityPostViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityPostViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = CommunityCardCellBinding.inflate(from, parent, false)
        return CommunityPostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CommunityPostViewHolder, position: Int) {
        holder.bindPost(postList[position])
    }

    override fun getItemCount(): Int {
        return postList.size
    }
}