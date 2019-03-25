package com.wxsoft.fcare.ui.details.informedconsent

import androidx.lifecycle.LifecycleOwner
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
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
            val ii = differ.currentList[position]
            ii.judgeTime()
            setVariable(BR.item, ii)
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

    class ItemViewHolder(binding: ViewDataBinding) : androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root) {
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