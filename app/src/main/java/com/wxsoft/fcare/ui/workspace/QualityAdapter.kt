package com.wxsoft.fcare.ui.workspace

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wxsoft.fcare.core.data.entity.TimeQuality
import com.wxsoft.fcare.databinding.ItemTimeQualityBinding


class QualityAdapter constructor(private val owner:LifecycleOwner) :
    ListAdapter<TimeQuality, QualityAdapter.ItemViewHolder>(DiffCallback) {


    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        holder.binding.apply {
            item=getItem(position)

            executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val binding = ItemTimeQualityBinding.inflate(LayoutInflater.from(parent.context), parent, false).apply {
            lifecycleOwner=owner
        }
        return ItemViewHolder(binding)
    }

    class ItemViewHolder(binding: ItemTimeQualityBinding) : RecyclerView.ViewHolder(binding.root) {
        var binding: ItemTimeQualityBinding
            private set

        init {
            this.binding = binding
        }
    }


    object DiffCallback : DiffUtil.ItemCallback<TimeQuality>() {
        override fun areItemsTheSame(oldItem: TimeQuality, newItem: TimeQuality): Boolean {

            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: TimeQuality, newItem: TimeQuality): Boolean {

            return oldItem.title == newItem.title  && oldItem.score == newItem.score
        }
    }
}