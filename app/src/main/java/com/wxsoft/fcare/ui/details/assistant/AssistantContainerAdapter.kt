package com.wxsoft.fcare.ui.details.assistant

import androidx.lifecycle.LifecycleOwner
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.BR
import com.wxsoft.fcare.core.data.entity.lis.LisRecord
import com.wxsoft.fcare.databinding.ItemAssistantAddOrNoneBinding
import com.wxsoft.fcare.databinding.ItemAssistantProjectContainerBinding
import kotlinx.android.synthetic.main.item_assistant_project_container.view.*

class AssistantContainerAdapter constructor(private val lifecycleOwner: LifecycleOwner, val viewModel: AssistantExaminationViewModel) :
    RecyclerView.Adapter<AssistantContainerAdapter.ItemViewHolder>() {

    private val differ = AsyncListDiffer<LisRecord>(this, DiffCallback)

    var items: List<LisRecord> = emptyList()
        set(value) {
            field = value
            differ.submitList(value)
        }

    override fun getItemCount(): Int {
        return differ.currentList.size + 1
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        holder.binding.apply {
            if (position!= differ.currentList.size){
                val record = differ.currentList[position]
                setVariable(BR.item, record)
                val adapter = AssistantDetailsAdapter(this@AssistantContainerAdapter.lifecycleOwner, viewModel)
                adapter.items = record.lisRecordDetails
                root.details_list.adapter = adapter
            }
            setVariable(BR.listener, viewModel)
            lifecycleOwner = this@AssistantContainerAdapter.lifecycleOwner
            executePendingBindings()
        }
    }


    override fun getItemViewType(position: Int): Int {

        return when (position) {
            differ.currentList.size -> {
                R.layout.item_assistant_add_or_none
            }
            else -> {
                R.layout.item_assistant_project_container
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        when (viewType) {
            R.layout.item_assistant_add_or_none -> {
                val binding = ItemAssistantAddOrNoneBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return ItemViewHolder(binding)
            }
            R.layout.item_assistant_project_container -> {
                val binding = ItemAssistantProjectContainerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return ItemViewHolder(binding)
            }
            else -> {
                val binding: ViewDataBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_assistant_project_container,
                    parent,
                    false
                )
                return ItemViewHolder(binding)
            }
        }
    }

    class ItemViewHolder(binding: ViewDataBinding) : androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root) {
        var binding: ViewDataBinding
            private set
        init {
            this.binding = binding
        }
    }


    object DiffCallback : DiffUtil.ItemCallback<LisRecord>() {
        override fun areItemsTheSame(oldItem: LisRecord, newItem: LisRecord): Boolean {

            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: LisRecord, newItem: LisRecord): Boolean {

            return oldItem.id == newItem.id
        }
    }
}