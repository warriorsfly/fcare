package com.wxsoft.fcare.ui.details.vitalsigns.records

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.wxsoft.fcare.core.BR
import com.wxsoft.fcare.core.data.entity.VitalSign
import com.wxsoft.fcare.databinding.ItemVitalRecordDetailsItemBinding

class VitalSignsDetailItemAdapter constructor(private val owner: LifecycleOwner) :
    RecyclerView.Adapter<VitalSignsDetailItemAdapter.ItemViewHolder>(){

    private val differ = AsyncListDiffer<VitalSign>(this, DiffCallback)

    var items: List<VitalSign> = emptyList()
        set(value) {
            field = value
            differ.submitList(value)
        }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        holder.binding.apply {
            item=differ.currentList[position]
            executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val binding =
            ItemVitalRecordDetailsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//                .apply {
//                    lifecycleOwner=owner
//                }
        return ItemViewHolder(binding)
    }

    class ItemViewHolder(binding: ItemVitalRecordDetailsItemBinding) : RecyclerView.ViewHolder(binding.root) {
        var binding: ItemVitalRecordDetailsItemBinding
            private set
        init {
            this.binding = binding
        }
    }


    object DiffCallback : DiffUtil.ItemCallback<VitalSign>() {
        override fun areItemsTheSame(oldItem: VitalSign, newItem: VitalSign): Boolean {

            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: VitalSign, newItem: VitalSign): Boolean {

            return oldItem.id == newItem.id
        }
    }


}