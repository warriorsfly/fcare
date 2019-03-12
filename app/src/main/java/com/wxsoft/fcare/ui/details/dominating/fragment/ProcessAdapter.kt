package com.wxsoft.fcare.ui.details.dominating.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.wxsoft.fcare.core.data.entity.TaskSpend
import com.wxsoft.fcare.databinding.ItemTaskSpendBinding


class ProcessAdapter constructor(private val owner: LifecycleOwner) :
    ListAdapter<TaskSpend,ProcessAdapter.ItemViewHolder>(DiffCallback) {


    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        holder.binding.apply {
            item = getItem(position)

            lifecycleOwner = owner
            executePendingBindings()

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val binding = ItemTaskSpendBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    class ItemViewHolder(binding: ItemTaskSpendBinding) : androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root) {
        var binding: ItemTaskSpendBinding
            private set

        init {
            this.binding = binding
        }
    }


    object DiffCallback : DiffUtil.ItemCallback<TaskSpend>() {
        override fun areItemsTheSame(oldItem: TaskSpend, newItem: TaskSpend): Boolean {

            return oldItem.task == newItem.task
        }

        override fun areContentsTheSame(oldItem: TaskSpend, newItem: TaskSpend): Boolean {

            return oldItem.spends== newItem.spends
        }
    }

}