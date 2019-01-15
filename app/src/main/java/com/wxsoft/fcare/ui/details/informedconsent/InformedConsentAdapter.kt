package com.wxsoft.fcare.ui.details.informedconsent

import android.arch.lifecycle.LifecycleOwner
import android.databinding.ViewDataBinding
import android.support.v7.recyclerview.extensions.AsyncListDiffer
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.wxsoft.fcare.core.BR
import com.wxsoft.fcare.core.data.entity.drug.DrugPackage
import com.wxsoft.fcare.databinding.ItemInformedConsentBinding
import com.wxsoft.fcare.databinding.ItemPharmacyDrugBagBinding
import com.wxsoft.fcare.ui.details.pharmacy.DrugBagAdapter
import com.wxsoft.fcare.ui.details.pharmacy.DrugBagItemAdapter
import com.wxsoft.fcare.ui.details.pharmacy.PharmacyViewModel
import kotlinx.android.synthetic.main.item_pharmacy_drug_bag.view.*

class InformedConsentAdapter constructor(private val lifecycleOwner: LifecycleOwner, val viewModel: InformedConsentViewModel) :
    RecyclerView.Adapter<InformedConsentAdapter.ItemViewHolder>(){

    override fun getItemCount(): Int {
        return 3
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        holder.binding.apply {
            setVariable(BR.viewmodel, viewModel)
            setLifecycleOwner(lifecycleOwner)
            executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val binding: ViewDataBinding =
            ItemInformedConsentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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