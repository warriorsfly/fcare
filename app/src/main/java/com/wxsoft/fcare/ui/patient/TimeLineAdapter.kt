package com.wxsoft.fcare.ui.patient

import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.wxsoft.fcare.core.data.entity.NewTimeLine
import com.wxsoft.fcare.databinding.ItemTimelineNoneBinding


class TimeLineAdapter :
    ListAdapter<NewTimeLine, TimeLineAdapter.ItemViewHolder>(DiffCallback) {


    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        holder.binding.apply {

            starter=position==0
            ender=position==itemCount-1
            item=getItem(position)
            executePendingBindings()

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val binding = ItemTimelineNoneBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    class ItemViewHolder(binding: ItemTimelineNoneBinding) : RecyclerView.ViewHolder(binding.root) {
        var binding: ItemTimelineNoneBinding
            private set

        init {
            this.binding = binding
        }
    }


    object DiffCallback : DiffUtil.ItemCallback<NewTimeLine>() {
        override fun areItemsTheSame(oldItem: NewTimeLine, newItem: NewTimeLine): Boolean {

            return oldItem.eventName == newItem.eventName
        }

        override fun areContentsTheSame(oldItem: NewTimeLine, newItem: NewTimeLine): Boolean {

            return oldItem.eventName == newItem.eventName  && oldItem.excutedAt == newItem.excutedAt
        }
    }
}