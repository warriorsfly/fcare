package com.wxsoft.fcare.ui.rating


import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.wxsoft.fcare.core.data.entity.rating.Rating
import com.wxsoft.fcare.core.data.entity.rating.RatingResult
import com.wxsoft.fcare.databinding.ItemRatingBinding
import com.wxsoft.fcare.databinding.ItemRatingResultBinding
import com.wxsoft.fcare.ui.EventAction


class RatingAdapter constructor(private val owner: LifecycleOwner,
                                private val showDetail:(RatingResult)->Unit): ListAdapter<RatingResult,RatingAdapter.ItemViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ItemViewHolder(ItemRatingResultBinding.inflate(inflater,parent,false).apply {

            root.setOnClickListener {
                item?.let { showDetail(it) }
            }
            lifecycleOwner =owner
        })
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.binding.apply {
            item=getItem(position)
            executePendingBindings()
        }
    }

    class ItemViewHolder(bind: ItemRatingResultBinding) : RecyclerView.ViewHolder(bind.root) {

        var binding: ItemRatingResultBinding
            private set

        init {
            this.binding = bind
        }

    }

    object DiffCallback : DiffUtil.ItemCallback<RatingResult>() {
        override fun areItemsTheSame(oldItem: RatingResult, newItem: RatingResult): Boolean {
            return oldItem.id==newItem.id
        }

        override fun areContentsTheSame(oldItem: RatingResult, newItem: RatingResult): Boolean {
            return oldItem.score==newItem.score
        }
    }
}



