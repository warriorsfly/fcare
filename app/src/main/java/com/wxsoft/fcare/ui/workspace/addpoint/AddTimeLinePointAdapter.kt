package com.wxsoft.fcare.ui.workspace.addpoint

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wxsoft.fcare.core.data.entity.TimePoint
import com.wxsoft.fcare.databinding.ItemAddTimeLinePointItemBinding

class AddTimeLinePointAdapter constructor(private val owner: LifecycleOwner,val viewModel: AddTimeLinePointViewModel) :
    ListAdapter<TimePoint, AddTimeLinePointAdapter.ItemViewHolder>(DiffCallback) {


    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        holder.binding.apply {
            item=getItem(position)
            listener = this@AddTimeLinePointAdapter.viewModel
            executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val binding = ItemAddTimeLinePointItemBinding.inflate(LayoutInflater.from(parent.context), parent, false).apply {
            lifecycleOwner=owner
        }
        return ItemViewHolder(binding)
    }

    class ItemViewHolder(binding: ItemAddTimeLinePointItemBinding) : RecyclerView.ViewHolder(binding.root) {
        var binding: ItemAddTimeLinePointItemBinding
            private set

        init {
            this.binding = binding
        }
    }


    object DiffCallback : DiffUtil.ItemCallback<TimePoint>() {
        override fun areItemsTheSame(oldItem: TimePoint, newItem: TimePoint): Boolean {

            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TimePoint, newItem: TimePoint): Boolean {

            return oldItem.id == newItem.id
        }
    }

}