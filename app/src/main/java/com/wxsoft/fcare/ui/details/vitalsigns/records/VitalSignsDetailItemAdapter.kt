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
import com.wxsoft.fcare.databinding.ItemVitalSignsRecordListItemBinding

class VitalSignsDetailItemAdapter constructor(private val lifecycleOwner: LifecycleOwner, val viewModel: VitalSignsRecordViewModel) :
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
            setVariable(BR.item, differ.currentList[position])
            setVariable(BR.viewModel, viewModel)
            lifecycleOwner = this@VitalSignsDetailItemAdapter.lifecycleOwner
            executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val binding: ViewDataBinding =
            ItemVitalSignsRecordListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    class ItemViewHolder(binding: ViewDataBinding) : androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root) {
        var binding: ViewDataBinding
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