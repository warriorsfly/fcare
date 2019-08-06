package com.wxsoft.fcare.ui.details.evaluate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.data.entity.EvaluateItem
import com.wxsoft.fcare.databinding.ItemEvaluateBinding

class EvaluateAdapter constructor(private val owner: LifecycleOwner,private val viewModel: EvaluateViewModel, private val click:(EvaluateItem)->Unit) :
    ListAdapter<EvaluateItem,EvaluateAdapter.ItemViewHolder>(DiffCallback) {

    private var pos = 0

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        holder.binding.apply {
            item=getItem(position)

            root.setBackgroundResource(if(position==pos) R.color.white else android.R.color.transparent)
            executePendingBindings()

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val binding =
            ItemEvaluateBinding.inflate(LayoutInflater.from(parent.context), parent, false).apply {
                lifecycleOwner = owner
            }
        return ItemViewHolder(binding).apply {

            binding.root.setOnClickListener {
                pos = adapterPosition
                this@EvaluateAdapter.viewModel.clickIndex.set(pos)
                binding.item?.let(click)
                notifyDataSetChanged()

            }
        }
    }

    class ItemViewHolder(binding: ItemEvaluateBinding) : RecyclerView.ViewHolder(binding.root) {
        var binding: ItemEvaluateBinding
            private set
        init {
            this.binding = binding
        }
    }


    object DiffCallback : DiffUtil.ItemCallback<EvaluateItem>() {
        override fun areItemsTheSame(oldItem: EvaluateItem, newItem: EvaluateItem): Boolean {

            return oldItem.name== newItem.name
        }

        override fun areContentsTheSame(oldItem: EvaluateItem, newItem: EvaluateItem): Boolean {

            return oldItem.id == newItem.id
                    && oldItem.dbp == newItem.dbp
                    && oldItem.heart_Rate==newItem.heart_Rate
                    && oldItem.sbp==newItem.sbp
        }
    }


}