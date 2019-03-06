package com.wxsoft.fcare.ui.details.diagnose.record

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.wxsoft.fcare.core.data.entity.Diagnosis
import com.wxsoft.fcare.databinding.ItemDiagnoseRecordDetailsItemBinding

class DiagnoseRecordDetailsAdapter constructor(private val owner: LifecycleOwner, val viewModel: DiagnoseRecordViewModel) :
    RecyclerView.Adapter<DiagnoseRecordDetailsAdapter.ItemViewHolder>(){

    private val differ = AsyncListDiffer<Diagnosis>(this, DiffCallback)

    var items: List<Diagnosis> = emptyList()
        set(value) {
            field = value
            differ.submitList(value)
        }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        holder.binding.apply {
            item=differ.currentList[position]
            viewModel = this@DiagnoseRecordDetailsAdapter.viewModel
            executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val binding =
            ItemDiagnoseRecordDetailsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                .apply {
                    lifecycleOwner=owner
                }
        return ItemViewHolder(binding)
    }

    class ItemViewHolder(binding: ItemDiagnoseRecordDetailsItemBinding) : RecyclerView.ViewHolder(binding.root) {
        var binding: ItemDiagnoseRecordDetailsItemBinding
            private set
        init {
            this.binding = binding
        }
    }


    object DiffCallback : DiffUtil.ItemCallback<Diagnosis>() {
        override fun areItemsTheSame(oldItem: Diagnosis, newItem: Diagnosis): Boolean {

            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Diagnosis, newItem: Diagnosis): Boolean {

            return oldItem.id == newItem.id
        }
    }
}