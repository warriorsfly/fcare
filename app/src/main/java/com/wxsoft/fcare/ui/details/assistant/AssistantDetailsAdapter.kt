package com.wxsoft.fcare.ui.details.assistant

import android.arch.lifecycle.LifecycleOwner
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.recyclerview.extensions.AsyncListDiffer
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.BR
import com.wxsoft.fcare.core.data.entity.lis.LisRecordItem
import com.wxsoft.fcare.databinding.ItemAssistantSubDetailsBinding
import com.wxsoft.fcare.databinding.ItemAssistantSubtitleBinding

class AssistantDetailsAdapter constructor(private val lifecycleOwner: LifecycleOwner, val viewModel: AssistantExaminationViewModel): RecyclerView.Adapter< AssistantDetailsAdapter.ItemViewHolder>() {

    private val differ = AsyncListDiffer<LisRecordItem>(this, DiffCallback)
    var items: List<LisRecordItem> = emptyList()
        set(value) {
            field = value
            differ.submitList(value)
        }

    override fun getItemCount(): Int {
        return if (differ.currentList.size > 0)differ.currentList.size+1 else 0
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.binding.apply {
            if (position != 0){
                setVariable(BR.item, differ.currentList[position-1])
                setVariable(BR.listener, viewModel)
            }
            lifecycleOwner = this@AssistantDetailsAdapter. lifecycleOwner
            executePendingBindings()
        }


    }

    override fun getItemViewType(position: Int): Int {
        var type: Int = 0
        when (position) {
            0 -> {
                type = R.layout.item_assistant_subtitle
            }
            else ->{
                type = R.layout.item_assistant_sub_details
            }
        }
        return type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        when (viewType) {
            R.layout.item_assistant_subtitle -> {
                val binding = ItemAssistantSubtitleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return ItemViewHolder(binding)
            }
            R.layout.item_assistant_sub_details -> {
                val binding = ItemAssistantSubDetailsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return ItemViewHolder(binding)
            }
            else -> {
                val binding: ViewDataBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_assistant_sub_details,
                    parent,
                    false
                )

                return ItemViewHolder(binding)
            }
        }
    }


    class ItemViewHolder(binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        var binding: ViewDataBinding
            private set

        init {
            this.binding = binding
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<LisRecordItem>() {
        override fun areItemsTheSame(oldItem: LisRecordItem, newItem: LisRecordItem): Boolean {

            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: LisRecordItem, newItem: LisRecordItem): Boolean {

            return oldItem.id == newItem.id
        }
    }
}