package com.wxsoft.fcare.ui.detail.fragment.timeline


import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.wxsoft.fcare.BR
import com.wxsoft.fcare.R
import com.wxsoft.fcare.data.entity.EmrLog


class TimeAdapter: ListAdapter<EmrLog, TimeAdapter.ItemViewHolder>(TimeDiff) {

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        var binding=holder.binding

        binding.setVariable(BR.emr,getItem(position))

        binding.executePendingBindings()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val binding: ViewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_emr_detail,parent, false)

        return ItemViewHolder(binding)
    }

    class ItemViewHolder(binding:ViewDataBinding ) : RecyclerView.ViewHolder(binding.root) {
        var binding: ViewDataBinding
            private set

        init {
            this.binding = binding
        }
    }

    object TimeDiff: DiffUtil.ItemCallback<EmrLog>(){
        override fun areContentsTheSame(oldItem: EmrLog, newItem: EmrLog): Boolean {
            return oldItem.id==newItem.id
        }

        override fun areItemsTheSame(oldItem: EmrLog, newItem: EmrLog): Boolean {
            return oldItem == newItem
        }

    }
}