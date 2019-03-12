package com.wxsoft.fcare.ui.details.dominating.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.data.entity.Patient
import com.wxsoft.fcare.databinding.ItemImageRemoteBinding
import com.wxsoft.fcare.databinding.ItemNewImageBinding
import com.wxsoft.fcare.databinding.ItemPatientInTaskBinding
import com.wxsoft.fcare.databinding.ItemTaskNoPatientBinding

class PatientInTaskAdapter constructor(private val owner: LifecycleOwner,private val detailPatient:(Patient)->Unit) :
    RecyclerView.Adapter<PatientInTaskAdapter.ItemViewHolder>() {


    private val differ = AsyncListDiffer<Any>(this, DiffCallback)

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    var patients: List<Patient> = emptyList()
        set(value) {
            field = value
            differ.submitList(buildMergedList(value))
        }
    private fun buildMergedList(
        ps: List<Patient> =patients): List<Any> {
        val merged = mutableListOf<Any>()
        if(ps.isNotEmpty()){
            merged.addAll(ps)
        }else{
            merged+=EmptyItem
        }
        return merged
    }



    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        when (holder) {
            is ItemViewHolder.EmptyViewHolder -> holder.binding.apply {
                executePendingBindings()
            }

            is ItemViewHolder.PatientViewHolder -> holder.binding.apply {
                patient =differ.currentList[position] as Patient
                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            R.layout.item_task_no_patient -> ItemViewHolder.EmptyViewHolder(
                ItemTaskNoPatientBinding.inflate(inflater, parent, false).apply {
                    lifecycleOwner=owner
                }
            )
            R.layout.item_patient_in_task -> ItemViewHolder.PatientViewHolder(
                ItemPatientInTaskBinding.inflate(inflater, parent, false).apply {

                    lifecycleOwner=owner
                },detailPatient
            )
            else -> throw IllegalStateException("Unknown viewType $viewType")
        }
    }


    override fun getItemViewType(position: Int): Int {
        return when (differ.currentList[position]) {
            is EmptyItem -> R.layout.item_task_no_patient
            is Patient-> R.layout.item_patient_in_task

            else -> throw IllegalStateException("Unknown viewType at  $position")
        }
    }


    object DiffCallback : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
            return when {
                oldItem == EmptyItem && newItem == EmptyItem -> true
                oldItem is Patient && newItem is Patient -> newItem.id == oldItem.id
                else -> false
            }
        }

        override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
            return when {
                oldItem is Patient && newItem is Patient ->
                    newItem.name == oldItem.name
                else -> false
            }
        }

    }

    sealed class ItemViewHolder(binding: ViewDataBinding) : androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root) {

        class EmptyViewHolder(
            val binding: ItemTaskNoPatientBinding
        ) : ItemViewHolder(binding)

        class PatientViewHolder(
            val binding: ItemPatientInTaskBinding,
            val showDetail:(Patient)->Unit
        ) : ItemViewHolder(binding){
            init {
                binding.apply {
                    root.setOnClickListener {
                        patient?.let{showDetail(it)}
                    }

                }
            }
        }

        class HeaderViewHolder(
            val binding: ItemImageRemoteBinding
        ) : ItemViewHolder(binding)

        class FooterViewHolder(
            val binding: ItemNewImageBinding
        ) : ItemViewHolder(binding)
    }


    object EmptyItem
//    object Header
//    object Footer
}



