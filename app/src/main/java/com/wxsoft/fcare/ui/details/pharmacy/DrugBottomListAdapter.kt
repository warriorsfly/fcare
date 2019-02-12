package com.wxsoft.fcare.ui.details.pharmacy

import android.arch.lifecycle.LifecycleOwner
import android.databinding.ViewDataBinding
import android.support.v7.recyclerview.extensions.AsyncListDiffer
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.wxsoft.fcare.core.BR
import com.wxsoft.fcare.core.data.entity.drug.Drug
import com.wxsoft.fcare.databinding.ItemPharmacyDrugBottomItemBinding

class DrugBottomListAdapter constructor(private val lifecycleOwner: LifecycleOwner, val viewModel: PharmacyViewModel) :
    RecyclerView.Adapter<DrugBottomListAdapter.ItemViewHolder>(){

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
            setVariable(BR.listener, viewModel)
            lifecycleOwner = this@DrugBottomListAdapter. lifecycleOwner
            executePendingBindings()

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val binding: ViewDataBinding =
            ItemPharmacyDrugBottomItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    class ItemViewHolder(binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
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