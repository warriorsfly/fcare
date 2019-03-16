package com.wxsoft.fcare.ui.details.vitalsigns.records

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.wxsoft.fcare.core.data.entity.VitalSignRecord
import com.wxsoft.fcare.databinding.ItemVitalSignsRecordListItemBinding

class VitalRecordListAdapter constructor(private val owner: LifecycleOwner, val viewModel: VitalSignsRecordViewModel) :
    RecyclerView.Adapter<VitalRecordListAdapter.ItemViewHolder>(){

    private val differ = AsyncListDiffer<VitalSignRecord>(this, DiffCallback)

    var items: List<VitalSignRecord> = emptyList()
        set(value) {
            field = value
            differ.submitList(value)
        }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        holder.binding.apply {
            val tt = differ.currentList[position]
            item= tt
            viewModel=this@VitalRecordListAdapter.viewModel
            if (vitalRecordsDetailsList.adapter == null) {
                val adapter = VitalSignsDetailItemAdapter(owner,this@VitalRecordListAdapter.viewModel)
                vitalRecordsDetailsList.adapter = adapter
            }
            (vitalRecordsDetailsList.adapter as? VitalSignsDetailItemAdapter)?.items = tt.items

            lifecycleOwner = owner
            executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val binding: ItemVitalSignsRecordListItemBinding =
            ItemVitalSignsRecordListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    class ItemViewHolder(binding: ItemVitalSignsRecordListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        var binding: ItemVitalSignsRecordListItemBinding
            private set
        init {
            this.binding = binding
        }
    }


    object DiffCallback : DiffUtil.ItemCallback<VitalSignRecord>() {
        override fun areItemsTheSame(oldItem: VitalSignRecord, newItem: VitalSignRecord): Boolean {

            return oldItem.typeId == newItem.typeId
        }

        override fun areContentsTheSame(oldItem: VitalSignRecord, newItem: VitalSignRecord): Boolean {

            return oldItem.items == newItem.items
        }
    }



}