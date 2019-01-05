package com.wxsoft.fcare.ui.details.checkbody

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.databinding.ViewDataBinding
import android.support.v7.recyclerview.extensions.AsyncListDiffer
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.wxsoft.fcare.core.BR
import com.wxsoft.fcare.core.data.entity.Dictionary
import com.wxsoft.fcare.databinding.ItemCheckOnlyNameBinding

class CheckBodyItemAdapter constructor(private val lifecycleOwner: LifecycleOwner, val viewModel: CheckBodyViewModel) :
    RecyclerView.Adapter<CheckBodyItemAdapter.ItemViewHolder>() {

    private val differ = AsyncListDiffer<Dictionary>(this, DiffCallback)

    var section:Int = 0
        set(value) {
            field = value
            when(value){
                0->{viewModel.coordinationItems.observe(lifecycleOwner, Observer {it -> items = it ?: emptyList()})}
                1->{viewModel.skinItems.observe(lifecycleOwner, Observer {it -> items = it ?: emptyList()})}
                2->{viewModel.leftPupilsItems.observe(lifecycleOwner, Observer {it -> items = it ?: emptyList()})}
                3->{viewModel.leftResponseLightItems.observe(lifecycleOwner, Observer {it -> items = it ?: emptyList()})}
                4->{viewModel.rightPupilsItems.observe(lifecycleOwner, Observer {it -> items = it ?: emptyList()})}
                5->{viewModel.rightResponseLightItems.observe(lifecycleOwner, Observer {it -> items = it ?: emptyList()})}
            }
        }

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
            differ.currentList[position].section = section
            setVariable(BR.item, differ.currentList[position])
            setVariable(BR.listener, viewModel)
            setLifecycleOwner(lifecycleOwner)
            executePendingBindings()

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val binding: ViewDataBinding =
            ItemCheckOnlyNameBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    class ItemViewHolder(binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
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