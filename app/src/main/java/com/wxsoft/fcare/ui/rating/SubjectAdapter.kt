package com.wxsoft.fcare.ui.rating


import android.arch.lifecycle.LifecycleOwner
import android.databinding.ViewDataBinding
import android.support.v7.recyclerview.extensions.AsyncListDiffer
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.data.entity.rating.Option
import com.wxsoft.fcare.core.data.entity.rating.Rating
import com.wxsoft.fcare.core.data.entity.rating.Subject
import com.wxsoft.fcare.databinding.ItemRatingSubjectBinding
import com.wxsoft.fcare.databinding.ItemRatingSubjectItemBinding


class SubjectAdapter constructor(private val lifecycleOwner: LifecycleOwner):
    RecyclerView.Adapter<SubjectAdapter.ItemViewHolder>() {

    private val differ = AsyncListDiffer<Any>(this, DiffCallback)

    var rating: Rating?=null
    var subjects: List<Subject> = emptyList()
        set(value) {
            field = value
            val merged = mutableListOf<Any>()
            field.forEach {
                merged+=it
                merged.addAll(it.options)
            }


            differ.submitList(merged)
        }

    override fun getItemViewType(position: Int): Int {
        return when(differ.currentList[position]){
            is Subject-> R.layout.item_rating_subject
            is Option-> R.layout.item_rating_subject_item
            else -> throw IllegalStateException("Unknown view type at position $position")
        }
    }
    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when(viewType){
            R.layout.item_rating_subject -> ItemViewHolder.RatingViewHolder(
                ItemRatingSubjectBinding.inflate(inflater, parent, false)
            )
            R.layout.item_rating_subject_item -> ItemViewHolder.OptionItemViewHolder(
                ItemRatingSubjectItemBinding.inflate(inflater, parent, false)
            )
            else -> throw IllegalStateException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        when(holder) {
            is ItemViewHolder.RatingViewHolder -> {
                val presenter = differ.currentList[position] as Subject
                holder.binding.apply {
                    item=presenter
                    setLifecycleOwner(lifecycleOwner)
                    executePendingBindings()
                }
            }
            is ItemViewHolder.OptionItemViewHolder -> {
                val presenter = differ.currentList[position] as Option
                holder.binding.apply {
                    item=presenter
                    rating=this@SubjectAdapter.rating
                    subject=subjects.first { it.options.contains(presenter) }
                    setLifecycleOwner(lifecycleOwner)
                    executePendingBindings()
                }


            }
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
            return when{
                oldItem is Subject && newItem is Subject-> oldItem.id == newItem.id
                oldItem is Option && newItem is Option-> oldItem.id == newItem.id
                else-> false
            }
        }

        override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
            return when{
                oldItem is Subject && newItem is Subject-> oldItem.id == newItem.id
                oldItem is Option && newItem is Option-> oldItem.id == newItem.id
                else-> false
            }
        }
    }

    sealed class ItemViewHolder(bind: ViewDataBinding) : RecyclerView.ViewHolder(bind.root) {

        class RatingViewHolder(
            val binding: ItemRatingSubjectBinding
        ) : ItemViewHolder(binding)

        class OptionItemViewHolder(
            val binding: ItemRatingSubjectItemBinding
        ) : ItemViewHolder(binding)
    }
}




