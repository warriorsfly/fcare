package com.wxsoft.fcare.ui.details.diagnose.record

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.wxsoft.fcare.core.data.entity.DiagnoseRecord
import com.wxsoft.fcare.databinding.ItemDiagnoseRecordListItemBinding

class DiagnoseRecordAdapter constructor(private val owner: LifecycleOwner, val viewModel: DiagnoseRecordViewModel) :
    RecyclerView.Adapter<DiagnoseRecordAdapter.ItemViewHolder>(){

    private val differ = AsyncListDiffer<DiagnoseRecord>(this, DiffCallback)

    var items: List<DiagnoseRecord> = emptyList()
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
            viewModel=this@DiagnoseRecordAdapter.viewModel
            if(diagnoseRecordsDetailsList.adapter==null){
                diagnoseRecordsDetailsList.adapter=
                    DiagnoseRecordDetailsAdapter(owner,this@DiagnoseRecordAdapter.viewModel).apply {
                        items = differ.currentList[position].items
                    }
            }
            lifecycleOwner = owner
            executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val binding: ItemDiagnoseRecordListItemBinding =
            ItemDiagnoseRecordListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    class ItemViewHolder(binding: ItemDiagnoseRecordListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        var binding: ItemDiagnoseRecordListItemBinding
            private set
        init {
            this.binding = binding
        }
    }


    object DiffCallback : DiffUtil.ItemCallback<DiagnoseRecord>() {
        override fun areItemsTheSame(oldItem: DiagnoseRecord, newItem: DiagnoseRecord): Boolean {

            return oldItem.typeId == newItem.typeId
        }

        override fun areContentsTheSame(oldItem: DiagnoseRecord, newItem: DiagnoseRecord): Boolean {

            return oldItem.items == newItem.items
        }
    }


}