package com.wxsoft.fcare.ui.details.blood.pressure

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.data.entity.BloodPressureItem
import com.wxsoft.fcare.databinding.ItemBloodPressureBinding

class BloodPressureAdapter constructor(private val owner: LifecycleOwner, private val click:(BloodPressureItem)->Unit) :
    ListAdapter<BloodPressureItem,BloodPressureAdapter.ItemViewHolder>(DiffCallback) {

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
            ItemBloodPressureBinding.inflate(LayoutInflater.from(parent.context), parent, false).apply {
//                root.setOnClickListener{item?.let(click)}
                lifecycleOwner = owner
            }
        return ItemViewHolder(binding)
            .apply {

                binding.root.setOnClickListener {
                    pos = adapterPosition
                    binding.item?.let(click)
                    notifyDataSetChanged()

                }
            }
    }

    class ItemViewHolder(binding: ItemBloodPressureBinding) : RecyclerView.ViewHolder(binding.root) {
        var binding: ItemBloodPressureBinding
            private set
        init {
            this.binding = binding
        }
    }


    object DiffCallback : DiffUtil.ItemCallback<BloodPressureItem>() {
        override fun areItemsTheSame(oldItem: BloodPressureItem, newItem: BloodPressureItem): Boolean {

            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: BloodPressureItem, newItem: BloodPressureItem): Boolean {

            return oldItem.dbp == newItem.dbp && oldItem.heartRate==newItem.heartRate && oldItem.sbp==newItem.sbp
        }
    }


}