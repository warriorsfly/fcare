package com.wxsoft.fcare.ui.details.strategy

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.wxsoft.fcare.core.BR
import com.wxsoft.fcare.core.data.entity.Dictionary
import com.wxsoft.fcare.databinding.ItemStrategyOnlyNameBinding

class StrategyAdapter constructor(private val lifecycleOwner: LifecycleOwner, val viewModel: StrategyViewModel) :
    androidx.recyclerview.widget.RecyclerView.Adapter<StrategyAdapter.ItemViewHolder>() {

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
            setVariable(BR.listener, viewModel)
            lifecycleOwner = this@StrategyAdapter.lifecycleOwner
            executePendingBindings()

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val binding: ViewDataBinding =
            ItemStrategyOnlyNameBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    class ItemViewHolder(binding: ViewDataBinding) : androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root) {
        var binding: ViewDataBinding
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