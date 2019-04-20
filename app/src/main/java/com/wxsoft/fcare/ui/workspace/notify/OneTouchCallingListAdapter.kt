package com.wxsoft.fcare.ui.workspace.notify

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.wxsoft.fcare.core.data.entity.Dictionary
import com.wxsoft.fcare.databinding.ItemOnetouchcallingBinding

class OneTouchCallingListAdapter constructor(private val owner: LifecycleOwner, val viewModel: OneTouchCallingViewModel) :
    ListAdapter<Dictionary, OneTouchCallingListAdapter.ItemViewHolder>(DiffCallback){

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        holder.binding.apply {
            val yy = getItem(position)
            item = yy
            viewModel = this@OneTouchCallingListAdapter.viewModel
            executePendingBindings()

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemOnetouchcallingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            .apply {
                lifecycleOwner = owner
            }
        return ItemViewHolder(binding)
    }

    class ItemViewHolder(binding: ItemOnetouchcallingBinding) : androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root) {
        var binding: ItemOnetouchcallingBinding
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