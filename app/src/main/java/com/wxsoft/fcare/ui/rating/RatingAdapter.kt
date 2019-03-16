package com.wxsoft.fcare.ui.rating


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wxsoft.fcare.core.data.entity.rating.RatingResult
import com.wxsoft.fcare.databinding.ItemNewEmrRatingItemBinding
import com.wxsoft.fcare.databinding.ItemNewEmrRatingListBinding
import com.wxsoft.fcare.databinding.ItemRatingResultBinding


class RatingAdapter constructor(private val owner: LifecycleOwner,
                                private val showDetail:(RatingResult)->Unit,
                                private val scencely:Boolean = true):
    ListAdapter<RatingResult,RatingAdapter.ItemViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        if(scencely){
            return RatingAdapter.ItemViewHolder.ScencelyViewHolder(ItemRatingResultBinding.inflate(inflater, parent, false).apply {

                root.setOnClickListener {
                    item?.let { showDetail(it) }
                }
                lifecycleOwner = owner
            })
        }else {
            return RatingAdapter.ItemViewHolder.EmrViewHolder(ItemNewEmrRatingItemBinding.inflate(inflater, parent, false).apply {
                lifecycleOwner = owner
            })
        }
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        when(holder){
            is RatingAdapter.ItemViewHolder.ScencelyViewHolder->{
                holder.binding.apply {
                    item=getItem(position)
                    executePendingBindings()
                }
            }

            is RatingAdapter.ItemViewHolder.EmrViewHolder->{
                holder.binding.apply {
                    item=getItem(position)
                    executePendingBindings()
                }
            }
        }

    }

    sealed class ItemViewHolder(open val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {


        class ScencelyViewHolder(override val binding: ItemRatingResultBinding) : ItemViewHolder(binding)
        class EmrViewHolder(override val binding: ItemNewEmrRatingItemBinding) : ItemViewHolder(binding)
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



