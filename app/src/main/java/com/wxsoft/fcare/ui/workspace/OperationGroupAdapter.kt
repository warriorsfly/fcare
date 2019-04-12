package com.wxsoft.fcare.ui.workspace

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wxsoft.fcare.core.data.entity.Record
import com.wxsoft.fcare.core.data.entity.WorkOperation
import com.wxsoft.fcare.databinding.ItemOperationListBinding


class OperationGroupAdapter constructor(private val owner:LifecycleOwner,private val pool: RecyclerView.RecycledViewPool, private val itemClickListener: (WorkOperation) -> Unit) :
    ListAdapter<Record<WorkOperation>, OperationGroupAdapter.ItemViewHolder>(DiffCallback) {

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        holder.binding.apply {
            getItem(position).let {
                head.text=it.typeName
                (list.adapter as? OperationAdapter)?.submitList(it.items)
            }
            executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val binding = ItemOperationListBinding.inflate(LayoutInflater.from(parent.context), parent, false).apply {
            lifecycleOwner=owner

            list.adapter=OperationAdapter(owner,itemClickListener)
            list.setRecycledViewPool(pool)
        }
        return ItemViewHolder(binding)
    }

    class ItemViewHolder(val binding: ItemOperationListBinding) : RecyclerView.ViewHolder(binding.root)


    object DiffCallback : DiffUtil.ItemCallback<Record<WorkOperation>>() {
        override fun areItemsTheSame(oldItem: Record<WorkOperation>, newItem: Record<WorkOperation>): Boolean {

            return oldItem.typeId == newItem.typeId
        }

        override fun areContentsTheSame(oldItem: Record<WorkOperation>, newItem: Record<WorkOperation>): Boolean {

            return oldItem.typeName == newItem.typeName  && oldItem.items == newItem.items
        }
    }
}