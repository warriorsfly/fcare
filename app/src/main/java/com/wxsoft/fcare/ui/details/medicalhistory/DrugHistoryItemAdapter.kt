package com.wxsoft.fcare.ui.details.medicalhistory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wxsoft.fcare.core.data.entity.drug.DrugRecord
import com.wxsoft.fcare.databinding.ItemDrugHistoryBinding


class DrugHistoryItemAdapter constructor(private val owner: LifecycleOwner, val viewModel: MedicalHistoryViewModel) :
    ListAdapter<DrugRecord,DrugHistoryItemAdapter.ItemViewHolder>(DiffCallback) {

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        holder.binding.apply {
            item=getItem(position)
            executePendingBindings()
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        return  ItemViewHolder(ItemDrugHistoryBinding.inflate(inflater, parent, false).apply {
            lifecycleOwner=owner
            viewModel = this@DrugHistoryItemAdapter.viewModel
        })
    }



    class ItemViewHolder(val binding: ItemDrugHistoryBinding) : RecyclerView.ViewHolder(binding.root)

    object DiffCallback : DiffUtil.ItemCallback<DrugRecord>() {
        override fun areItemsTheSame(oldItem: DrugRecord, newItem: DrugRecord): Boolean {

            return oldItem.drugId == newItem.drugId
        }

        override fun areContentsTheSame(oldItem: DrugRecord, newItem: DrugRecord): Boolean {

            return oldItem.drugId == newItem.drugId && oldItem.dose==newItem.dose
        }
    }
}