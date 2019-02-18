package com.wxsoft.fcare.ui.details.diagnose

import androidx.lifecycle.LifecycleOwner
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.BR
import com.wxsoft.fcare.core.data.entity.Dictionary
import com.wxsoft.fcare.databinding.ItemDiagnoseTypeBinding
import kotlinx.android.synthetic.main.item_diagnose_type.view.*

class DiagnoseTypeAdapter constructor(private val lifecycleOwner: LifecycleOwner, val viewModel: DiagnoseViewModel) :
    androidx.recyclerview.widget.RecyclerView.Adapter<DiagnoseTypeAdapter.ItemViewHolder>() {

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
            when(differ.currentList[position].itemName){
                "胸痛" ->{
                    root.icon_for_type.setImageResource(R.drawable.ic_item_icon_xiontong)
                    differ.currentList[position].checked = true
                }
                "卒中" ->{root.icon_for_type.setImageResource(R.drawable.ic_item_icon_cuzhong)}
                "创伤" ->{root.icon_for_type.setImageResource(R.drawable.ic_item_icon_chuangshang)}
                "危重孕产妇" ->{root.icon_for_type.setImageResource(R.drawable.ic_item_icon_yunchanfu)}
                "危重新生儿" ->{root.icon_for_type.setImageResource(R.drawable.ic_item_icon_ertong)}
                "其他" ->{root.icon_for_type.setImageResource(R.drawable.ic_item_icon_other)}
            }
            setVariable(BR.item, differ.currentList[position])
            setVariable(BR.listener,viewModel)
            lifecycleOwner = this@DiagnoseTypeAdapter.lifecycleOwner
            executePendingBindings()

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val binding: ViewDataBinding =
            ItemDiagnoseTypeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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