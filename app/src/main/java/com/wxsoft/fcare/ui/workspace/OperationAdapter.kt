package com.wxsoft.fcare.ui.workspace

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wxsoft.fcare.core.data.entity.WorkOperation
import com.wxsoft.fcare.databinding.ItemWorkSpaceOperationBinding


class OperationAdapter constructor(private val owner:LifecycleOwner,private val itemClickListener: (WorkOperation) -> Unit,private val ev:Boolean=false) :
    ListAdapter<WorkOperation, OperationAdapter.ItemViewHolder>(DiffCallback) {

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        holder.binding.apply {
            getItem(position).let {
                item=it
            }
            executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val binding = ItemWorkSpaceOperationBinding.inflate(LayoutInflater.from(parent.context), parent, false).apply {
            lifecycleOwner=owner
        }.apply {
            if(ev){
                ico.elevation=3f
            }
        }
        return ItemViewHolder(binding,itemClickListener)
    }

    class ItemViewHolder(binding: ItemWorkSpaceOperationBinding, itemClickListener: (WorkOperation) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        var binding: ItemWorkSpaceOperationBinding
            private set

        init {
            this.binding = binding.apply {
                root.setOnClickListener{
                    item?.let(itemClickListener)
                }
            }
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