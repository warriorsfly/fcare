package com.wxsoft.fcare.ui.details.assistant

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.wxsoft.fcare.core.data.entity.lis.LisRecord
import com.wxsoft.fcare.databinding.ItemAssistantDateDetailsBinding

class LisRecordAdapter constructor(private val owner: LifecycleOwner, val viewModel: AssistantExaminationViewModel) :
    ListAdapter<LisRecord, LisRecordAdapter.ItemViewHolder>(DiffCallback){

    lateinit var adapter: LisRecordItemAdapter
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        holder.binding.apply {
            val yy = getItem(position)
            item = yy
            listener = this@LisRecordAdapter.viewModel

            (list.adapter as? LisRecordItemAdapter)?.submitList(yy.lisReoprtaRecordDetails)
            executePendingBindings()

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val binding = ItemAssistantDateDetailsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            .apply {
                lifecycleOwner = owner
                list.adapter= LisRecordItemAdapter(owner,viewModel)
            }
        return ItemViewHolder(binding)
    }

    class ItemViewHolder(binding: ItemAssistantDateDetailsBinding) : androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root) {
        var binding: ItemAssistantDateDetailsBinding
            private set

        init {
            this.binding = binding
        }
    }


    object DiffCallback : DiffUtil.ItemCallback<LisRecord>() {
        override fun areItemsTheSame(oldItem: LisRecord, newItem: LisRecord): Boolean {

            return oldItem.lisReoprtaRecordDetails == newItem.lisReoprtaRecordDetails
        }

        override fun areContentsTheSame(oldItem: LisRecord, newItem: LisRecord): Boolean {

            return oldItem.lisReoprtaRecordDetails == newItem.lisReoprtaRecordDetails
        }
    }

}