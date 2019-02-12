package com.wxsoft.fcare.ui.rating


import android.arch.lifecycle.LifecycleOwner
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.wxsoft.fcare.core.data.entity.rating.Rating
import com.wxsoft.fcare.databinding.ItemRatingBinding
import com.wxsoft.fcare.ui.EventAction


class RatingAdapter constructor(private val lifecycleOwner: LifecycleOwner): ListAdapter<Rating,RatingAdapter.ItemViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ItemViewHolder(ItemRatingBinding.inflate(inflater,parent,false))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.binding.apply {
            item=getItem(position)
            root.setOnClickListener { action?.onOpen(getItem(position)) }
            lifecycleOwner = this@RatingAdapter.lifecycleOwner
        }
    }


    class ItemViewHolder(bind: ItemRatingBinding) : RecyclerView.ViewHolder(bind.root) {

        var binding: ItemRatingBinding
            private set

        init {
            this.binding = bind
        }

    }

    private var action: EventAction<Rating>?=null

    fun setActionListener(actions: EventAction<Rating>){
        this.action=actions
    }
    object DiffCallback : DiffUtil.ItemCallback<Rating>() {
        override fun areItemsTheSame(oldItem: Rating, newItem: Rating): Boolean {
            return return oldItem.id==newItem.id
        }

        override fun areContentsTheSame(oldItem: Rating, newItem: Rating): Boolean {
            return oldItem.id==newItem.id
        }
    }
}



