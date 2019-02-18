package com.wxsoft.fcare.ui.details.assistant

import androidx.lifecycle.LifecycleOwner
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.wxsoft.fcare.core.BR
import com.wxsoft.fcare.core.data.entity.lis.LisItem
import com.wxsoft.fcare.databinding.ItemAssistantTypeBinding

class AssistantTypeAdapter constructor(private val lifecycleOwner: LifecycleOwner, val viewModel: AssistantExaminationViewModel) :
    androidx.recyclerview.widget.RecyclerView.Adapter<AssistantTypeAdapter.ItemViewHolder>() {

    private val differ = AsyncListDiffer<LisItem>(this, DiffCallback)

    var section:Int = 0

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
            lifecycleOwner = this@AssistantTypeAdapter.lifecycleOwner
            executePendingBindings()

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val binding: ViewDataBinding =
            ItemAssistantTypeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    class ItemViewHolder(binding: ViewDataBinding) : androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root) {
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