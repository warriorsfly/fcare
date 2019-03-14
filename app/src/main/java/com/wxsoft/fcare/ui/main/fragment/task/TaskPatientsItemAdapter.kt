package com.wxsoft.fcare.ui.main.fragment.task

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.wxsoft.fcare.core.data.entity.Patient
import com.wxsoft.fcare.databinding.ItemTaskListPatientItemBinding

class TaskPatientsItemAdapter constructor(private val owner: LifecycleOwner, val viewModel: TaskViewModel):
    ListAdapter<Patient, TaskPatientsItemAdapter.ItemViewHolder>(DiffCallback) {

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        holder.binding.apply {
            val p= getItem(position)
            patient =p
            lifecycleOwner = owner
            executePendingBindings()

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val binding = ItemTaskListPatientItemBinding.inflate(LayoutInflater.from(parent.context),parent,  false)
        return ItemViewHolder(binding)
    }

    class ItemViewHolder(binding: ItemTaskListPatientItemBinding) : androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root) {
        var binding: ItemTaskListPatientItemBinding
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