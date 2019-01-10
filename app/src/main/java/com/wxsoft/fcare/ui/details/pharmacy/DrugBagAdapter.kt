package com.wxsoft.fcare.ui.details.pharmacy

import android.arch.lifecycle.LifecycleOwner
import android.databinding.ViewDataBinding
import android.support.v7.recyclerview.extensions.AsyncListDiffer
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.wxsoft.fcare.core.BR
import com.wxsoft.fcare.core.data.entity.drug.DrugPackage
import com.wxsoft.fcare.databinding.ItemPharmacyDrugBagBinding
import kotlinx.android.synthetic.main.item_pharmacy_drug_bag.view.*

class DrugBagAdapter constructor(private val lifecycleOwner: LifecycleOwner, val viewModel: PharmacyViewModel) :
    RecyclerView.Adapter<DrugBagAdapter.ItemViewHolder>(){

    private val differ = AsyncListDiffer<DrugPackage>(this, DiffCallback)

    var items: List<DrugPackage> = emptyList()
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
            if (root.drug_bag_item_rv.adapter == null){
                var adapter = DrugBagItemAdapter(lifecycleOwner,viewModel)
                adapter.items = differ.currentList[position].drugPackageDetails
                root.drug_bag_item_rv.adapter = adapter
            }
            setVariable(BR.listener, viewModel)
            setLifecycleOwner(lifecycleOwner)
            executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val binding: ViewDataBinding =
            ItemPharmacyDrugBagBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    class ItemViewHolder(binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        var binding: ViewDataBinding
            private set
        init {
            this.binding = binding
        }
    }


    object DiffCallback : DiffUtil.ItemCallback<DrugPackage>() {
        override fun areItemsTheSame(oldItem: DrugPackage, newItem: DrugPackage): Boolean {

            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DrugPackage, newItem: DrugPackage): Boolean {

            return oldItem.id == newItem.id
        }
    }

}