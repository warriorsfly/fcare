package com.wxsoft.fcare.ui.rating


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.data.entity.rating.RatingResult
import com.wxsoft.fcare.core.data.entity.rating.ScencelyRatingResult
import com.wxsoft.fcare.databinding.ItemRatingResultBinding
import com.wxsoft.fcare.databinding.ItemRatingScenceBinding


class RatingResultAdapter constructor(private val owner: LifecycleOwner,private val newItem: (String)->Unit,private val showDetail:(RatingResult)->Unit): RecyclerView.Adapter<RatingResultAdapter.ItemViewHolder>() {

    private val differ = AsyncListDiffer<Any>(this, DiffCallback)
    var items: List<ScencelyRatingResult> = emptyList()
        set(value) {
            field = value
            val merged = mutableListOf<Any>()
            field.forEach {
                merged+=it
                merged.addAll(it.items)
            }
            differ.submitList(value)
        }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            R.layout.item_rating_scence -> ItemViewHolder.ScenceViewHolder(
                ItemRatingScenceBinding.inflate(inflater, parent, false).apply { lifecycleOwner=owner },newItem)
            R.layout.item_rating_result -> ItemViewHolder.ResultViewHolder(
                ItemRatingResultBinding.inflate(inflater, parent, false).apply { lifecycleOwner=owner },showDetail)

            else -> throw IllegalStateException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        when(holder){
            is ItemViewHolder.ScenceViewHolder->{
                holder.binding.apply {
                    item=differ.currentList[position] as ScencelyRatingResult
                    executePendingBindings()
                }
            }
            is ItemViewHolder.ResultViewHolder->{
                holder.binding.apply {
                    item=differ.currentList[position] as RatingResult
                    executePendingBindings()
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(differ.currentList[position]){
            is ScencelyRatingResult-> R.layout.item_rating_scence
            is RatingResult->R.layout.item_rating_result
            else -> throw IllegalStateException("Unknown view type at position $position")
        }
    }



    sealed class ItemViewHolder(bind: ViewDataBinding) : RecyclerView.ViewHolder(bind.root) {

        //无结果
        class ScenceViewHolder(
            val binding: ItemRatingScenceBinding,private val newItemClick: (String)->Unit
        ) : ItemViewHolder(binding){
            init {
                binding.apply {
                    add.setOnClickListener {
                        item?.typeId?.let { newItemClick(it) }
                    }
                    executePendingBindings()
                }
            }
        }

        //基本信息
        class ResultViewHolder(
            val binding: ItemRatingResultBinding,private val showDetailClick:(RatingResult)->Unit
        ) : ItemViewHolder(binding){
            init {
                binding.apply {
                    root.setOnClickListener {
                        item?.let(showDetailClick)
                    }
                }
            }
        }

    }

    object DiffCallback : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
            return when{
                oldItem is  ScencelyRatingResult && newItem is ScencelyRatingResult->
                    return oldItem.typeId==newItem.typeId

                oldItem is  RatingResult && newItem is RatingResult->
                    return oldItem.id==newItem.id

                else->false
            }
        }

        override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
            return when{
                oldItem is  ScencelyRatingResult && newItem is ScencelyRatingResult->
                    return oldItem.items==newItem.items

                oldItem is  RatingResult && newItem is RatingResult->
                    return oldItem.score==newItem.score

                else->false
            }
        }
    }
}



