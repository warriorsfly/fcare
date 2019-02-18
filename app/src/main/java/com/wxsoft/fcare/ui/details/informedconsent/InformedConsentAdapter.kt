package com.wxsoft.fcare.ui.details.informedconsent

import android.arch.lifecycle.LifecycleOwner
import android.databinding.ViewDataBinding
import android.support.v7.recyclerview.extensions.AsyncListDiffer
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.wxsoft.fcare.core.BR
import com.wxsoft.fcare.core.data.entity.Talk
import com.wxsoft.fcare.databinding.ItemInformedConsentBinding

class InformedConsentAdapter constructor(private val lifecycleOwner: LifecycleOwner, val viewModel: InformedConsentViewModel) :
    RecyclerView.Adapter<InformedConsentAdapter.ItemViewHolder>(){

    private val differ = AsyncListDiffer<Talk>(this, DiffCallback)

    var items: List<Talk> = emptyList()
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
            setVariable(BR.viewmodel, viewModel)
            lifecycleOwner = lifecycleOwner
            executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val binding: ViewDataBinding =
            ItemInformedConsentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    class ItemViewHolder(binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        var binding: ViewDataBinding
            private set
        init {
            this.binding = binding
        }
    }


    object DiffCallback : DiffUtil.ItemCallback<Talk>() {
        override fun areItemsTheSame(oldItem: Talk, newItem: Talk): Boolean {

            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Talk, newItem: Talk): Boolean {

            return oldItem.id == newItem.id
        }
    }
}