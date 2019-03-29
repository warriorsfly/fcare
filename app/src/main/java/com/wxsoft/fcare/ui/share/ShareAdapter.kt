package com.wxsoft.fcare.ui.share

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wxsoft.fcare.databinding.ItemShareListItemBinding

class ShareAdapter constructor(private val owner: LifecycleOwner,val viewModel: ShareViewModel) :
    ListAdapter<String, ShareAdapter.ItemViewHolder>(DiffCallback) {


    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        holder.binding.apply {
            uri=getItem(position)
            viewModel= this@ShareAdapter.viewModel
            executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val binding = ItemShareListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false).apply {
            lifecycleOwner=owner
        }
        return ItemViewHolder(binding)
    }

    class ItemViewHolder(binding: ItemShareListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        var binding: ItemShareListItemBinding
            private set

        init {
            this.binding = binding
        }
    }


    object DiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {

            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {

            return oldItem == newItem
        }
    }
}