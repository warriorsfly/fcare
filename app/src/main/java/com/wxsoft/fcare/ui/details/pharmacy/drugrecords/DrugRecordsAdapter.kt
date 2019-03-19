package com.wxsoft.fcare.ui.details.pharmacy.drugrecords

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.wxsoft.fcare.BR
import com.wxsoft.fcare.core.data.entity.drug.DrugRecord
import com.wxsoft.fcare.databinding.ItemDrugRecordsItemsBinding
import com.wxsoft.fcare.databinding.ItemNewEmrDrugItemBinding

class DrugRecordsAdapter constructor(private val owner: LifecycleOwner,
                                     val viewModel: DrugRecordsViewModel?=null,
                                     private val isEmr:Boolean=false) :
    RecyclerView.Adapter<DrugRecordsAdapter.ItemViewHolder>(){

    private val differ = AsyncListDiffer<DrugRecord>(this, DiffCallback)

    var items: List<DrugRecord> = emptyList()
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
            executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {


        return if(!isEmr) ItemViewHolder.RecordViewHolder(ItemDrugRecordsItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            .apply {
                lifecycleOwner=owner
            }) else
            ItemViewHolder.EmrViewHolder(ItemNewEmrDrugItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                .apply {
                    lifecycleOwner=owner
                })
    }

    sealed class ItemViewHolder(open val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        class RecordViewHolder(override val  binding: ItemDrugRecordsItemsBinding):ItemViewHolder(binding)
        class EmrViewHolder(override val  binding: ItemNewEmrDrugItemBinding):ItemViewHolder(binding)
    }


    object DiffCallback : DiffUtil.ItemCallback<DrugRecord>() {
        override fun areItemsTheSame(oldItem: DrugRecord, newItem: DrugRecord): Boolean {

            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DrugRecord, newItem: DrugRecord): Boolean {

            return oldItem.id == newItem.id
        }
    }

}