package com.wxsoft.fcare.ui.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wxsoft.fcare.databinding.ItemImageRemoteBinding

class EmrImageAdapter constructor(private val owner: LifecycleOwner) :
    ListAdapter<String,EmrImageAdapter.ItemViewHolder>(DiffCallback) {



    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        holder.binding.apply {
            url =getItem(position)
            executePendingBindings()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        return ItemViewHolder(
            ItemImageRemoteBinding.inflate(inflater, parent, false).apply {
                lifecycleOwner=owner
            }
        )
    }



    object DiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return return newItem == oldItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return newItem == oldItem
        }

    }

    class ItemViewHolder( val binding: ItemImageRemoteBinding) : RecyclerView.ViewHolder(binding.root)
}

