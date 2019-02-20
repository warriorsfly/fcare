package com.wxsoft.fcare.ui.details.dominating.fragment.emr

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.data.entity.Complain
import com.wxsoft.fcare.core.data.entity.drug.DrugRecord
import com.wxsoft.fcare.data.dictionary.ActionRes
import com.wxsoft.fcare.databinding.ItemEmrComplaintRecordBinding
import com.wxsoft.fcare.databinding.ItemEmrDrugRecordBinding
import com.wxsoft.fcare.ui.EmrEventAction

class EmrLineLayoutAdapter constructor(private val lifecycleOwner: LifecycleOwner,private val  action: EmrEventAction?) :
    androidx.recyclerview.widget.RecyclerView.Adapter<EmrLineLayoutAdapter.ItemViewHolder>() {

    private val differ = AsyncListDiffer<Any>(this, EmrLineLayoutAdapter.DiffCallback)


    var items: List<Any> = emptyList()
        set(value) {
            field = value
            differ.submitList(value)
        }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        when (holder) {
            is ItemViewHolder.DrugRecordViewHolder ->holder.binding.apply {
                root.setOnClickListener { action?.onOpen(ActionRes.ActionType.给药, (differ.currentList[position]as DrugRecord).id) }
                record=differ.currentList[position] as DrugRecord
                executePendingBindings()
            }
            is ItemViewHolder.ComplaintViewHolder ->holder.binding.apply {
                root.setOnClickListener { action?.onOpen(ActionRes.ActionType.主诉及症状, (differ.currentList[position]as Complain).id) }
                record=differ.currentList[position] as Complain
                executePendingBindings()
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item=differ.currentList[position]
        return when (item) {
            is DrugRecord-> R.layout.item_emr_drug_record
            is Complain->R.layout.item_emr_complaint_record
            else->R.layout.item_emr_complaint_record
//            else -> throw IllegalStateException("Unknown view type at position $position")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            R.layout.item_emr_drug_record -> ItemViewHolder.DrugRecordViewHolder(
                ItemEmrDrugRecordBinding.inflate(inflater, parent, false)
            )
            R.layout.item_emr_complaint_record -> ItemViewHolder.ComplaintViewHolder(
                ItemEmrComplaintRecordBinding.inflate(inflater, parent, false)
            )
            else -> throw IllegalStateException("Unknown viewType $viewType")
        }
    }

    sealed class ItemViewHolder(binding: ViewDataBinding) : androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root) {
        //用药
        class DrugRecordViewHolder(
            val binding: ItemEmrDrugRecordBinding
        ) : ItemViewHolder(binding)

        //基本信息
        class ComplaintViewHolder(
            val binding: ItemEmrComplaintRecordBinding
        ) : ItemViewHolder(binding)
    }


    object DiffCallback : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
            when{
                oldItem is DrugRecord && newItem is DrugRecord ->{return oldItem.id == newItem.id}
                oldItem is Complain && newItem is Complain -> {return oldItem.id == newItem.id}
                else -> return true
            }
        }

        override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {

            when{
                oldItem is DrugRecord && newItem is DrugRecord ->{return oldItem == newItem}
                oldItem is Complain && newItem is Complain -> {return oldItem == newItem}
                else -> return true
            }

        }
    }

}