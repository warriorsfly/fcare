package com.wxsoft.fcare.ui.details.thrombolysis

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wxsoft.fcare.core.data.entity.drug.DrugHistory
import com.wxsoft.fcare.core.data.entity.drug.DrugRecord
import com.wxsoft.fcare.databinding.ItemThrombolyDrugBinding

class ThrombolysisDrugsAdapter constructor(private val owner: LifecycleOwner, val viewModel: ThrombolysisViewModel) :
    ListAdapter<DrugRecord, ThrombolysisDrugsAdapter.ItemViewHolder>(DiffCallback) {

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        holder.binding.apply {
            item=getItem(position)
            lifecycleOwner=owner
            viewModel = this@ThrombolysisDrugsAdapter.viewModel
            executePendingBindings()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        return  ItemViewHolder(ItemThrombolyDrugBinding.inflate(inflater, parent, false))
    }



    class ItemViewHolder(val binding: ItemThrombolyDrugBinding) : RecyclerView.ViewHolder(binding.root){
    }

    object DiffCallback : DiffUtil.ItemCallback<DrugRecord>() {
        override fun areItemsTheSame(oldItem: DrugRecord, newItem: DrugRecord): Boolean {

            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DrugRecord, newItem: DrugRecord): Boolean {

            return oldItem.id == newItem.id && oldItem.dose==newItem.dose
        }
    }
}