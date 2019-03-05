package com.wxsoft.fcare.ui.details.vitalsigns.records

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.wxsoft.fcare.core.BR
import com.wxsoft.fcare.core.data.entity.VitalSignRecord
import com.wxsoft.fcare.databinding.ItemVitalSignsRecordListItemBinding
import kotlinx.android.synthetic.main.item_vital_signs_record_list_item.view.*

class VitalRecordListAdapter constructor(private val lifecycleOwner: LifecycleOwner, val viewModel: VitalSignsRecordViewModel) :
    RecyclerView.Adapter<VitalRecordListAdapter.ItemViewHolder>(){

    private val differ = AsyncListDiffer<VitalSignRecord>(this, DiffCallback)

    var items: List<VitalSignRecord> = emptyList()
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
            if (root.vital_records_details_list.adapter == null){
                var adapter = VitalSignsDetailItemAdapter(this@VitalRecordListAdapter.lifecycleOwner,viewModel)
                adapter.items = differ.currentList[position].items
                root.vital_records_details_list.adapter = adapter
            }
            lifecycleOwner = this@VitalRecordListAdapter.lifecycleOwner
            executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val binding: ViewDataBinding =
            ItemVitalSignsRecordListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    class ItemViewHolder(binding: ViewDataBinding) : androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root) {
        var binding: ViewDataBinding
            private set
        init {
            this.binding = binding
        }
    }


    object DiffCallback : DiffUtil.ItemCallback<VitalSignRecord>() {
        override fun areItemsTheSame(oldItem: VitalSignRecord, newItem: VitalSignRecord): Boolean {

            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: VitalSignRecord, newItem: VitalSignRecord): Boolean {

            return oldItem.id == newItem.id
        }
    }



}