package com.wxsoft.fcare.ui.details.dispatchcar

import androidx.lifecycle.LifecycleOwner
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.wxsoft.fcare.core.BR
import com.wxsoft.fcare.core.data.entity.Car
import com.wxsoft.fcare.databinding.ItemSelectCarBinding


class CarAdapter constructor(private val lifecycleOwner: LifecycleOwner, val viewModel: DispatchCarViewModel): androidx.recyclerview.widget.RecyclerView.Adapter< CarAdapter.ItemViewHolder>() {

    private val differ = AsyncListDiffer<Car>(this, CarAdapter.DiffCallback)

    var cars: List<Car> = emptyList()
        set(value) {
            field = value
            differ.submitList(value)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding: ViewDataBinding = ItemSelectCarBinding.inflate(LayoutInflater.from(parent.context),parent,  false)
        return CarAdapter.ItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.binding.apply {
            setVariable(BR.car,differ.currentList[position])
            setVariable(BR.listener,viewModel)
            lifecycleOwner = this@CarAdapter.lifecycleOwner
            executePendingBindings()

        }
    }


    class ItemViewHolder(binding: ViewDataBinding) : androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root) {
        var binding: ViewDataBinding
            private set

        init {
            this.binding = binding
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<Car>() {
        override fun areItemsTheSame(oldItem: Car, newItem: Car): Boolean {

            return  oldItem.id==newItem.id
        }

        override fun areContentsTheSame(oldItem: Car, newItem: Car): Boolean {

            return oldItem.id==newItem.id
        }
    }

}