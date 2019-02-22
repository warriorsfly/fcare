package com.wxsoft.fcare.ui.details.medicalhistory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.data.entity.Dictionary
import com.wxsoft.fcare.databinding.ItemMedicalHistoryMultiSelectBinding
import com.wxsoft.fcare.databinding.ItemMedicalHistoryOnlyNameBinding


class MedicalHistoryItemAdapter constructor(private val owner: LifecycleOwner, val viewModel: MedicalHistoryViewModel) :
    RecyclerView.Adapter<MedicalHistoryItemAdapter.ItemViewHolder>() {

    private val differ = AsyncListDiffer<Dictionary>(this, DiffCallback)

    var section: Int = 0
        set(value) {
            field = value
            when (value) {
                1 -> {
                    viewModel.historyItems.observe(owner, Observer { history1s = it ?: emptyList() })
                }
                3 -> {
                    viewModel.providerItems.observe(owner, Observer { history1s = it ?: emptyList() })
                }
            }
        }

    var history1s: List<Dictionary> = emptyList()
        set(value) {
            field = value
            differ.submitList(value)
        }
    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun getItemViewType(position: Int): Int {
        return when(section){
            1->R.layout.item_medical_history_multi_select
            2,3-> R.layout.item_medical_history_only_name
            else-> throw IllegalArgumentException("error index $section")
        }
    }
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        when(holder){
            is ItemViewHolder.MedicalHistoryViewHolder->{
                holder.binding.apply {
                    item=differ.currentList[position]
                    lifecycleOwner=owner
                    executePendingBindings()
                }
            }

            is ItemViewHolder.ProviderViewHolder->{
                holder.binding.apply {
                    item=differ.currentList[position]
                    listener=viewModel
                    lifecycleOwner=owner
                    executePendingBindings()
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        return when(viewType){
            R.layout.item_medical_history_multi_select->
                ItemViewHolder.MedicalHistoryViewHolder(
                    ItemMedicalHistoryMultiSelectBinding.inflate(inflater, parent, false))

            R.layout.item_medical_history_only_name->
                ItemViewHolder.ProviderViewHolder(
                    ItemMedicalHistoryOnlyNameBinding.inflate(inflater, parent, false))

            else-> throw IllegalArgumentException("error item type $viewType")
        }
    }



    sealed class ItemViewHolder(binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

        //
        class MedicalHistoryViewHolder(
            val binding: ItemMedicalHistoryMultiSelectBinding
        ) : ItemViewHolder(binding)

        //基本信息
        class ProviderViewHolder(
            val binding: ItemMedicalHistoryOnlyNameBinding
        ) : ItemViewHolder(binding)

    }

    object DiffCallback : DiffUtil.ItemCallback<Dictionary>() {
        override fun areItemsTheSame(oldItem: Dictionary, newItem: Dictionary): Boolean {

            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Dictionary, newItem: Dictionary): Boolean {

            return oldItem.id == newItem.id
        }
    }
}