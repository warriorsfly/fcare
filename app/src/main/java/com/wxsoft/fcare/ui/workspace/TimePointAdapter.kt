package com.wxsoft.fcare.ui.workspace

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.data.entity.TimePoint
import com.wxsoft.fcare.core.data.entity.TimePointHead
import com.wxsoft.fcare.databinding.ItemTimePointBinding
import com.wxsoft.fcare.databinding.ItemTimePointHeadBinding


class TimePointAdapter constructor(private val owner:LifecycleOwner,private val itemClick:(TimePoint)->Unit) :
    RecyclerView.Adapter<TimePointAdapter.ItemViewHolder>() {

    val differ = AsyncListDiffer<Any>(this, DiffCallback)

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    var points: List<TimePoint> = emptyList()
        set(value) {
            field = value
            val merged = mutableListOf<Any>()
            merged += TimePointHead(field.firstOrNull { it.excutedAt?.isNotEmpty() ?: false }?.excutedAt ?.substring(0,10))
            merged.addAll(field)
            differ.submitList(merged)
        }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        when(holder){
            is ItemViewHolder.TimePointViewHolder->{
                holder.binding.apply {
                    item = differ.currentList[position] as TimePoint
                    beforeEnd=differ.currentList.size!=position+1
                    executePendingBindings()
                }
            }
            is ItemViewHolder.DateViewHolder->{
                holder.binding.apply {
                    item = differ.currentList[position] as TimePointHead
                    executePendingBindings()
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        return when(viewType) {
            R.layout.item_time_point -> {

                ItemViewHolder.TimePointViewHolder(
                    ItemTimePointBinding.inflate(LayoutInflater.from(parent.context), parent, false).apply {
                        root.setOnClickListener {
                            item?.let {
                                if (it.editable) {
                                    itemClick(it)
                                }
                            }
                        }
                        lifecycleOwner = owner
                    })
            }

            R.layout.item_time_point_head -> {

                ItemViewHolder.DateViewHolder(
                    ItemTimePointHeadBinding.inflate(LayoutInflater.from(parent.context), parent, false).apply {
                        lifecycleOwner = owner
                    })
            }
            else->throw IllegalArgumentException("unkown viewType $viewType")
        }
//        return ItemViewHolder(binding)
    }

    override fun getItemViewType(position: Int): Int {
        return when (differ.currentList[position]) {
            is TimePointHead -> R.layout.item_time_point_head
            is TimePoint -> R.layout.item_time_point
            else -> throw IllegalArgumentException("unkown type at postion[$position]")
        }
    }


    sealed class ItemViewHolder(binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

        //无结果
        class TimePointViewHolder(val binding: ItemTimePointBinding) : ItemViewHolder(binding)

        //基本信息
        class DateViewHolder(val binding: ItemTimePointHeadBinding) : ItemViewHolder(binding)
    }


    object DiffCallback : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {

            return when{
                oldItem is TimePointHead && newItem is TimePointHead->oldItem.excutedAt==newItem.excutedAt
                oldItem is TimePoint && newItem is TimePoint -> oldItem.id==newItem.id
                else-> throw IllegalArgumentException("unKnown type]")
            }
        }

        override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {

            return when{
                oldItem is TimePointHead && newItem is TimePointHead->
                    oldItem.excutedAt==newItem.excutedAt
                oldItem is TimePoint && newItem is TimePoint ->
                    oldItem.eventName == newItem.eventName  && oldItem.excutedAt == newItem.excutedAt
                else-> throw IllegalArgumentException("unKnown type]")
            }
        }
    }
}