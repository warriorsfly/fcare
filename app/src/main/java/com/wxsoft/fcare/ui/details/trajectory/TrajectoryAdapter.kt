package com.wxsoft.fcare.ui.details.trajectory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wxsoft.fcare.core.data.entity.TriggerRecord
import com.wxsoft.fcare.databinding.ItemTrajectoryBinding

class TrajectoryAdapter constructor(private val owner: LifecycleOwner) :
    ListAdapter<TriggerRecord, TrajectoryAdapter.ItemViewHolder>(DiffCallback) {

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        holder.binding.apply {
            item=getItem(position)
            beforeEnd=currentList.size!=position+1
            executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val binding =
            ItemTrajectoryBinding.inflate(LayoutInflater.from(parent.context), parent, false).apply {
                lifecycleOwner = owner
            }
        return ItemViewHolder(binding)
    }

    class ItemViewHolder(binding: ItemTrajectoryBinding) : RecyclerView.ViewHolder(binding.root) {
        var binding: ItemTrajectoryBinding
            private set
        init {
            this.binding = binding
        }
    }


    object DiffCallback : DiffUtil.ItemCallback<TriggerRecord>() {
        override fun areItemsTheSame(oldItem: TriggerRecord, newItem: TriggerRecord): Boolean {

            return oldItem.baseStationId == newItem.baseStationId
        }

        override fun areContentsTheSame(oldItem: TriggerRecord, newItem: TriggerRecord): Boolean {

            return oldItem.baseStationId == newItem.baseStationId
        }
    }


}