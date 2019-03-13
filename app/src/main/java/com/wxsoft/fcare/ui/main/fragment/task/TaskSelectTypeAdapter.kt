package com.wxsoft.fcare.ui.main.fragment.task

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.wxsoft.fcare.core.BR
import com.wxsoft.fcare.core.data.entity.Dictionary
import com.wxsoft.fcare.databinding.ItemTaskSelectTypeBinding
import com.wxsoft.fcare.ui.main.fragment.patients.PatientsViewModel

class TaskSelectTypeAdapter constructor(private val owner: LifecycleOwner, val viewModel: TaskViewModel) :
    RecyclerView.Adapter<TaskSelectTypeAdapter.ItemViewHolder>(){

    private val differ = AsyncListDiffer<Dictionary>(this, DiffCallback)

    var items: List<Dictionary> = emptyList()
        set(value) {
            field = value
            differ.submitList(value)
        }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        holder.binding.apply {
            setVariable(BR.item, differ.currentList[position])
            setVariable(BR.viewModel, this@TaskSelectTypeAdapter.viewModel)
            lifecycleOwner=owner
            executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val binding =
            ItemTaskSelectTypeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    class ItemViewHolder(binding: ItemTaskSelectTypeBinding) : RecyclerView.ViewHolder(binding.root) {
        var binding: ItemTaskSelectTypeBinding
            private set
        init {
            this.binding = binding
        }
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