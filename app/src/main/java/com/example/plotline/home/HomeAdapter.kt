package com.example.plotline.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.plotline.ImageData
import com.example.plotline.databinding.HomeItemBinding


class HomeAdapter:ListAdapter<ImageData,HomeAdapter.vh>(diffUtil) {
    inner  class vh(private val itemBinding: HomeItemBinding):RecyclerView.ViewHolder(itemBinding.root){
        fun bind(data:ImageData){
            itemBinding.edged.setImageURI(data.edgedImage.toUri())
            itemBinding.normal.setImageURI(data.originalImage.toUri())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): vh {
        val view =
            HomeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return vh(view)
    }

    override fun onBindViewHolder(holder: vh, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<ImageData>() {
            override fun areItemsTheSame(
                oldItem: ImageData,
                newItem: ImageData
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ImageData,
                newItem: ImageData
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}