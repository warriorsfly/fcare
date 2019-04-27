package com.wxsoft.fcare.ui.details.assistant

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.wxsoft.fcare.core.data.entity.lis.LisRecordItem
import com.wxsoft.fcare.databinding.ItemAssistantSubDetailsBinding

class LisAdapter constructor(private val owner: LifecycleOwner, val viewModel: AssistantExaminationViewModel) :
    ListAdapter<LisRecordItem, LisAdapter.ItemViewHolder>(DiffCallback){

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        holder.binding.apply {
            val yy = getItem(position)
            item = yy
            listener = this@LisAdapter.viewModel
            executePendingBindings()

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val binding = ItemAssistantSubDetailsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            .apply {
                lifecycleOwner = owner
            }
        return ItemViewHolder(binding)
    }

    class ItemViewHolder(binding: ItemAssistantSubDetailsBinding) : androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root) {
        var binding: ItemAssistantSubDetailsBinding
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