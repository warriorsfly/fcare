package com.wxsoft.fcare.ui.main.fragment.patients.searchpatients

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.wxsoft.fcare.core.data.entity.Patient
import com.wxsoft.fcare.databinding.LayoutItemPatientBinding

class SearchPatientsAdapter constructor(private val owner: LifecycleOwner, val viewModel: SearchPatientsViewModel):
    RecyclerView.Adapter< SearchPatientsAdapter.ItemViewHolder>() {

    private val differ = AsyncListDiffer<Patient>(this, DiffCallback)

    var items: List<Patient> = emptyList()
        set(value) {
            field = value
            differ.submitList(value)
        }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        holder.binding.apply {
            val p= differ.currentList[position]
            patient =p
            p?.let {
                    pat->
                root.setOnClickListener { viewModel.onOpen(pat.id) }
            }

            lifecycleOwner = owner
            executePendingBindings()

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val binding = LayoutItemPatientBinding.inflate(LayoutInflater.from(parent.context),parent,  false)
        return ItemViewHolder(binding)
    }

    class ItemViewHolder(binding: LayoutItemPatientBinding) : androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root) {
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