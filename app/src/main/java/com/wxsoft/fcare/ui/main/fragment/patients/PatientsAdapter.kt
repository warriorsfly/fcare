package com.wxsoft.fcare.ui.main.fragment.patients


import android.arch.lifecycle.LifecycleOwner
import android.databinding.ViewDataBinding
import android.support.v7.recyclerview.extensions.AsyncListDiffer
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.wxsoft.fcare.BR
import com.wxsoft.fcare.data.entity.Patient
import com.wxsoft.fcare.databinding.LayoutItemPatientBinding


class PatientsAdapter constructor(private val lifecycleOwner: LifecycleOwner, val viewModel: PatientsViewModel): RecyclerView.Adapter< PatientsAdapter.ItemViewHolder>() {

    private val differ = AsyncListDiffer<Patient>(this, DiffCallback)

    var patients: List<Patient> = emptyList()
        set(value) {
            field = value
            differ.submitList(value)
        }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        holder.binding.apply {

            setVariable(BR.patient,differ.currentList[position])
            setVariable(BR.listener,viewModel)
            setLifecycleOwner(lifecycleOwner)
            executePendingBindings()

        }



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val binding: ViewDataBinding = LayoutItemPatientBinding.inflate(LayoutInflater.from(parent.context),parent,  false)
        return ItemViewHolder(binding)
    }

    class ItemViewHolder(binding:ViewDataBinding ) : RecyclerView.ViewHolder(binding.root) {
        var binding: ViewDataBinding
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