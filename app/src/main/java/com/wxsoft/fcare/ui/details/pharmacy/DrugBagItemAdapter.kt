package com.wxsoft.fcare.ui.details.pharmacy

import androidx.lifecycle.LifecycleOwner
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.wxsoft.fcare.core.BR
import com.wxsoft.fcare.core.data.entity.drug.DrugPackageItem
import com.wxsoft.fcare.databinding.ItemPharmacyDrugBagItemBinding
import com.wxsoft.fcare.ui.details.pharmacy.drugcar.DrugCarViewModel

class DrugBagItemAdapter constructor(private val lifecycleOwner: LifecycleOwner, val viewModel: DrugCarViewModel) :
    RecyclerView.Adapter<DrugBagItemAdapter.ItemViewHolder>(){

    private val differ = AsyncListDiffer<DrugPackageItem>(this, DiffCallback)

    var items: List<DrugPackageItem> = emptyList()
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
            lifecycleOwner = this@DrugBagItemAdapter.lifecycleOwner
            executePendingBindings()

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val binding: ViewDataBinding =
            ItemPharmacyDrugBagItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    class ItemViewHolder(binding: ViewDataBinding) : androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root) {
        var binding: ViewDataBinding
            private set
        init {
            this.binding = binding
        }
    }


    object DiffCallback : DiffUtil.ItemCallback<DrugPackageItem>() {
        override fun areItemsTheSame(oldItem: DrugPackageItem, newItem: DrugPackageItem): Boolean {

            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DrugPackageItem, newItem: DrugPackageItem): Boolean {

            return oldItem.id == newItem.id
        }
    }

}