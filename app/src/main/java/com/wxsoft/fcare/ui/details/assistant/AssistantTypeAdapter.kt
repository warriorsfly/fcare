package com.wxsoft.fcare.ui.details.assistant

import android.arch.lifecycle.LifecycleOwner
import android.databinding.ViewDataBinding
import android.support.v7.recyclerview.extensions.AsyncListDiffer
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.wxsoft.fcare.core.BR
import com.wxsoft.fcare.core.data.entity.lis.LisItem
import com.wxsoft.fcare.databinding.ItemAssistantTypeBinding

class AssistantTypeAdapter constructor(private val lifecycleOwner: LifecycleOwner, val viewModel: AssistantExaminationViewModel) :
    RecyclerView.Adapter<AssistantTypeAdapter.ItemViewHolder>() {

    private val differ = AsyncListDiffer<LisItem>(this, DiffCallback)

    var section:Int = 0
        set(value) {
            field = value
        }

    var items: List<LisItem> = emptyList()
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
            setLifecycleOwner(lifecycleOwner)
            executePendingBindings()

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val binding: ViewDataBinding =
            ItemAssistantTypeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    class ItemViewHolder(binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        var binding: ViewDataBinding
            private set
        init {
            this.binding = binding
        }
    }


    object DiffCallback : DiffUtil.ItemCallback<LisItem>() {
        override fun areItemsTheSame(oldItem: LisItem, newItem: LisItem): Boolean {

            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: LisItem, newItem: LisItem): Boolean {

            return oldItem.id == newItem.id
        }
    }
}