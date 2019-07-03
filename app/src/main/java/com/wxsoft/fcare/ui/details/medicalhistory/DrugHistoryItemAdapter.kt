package com.wxsoft.fcare.ui.details.medicalhistory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wxsoft.fcare.core.data.entity.drug.DrugHistory
import com.wxsoft.fcare.core.data.entity.previoushistory.History2
import com.wxsoft.fcare.databinding.ItemDrugHistoryBinding


class DrugHistoryItemAdapter constructor(private val owner: LifecycleOwner, val viewModel: MedicalHistoryViewModel) :
    ListAdapter<History2,DrugHistoryItemAdapter.ItemViewHolder>(DiffCallback) {

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        holder.binding.apply {
            item=getItem(position)
            lifecycleOwner=owner
            viewModel = this@DrugHistoryItemAdapter.viewModel
            executePendingBindings()
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        return  ItemViewHolder(ItemDrugHistoryBinding.inflate(inflater, parent, false))
    }



    class ItemViewHolder(val binding: ItemDrugHistoryBinding) : RecyclerView.ViewHolder(binding.root){
    }

    object DiffCallback : DiffUtil.ItemCallback<History2>() {
        override fun areItemsTheSame(oldItem: History2, newItem: History2): Boolean {

            return oldItem.drugId == newItem.drugId
        }

        override fun areContentsTheSame(oldItem: History2, newItem: History2): Boolean {

            return oldItem.drugId == newItem.drugId && oldItem.dose==newItem.dose
        }
    }
}