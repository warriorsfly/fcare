package com.wxsoft.fcare.ui.selecter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.wxsoft.fcare.core.data.entity.Dictionary
import com.wxsoft.fcare.databinding.ItemSelecterOfOneBinding

class SelecterOfOneAdapter constructor(private val owner: LifecycleOwner, val viewModel: SelecterOfOneViewModel) :
    ListAdapter<Dictionary, SelecterOfOneAdapter.ItemViewHolder>(DiffCallback){

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        holder.binding.apply {
            val yy = getItem(position)
            item = yy
            listener = viewModel
            executePendingBindings()

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val binding = ItemSelecterOfOneBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            .apply {
                lifecycleOwner = owner
            }
        return ItemViewHolder(binding)
    }

    class ItemViewHolder(binding: ItemSelecterOfOneBinding) : androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root) {
        var binding: ItemSelecterOfOneBinding
            private set

        init {
            this.binding = binding
        }
    }


    object DiffCallback : DiffUtil.ItemCallback<Dictionary>() {
        override fun areItemsTheSame(oldItem: Dictionary, newItem: Dictionary): Boolean {

            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Dictionary, newItem: Dictionary): Boolean {

            return oldItem.id == newItem.id
        }
    }
}