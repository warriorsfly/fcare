package com.wxsoft.fcare.ui.rating

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wxsoft.fcare.core.data.entity.rating.Rating
import com.wxsoft.fcare.core.data.entity.rating.Subject
import com.wxsoft.fcare.databinding.ItemRatingSubjectBinding


class SubjectAdapter constructor(private val owner: LifecycleOwner,
                                 private val pool: RecyclerView.RecycledViewPool,
                                 private var rat:Rating?=null):
    ListAdapter<Subject,SubjectAdapter.ItemViewHolder>(DiffCallback) {

    fun setRat(rating:Rating){
        rat=rating
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ItemViewHolder(
            ItemRatingSubjectBinding.inflate(inflater, parent, false).apply {
                optionList.setRecycledViewPool(pool)
                lifecycleOwner=owner
            }
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.binding.apply {
            val sub=getItem(position)
            item=sub
            if(optionList.adapter==null){
                optionList.adapter=OptionAdapter(owner,rat)
            }
            (optionList.adapter as? OptionAdapter)?.apply {
                subject=sub
                submitList(sub.options)
            }
            executePendingBindings()
        }

    }

    object DiffCallback : DiffUtil.ItemCallback<Subject>() {
        override fun areItemsTheSame(oldItem: Subject, newItem: Subject): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Subject, newItem: Subject): Boolean {
            return oldItem.options == newItem.options
        }
    }

    class ItemViewHolder(val binding: ItemRatingSubjectBinding) : RecyclerView.ViewHolder(binding.root)
}




