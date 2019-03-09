package com.wxsoft.fcare.ui.rating


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wxsoft.fcare.core.data.entity.rating.RatingResult
import com.wxsoft.fcare.core.data.entity.rating.ScencelyRatingResult
import com.wxsoft.fcare.databinding.ItemRatingScenceBinding


class RatingResultAdapter constructor(private val owner: LifecycleOwner,
                                      private val pool:RecyclerView.RecycledViewPool,
                                      private val newItem: (String)->Unit,
                                      private val showDetail:(RatingResult)->Unit): ListAdapter<ScencelyRatingResult,RatingResultAdapter.ItemViewHolder>(DiffCallback) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ItemViewHolder(
            ItemRatingScenceBinding.inflate(inflater, parent, false).apply {
                lifecycleOwner=owner

            },pool,newItem)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.binding.apply {
            val result=getItem(position)
            item=result
            if(list.adapter==null){
                list.adapter=RatingAdapter(owner,showDetail)
            }
            (list.adapter as?RatingAdapter)?. submitList(result.items)
            executePendingBindings()
        }
    }

    class ItemViewHolder(bind: ItemRatingScenceBinding,
                         private val pool: RecyclerView.RecycledViewPool,
                         private val newItemClick: (String)->Unit) : RecyclerView.ViewHolder(bind.root) {

        var binding: ItemRatingScenceBinding
            private set

        init {
            binding = bind.apply {
                list.setRecycledViewPool(pool)

                add.setOnClickListener {
                    item?.typeId?.let { newItemClick(it) }
                }
            }
        }
    }
    object DiffCallback : DiffUtil.ItemCallback<ScencelyRatingResult>() {
        override fun areItemsTheSame(oldItem: ScencelyRatingResult, newItem: ScencelyRatingResult): Boolean {
            return oldItem.typeId==newItem.typeId && oldItem.items.size==newItem.items.size
        }

        override fun areContentsTheSame(oldItem: ScencelyRatingResult, newItem: ScencelyRatingResult): Boolean {

            return oldItem.items==newItem.items && oldItem.items.size==newItem.items.size

        }
    }
}



