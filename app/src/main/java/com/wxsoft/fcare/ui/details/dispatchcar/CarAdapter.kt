package com.wxsoft.fcare.ui.details.dispatchcar

import android.arch.lifecycle.LifecycleOwner
import android.databinding.ViewDataBinding
import android.support.v7.recyclerview.extensions.AsyncListDiffer
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.wxsoft.fcare.core.BR

import com.wxsoft.fcare.core.data.entity.User
import com.wxsoft.fcare.databinding.LayoutItemAssignmentBinding


class CarAdapter constructor(private val lifecycleOwner: LifecycleOwner, val viewModel: DispatchCarViewModel): RecyclerView.Adapter< CarAdapter.ItemViewHolder>() {

    private val differ = AsyncListDiffer<User>(this, CarAdapter.DiffCallback)

    var doctors: List<User> = emptyList()
        set(value) {
            field = value
            differ.submitList(value)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding: ViewDataBinding = LayoutItemAssignmentBinding.inflate(LayoutInflater.from(parent.context),parent,  false)
        return CarAdapter.ItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.binding.apply {

            setVariable(BR.task,differ.currentList[position])
            setVariable(BR.listener,viewModel)
            setLifecycleOwner(lifecycleOwner)
            executePendingBindings()

        }
    }


    class ItemViewHolder(binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        var binding: ViewDataBinding
            private set

        init {
            this.binding = binding
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {

            return  oldItem.id==newItem.id
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {

            return oldItem.id==newItem.id
        }
    }

}