package com.wxsoft.fcare.ui.main.fragment.assignment

import android.arch.lifecycle.LifecycleOwner
import android.databinding.ViewDataBinding
import android.support.v7.recyclerview.extensions.AsyncListDiffer
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.wxsoft.fcare.core.BR
import com.wxsoft.fcare.core.data.entity.Task
import com.wxsoft.fcare.databinding.LayoutItemAssignmentBinding


class AssignmentAdapter constructor(private val lifecycleOwner: LifecycleOwner, val viewModel: AssignmentViewModel) :
    RecyclerView.Adapter<AssignmentAdapter.ItemViewHolder>() {

    private val differ = AsyncListDiffer<Task>(this, DiffCallback)

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    var tasks: List<Task> = emptyList()
        set(value) {
            field = value
            differ.submitList(value)
        }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        holder.binding.apply {

            setVariable(BR.task, differ.currentList[position])
            setVariable(BR.listener, viewModel)
            setLifecycleOwner(lifecycleOwner)
            executePendingBindings()

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val binding: ViewDataBinding =
            LayoutItemAssignmentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    class ItemViewHolder(binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        var binding: ViewDataBinding
            private set

        init {
            this.binding = binding
        }
    }


    object DiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {

            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {

            return oldItem.id == newItem.id
        }
    }


}