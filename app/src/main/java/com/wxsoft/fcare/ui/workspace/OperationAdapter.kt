package com.wxsoft.fcare.ui.workspace

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wxsoft.fcare.core.data.entity.WorkOperation
import com.wxsoft.fcare.databinding.ItemWorkSpaceOperationBinding


class OperationAdapter constructor(private val owner:LifecycleOwner,private val itemClickListener: (WorkOperation) -> Unit) :
    ListAdapter<WorkOperation, OperationAdapter.ItemViewHolder>(DiffCallback) {

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        holder.binding.apply {
            item=getItem(position)

            executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val binding = ItemWorkSpaceOperationBinding.inflate(LayoutInflater.from(parent.context), parent, false).apply {
            lifecycleOwner=owner
        }
        return ItemViewHolder(binding)
    }

    class ItemViewHolder(binding: ItemWorkSpaceOperationBinding) : RecyclerView.ViewHolder(binding.root) {
        var binding: ItemWorkSpaceOperationBinding
            private set

        init {
            this.binding = binding
        }
    }


    object DiffCallback : DiffUtil.ItemCallback<WorkOperation>() {
        override fun areItemsTheSame(oldItem: WorkOperation, newItem: WorkOperation): Boolean {

            return oldItem.actionCode == newItem.actionCode
        }

        override fun areContentsTheSame(oldItem: WorkOperation, newItem: WorkOperation): Boolean {

            return oldItem.hasExcuted == newItem.hasExcuted  && oldItem.excutedTime == newItem.excutedTime
                    && oldItem.ico==newItem.ico && oldItem.tint==newItem.tint
        }
    }
}