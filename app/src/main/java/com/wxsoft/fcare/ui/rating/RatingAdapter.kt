package com.wxsoft.fcare.ui.rating


import android.arch.lifecycle.LifecycleOwner
import android.databinding.ViewDataBinding
import android.support.v7.recyclerview.extensions.AsyncListDiffer
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wxsoft.fcare.core.data.entity.rating.Option

import com.wxsoft.fcare.core.data.entity.rating.Rating


class RatingAdapter constructor(private val lifecycleOwner: LifecycleOwner): RecyclerView.Adapter< ItemViewHolder>() {

    private val differ = AsyncListDiffer<Any>(this, DiffCallback)

    var groups: List<Rating> = emptyList()
        set(value) {
            field = value
            val newList=field.map {
                var l = ArrayList<Any>()
                l.add(it)
                l.addAll(it.)
                return@map l
            }
            val tot=ArrayList<Any>()
            for( group in newList){
                tot.addAll(group)
            }

            differ.submitList(tot)
        }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


    object DiffCallback : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
            return when{
                oldItem is Rating && newItem is Rating-> oldItem.id == newItem.id
                oldItem is Option && newItem is Option-> oldItem.id == newItem.id
                else-> false
            }
        }

        override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
            return when{
                oldItem is Rating && newItem is Rating-> oldItem.id == newItem.id
                oldItem is Option && newItem is Option-> oldItem.id == newItem.id
                else-> false
            }
        }
    }
}


/**
 * [RecyclerView.ViewHolder] types used by this adapter.
 */
sealed class ItemViewHolder(bind: ViewDataBinding) : RecyclerView.ViewHolder(bind.root) {

    class ScoreGroupViewHolder(
        val binding: ItemScoreGroupBinding
    ) : ItemViewHolder(binding)

    class ScoreItemViewHolder(
        val binding: ItemScoreSelectionBinding
    ) : ItemViewHolder(binding)
}


