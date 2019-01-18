package com.wxsoft.emergency.ui.main.fragment.patients


import android.arch.lifecycle.LifecycleOwner
import android.arch.paging.PagedListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.wxsoft.fcare.core.data.entity.Patient
import com.wxsoft.fcare.core.result.Event
import com.wxsoft.fcare.databinding.LayoutItemPatientBinding


class PatientsAdapter constructor(private val lifecycleOwner: LifecycleOwner, val viewModel: PatientsViewModel):
    PagedListAdapter<Patient, PatientsAdapter.ItemViewHolder>(DiffCallback) {

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        holder.binding.apply {
            val p=getItem(position)
            patient=p
            p?.let {
                pat->
                root.setOnClickListener { viewModel.onOpen(pat.id) }
            }

            setLifecycleOwner(lifecycleOwner)
            executePendingBindings()

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val binding = LayoutItemPatientBinding.inflate(LayoutInflater.from(parent.context),parent,  false)
        return ItemViewHolder(binding)
    }

    class ItemViewHolder(binding:LayoutItemPatientBinding ) : RecyclerView.ViewHolder(binding.root) {
        var binding: LayoutItemPatientBinding
            private set

        init {
            this.binding = binding
        }
    }


    object DiffCallback : DiffUtil.ItemCallback<Patient>() {
        override fun areItemsTheSame(oldItem: Patient, newItem: Patient): Boolean {

            return  oldItem.id==newItem.id
        }

        override fun areContentsTheSame(oldItem: Patient, newItem: Patient): Boolean {

            return oldItem.id==newItem.id
        }
    }
}