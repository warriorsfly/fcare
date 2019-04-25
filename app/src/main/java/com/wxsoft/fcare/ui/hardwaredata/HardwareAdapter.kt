package com.wxsoft.fcare.ui.hardwaredata

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.wxsoft.fcare.core.data.entity.hardware.MindrayDevices
import com.wxsoft.fcare.databinding.ItemHardwareDeviceBinding

class HardwareAdapter constructor(private val owner: LifecycleOwner, val viewModel: HardwareDataViewModel) :
    ListAdapter<MindrayDevices, HardwareAdapter.ItemViewHolder>(DiffCallback){

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        holder.binding.apply {
            val yy = getItem(position)
            device = yy
            viewmodel = viewModel
            executePendingBindings()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val binding = ItemHardwareDeviceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            .apply {
                lifecycleOwner = owner
            }
        return ItemViewHolder(binding)
    }

    class ItemViewHolder(binding:ItemHardwareDeviceBinding) : androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root) {
        var binding: ItemHardwareDeviceBinding
            private set

        init {
            this.binding = binding
        }
    }


    object DiffCallback : DiffUtil.ItemCallback<MindrayDevices>() {
        override fun areItemsTheSame(oldItem: MindrayDevices, newItem: MindrayDevices): Boolean {

            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MindrayDevices, newItem: MindrayDevices): Boolean {

            return oldItem.id == newItem.id
        }
    }
}