package com.wxsoft.fcare.ui.main.fragment.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wxsoft.fcare.core.data.entity.Hospital
import com.wxsoft.fcare.databinding.ItemChangeHospitalBinding


class HospitalsAdapter constructor(private val owner: LifecycleOwner,private val viewModel: UserProfileViewModel) :
    ListAdapter<Hospital, HospitalsAdapter.ItemViewHolder>(DiffCallback){



    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        holder.binding.apply {
            val message=getItem(position)
            hospiatl = message
            listener = this@HospitalsAdapter.viewModel
            executePendingBindings()
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val binding = ItemChangeHospitalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            .apply {
                lifecycleOwner = owner
            }
        return ItemViewHolder(binding)
    }

    class ItemViewHolder(val binding: ItemChangeHospitalBinding) : RecyclerView.ViewHolder(binding.root)


    object DiffCallback : DiffUtil.ItemCallback<Hospital>() {
        override fun areItemsTheSame(oldItem: Hospital, newItem: Hospital): Boolean {

            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Hospital, newItem: Hospital): Boolean {

            return oldItem.name == newItem.name
        }
    }


}