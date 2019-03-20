package com.wxsoft.fcare.ui.selecter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.wxsoft.fcare.core.data.entity.Dictionary
import com.wxsoft.fcare.core.data.entity.NotifyType
import com.wxsoft.fcare.databinding.ItemSelecterOfNotifyBinding
import com.wxsoft.fcare.databinding.ItemSelecterOfOneBinding

class SelecterOfNotifyTypeAdapter constructor(private val owner: LifecycleOwner, val viewModel: SelecterOfOneViewModel) :
    ListAdapter<NotifyType, SelecterOfNotifyTypeAdapter.ItemViewHolder>(DiffCallback){

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        holder.binding.apply {
            val yy = getItem(position)
            item = yy
            listener = viewModel
            executePendingBindings()

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val binding = ItemSelecterOfNotifyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            .apply {
                lifecycleOwner = owner
            }
        return ItemViewHolder(binding)
    }

    class ItemViewHolder(binding: ItemSelecterOfNotifyBinding) : androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root) {
        var binding: ItemSelecterOfNotifyBinding
            private set

        init {
            this.binding = binding
        }
    }


    object DiffCallback : DiffUtil.ItemCallback<NotifyType>() {
        override fun areItemsTheSame(oldItem: NotifyType, newItem: NotifyType): Boolean {

            return oldItem.templateId == newItem.templateId
        }

        override fun areContentsTheSame(oldItem: NotifyType, newItem: NotifyType): Boolean {

            return oldItem.templateId == newItem.templateId
        }
    }

}