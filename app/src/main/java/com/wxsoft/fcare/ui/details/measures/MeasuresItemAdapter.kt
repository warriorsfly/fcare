package com.wxsoft.fcare.ui.details.measures

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.wxsoft.fcare.core.BR
import com.wxsoft.fcare.core.data.entity.Dictionary
import com.wxsoft.fcare.databinding.ItemMeasuresOnlyNameBinding


class MeasuresItemAdapter constructor(private val lifecycleOwner: LifecycleOwner, val viewModel: MeasuresViewModel) :
    androidx.recyclerview.widget.RecyclerView.Adapter<MeasuresItemAdapter.ItemViewHolder>() {

    private val differ = AsyncListDiffer<Dictionary>(this, DiffCallback)

    var section:Int = 0
        set(value) {
            field = value
            when(value){
                0->{viewModel.measuresItems.observe(lifecycleOwner, Observer { items = it ?: emptyList()})}
                1->{viewModel.cureResultItems.observe(lifecycleOwner, Observer { items = it ?: emptyList()})}
                2->{viewModel.outcallResultItems.observe(lifecycleOwner, Observer { items = it ?: emptyList()})}
                3->{viewModel.departments.observe(lifecycleOwner, Observer { items = it ?: emptyList()})}
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
            lifecycleOwner = this@MeasuresItemAdapter.lifecycleOwner
            executePendingBindings()

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val binding: ViewDataBinding =
            ItemMeasuresOnlyNameBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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