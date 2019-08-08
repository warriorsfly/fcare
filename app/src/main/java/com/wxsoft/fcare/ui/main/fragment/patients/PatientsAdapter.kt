package com.wxsoft.fcare.ui.main.fragment.patients


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.wxsoft.fcare.core.data.entity.Patient
import com.wxsoft.fcare.databinding.LayoutItemPatientBinding


class PatientsAdapter constructor(private val owner: LifecycleOwner, val viewModel: PatientsViewModel,private val click:(Patient)->Unit):
    PagedListAdapter<Patient, PatientsAdapter.ItemViewHolder>(DiffCallback) {

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        viewModel.noPatientsShow.set(false)
        holder.binding.apply {
            val p= getItem(position)
            patient =p
            p?.let {
                    pat->
                root.setOnClickListener { viewModel.onOpen(pat.id) }
//                root.setOnLongClickListener {
//                        viewModel.deletePatient(pat)
//                    return@setOnLongClickListener true
//                }
            }



            lifecycleOwner = owner
            executePendingBindings()

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val binding = LayoutItemPatientBinding.inflate(LayoutInflater.from(parent.context),parent,  false)
        return ItemViewHolder(binding).apply {

            binding.root.setOnLongClickListener {
                binding.patient?.let(click)
                return@setOnLongClickListener true
            }
        }
    }

    class ItemViewHolder(binding:LayoutItemPatientBinding ) : androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root) {
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