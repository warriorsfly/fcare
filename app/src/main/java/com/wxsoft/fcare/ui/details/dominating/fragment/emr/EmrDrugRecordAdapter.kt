package com.wxsoft.fcare.ui.details.dominating.fragment.emr

import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.wxsoft.fcare.core.data.entity.drug.DrugRecord
import com.wxsoft.fcare.data.dictionary.ActionRes
import com.wxsoft.fcare.databinding.ItemEmrDrugRecordBinding
import com.wxsoft.fcare.ui.EmrEventAction

class EmrDrugRecordAdapter (private val owner: LifecycleOwner,private val  action: EmrEventAction?) :
    ListAdapter<DrugRecord, EmrDrugRecordAdapter.ItemViewHolder>(DiffCallback) {


    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.binding.apply {
            root.setOnClickListener { action?.onOpen(ActionRes.ActionType.给药, getItem(position).id) }
            record=getItem(position)
            executePendingBindings()
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val inflater = LayoutInflater.from(parent.context)

        return ItemViewHolder(
            ItemEmrDrugRecordBinding.inflate(inflater, parent, false).apply {
                lifecycleOwner=owner
            }
        )
    }
    object DiffCallback : DiffUtil.ItemCallback<DrugRecord>() {
        override fun areItemsTheSame(oldItem: DrugRecord, newItem: DrugRecord): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: DrugRecord, newItem: DrugRecord): Boolean {
            return oldItem==newItem
        }
    }

    class ItemViewHolder(binding: ItemEmrDrugRecordBinding) : androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root) {
        var binding: ItemEmrDrugRecordBinding
            private set

        init {
            this.binding = binding
        }
    }
}