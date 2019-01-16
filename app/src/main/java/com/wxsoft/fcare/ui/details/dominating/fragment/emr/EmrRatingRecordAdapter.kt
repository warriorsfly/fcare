package com.wxsoft.fcare.ui.details.dominating.fragment.emr

import android.arch.lifecycle.LifecycleOwner
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.wxsoft.fcare.core.data.entity.rating.RatingRecord
import com.wxsoft.fcare.databinding.ItemEmrRatingRecordBinding

class EmrRatingRecordAdapter :ListAdapter<RatingRecord,EmrRatingRecordAdapter.ItemViewHolder>(DiffCallback) {


    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.binding.apply {

            rating=getItem(position)
            executePendingBindings()
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val inflater = LayoutInflater.from(parent.context)

        return ItemViewHolder(
            ItemEmrRatingRecordBinding.inflate(inflater, parent, false)
        )
    }
    object DiffCallback : DiffUtil.ItemCallback<RatingRecord>() {
        override fun areItemsTheSame(oldItem: RatingRecord, newItem: RatingRecord): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: RatingRecord, newItem: RatingRecord): Boolean {
            return oldItem==newItem
        }
    }

    class ItemViewHolder(binding: ItemEmrRatingRecordBinding) : RecyclerView.ViewHolder(binding.root) {
        var binding: ItemEmrRatingRecordBinding
            private set

        init {
            this.binding = binding
        }
    }
}
