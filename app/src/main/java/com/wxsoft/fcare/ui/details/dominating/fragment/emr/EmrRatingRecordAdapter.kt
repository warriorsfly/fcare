package com.wxsoft.fcare.ui.details.dominating.fragment.emr

import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.wxsoft.fcare.core.data.entity.rating.RatingRecord
import com.wxsoft.fcare.utils.ActionType.Companion.GRACE
import com.wxsoft.fcare.databinding.ItemEmrRatingRecordBinding
import com.wxsoft.fcare.ui.EmrEventAction

class EmrRatingRecordAdapter(private val owner: LifecycleOwner,private val  action: EmrEventAction?) :ListAdapter<RatingRecord,EmrRatingRecordAdapter.ItemViewHolder>(DiffCallback) {


    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.binding.apply {
            root.setOnClickListener { action?.onOpen(GRACE, getItem(position).id) }
            rating=getItem(position)
            executePendingBindings()
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val inflater = LayoutInflater.from(parent.context)

        return ItemViewHolder(
            ItemEmrRatingRecordBinding.inflate(inflater, parent, false).apply {
                lifecycleOwner=owner
            }
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

    class ItemViewHolder(binding: ItemEmrRatingRecordBinding) : androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root) {
        var binding: ItemEmrRatingRecordBinding
            private set

        init {
            this.binding = binding
        }
    }
}
