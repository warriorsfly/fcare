package com.wxsoft.fcare.ui.details.pharmacy.drugcar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.wxsoft.fcare.core.BR
import com.wxsoft.fcare.core.data.entity.drug.Drug
import com.wxsoft.fcare.databinding.ItemDrugCarItemBinding

class DrugCarAdapter constructor(private val lifecycleOwner: LifecycleOwner, val viewModel: DrugCarViewModel) :
    RecyclerView.Adapter<DrugCarAdapter.ItemViewHolder>() {

    private val differ = AsyncListDiffer<Drug>(this, DiffCallback)

    var items: List<Drug> = emptyList()
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
            lifecycleOwner = this@DrugCarAdapter.lifecycleOwner
            executePendingBindings()

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val binding: ViewDataBinding =
            ItemDrugCarItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    class ItemViewHolder(binding: ViewDataBinding) : androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root) {
        var binding: ViewDataBinding
            private set
        init {
            this.binding = binding
        }
    }


    object DiffCallback : DiffUtil.ItemCallback<Drug>() {
        override fun areItemsTheSame(oldItem: Drug, newItem: Drug): Boolean {

            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Drug, newItem: Drug): Boolean {

            return oldItem.id == newItem.id
        }
    }


}