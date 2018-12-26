package com.wxsoft.fcare.ui.detail.fragment.evaluation


import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

import com.wxsoft.fcare.BR
import com.wxsoft.fcare.R
import com.wxsoft.fcare.data.entity.Dictionary
import com.wxsoft.fcare.ui.detail.PatientDetailViewModel


class EvaluationAdapter constructor(private val viewModel: PatientDetailViewModel) :  RecyclerView.Adapter<EvaluationAdapter.ItemViewHolder>() {
    var dictionarys: List<Dictionary> = emptyList()
    override fun getItemCount(): Int {
        return dictionarys.size
    }
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        var binding=holder.binding

        binding.setVariable(BR.dictionary, dictionarys[position])
        binding.setVariable(BR.listener,viewModel)
        binding.executePendingBindings()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val binding: ViewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_dictionary,parent, false)

        return ItemViewHolder(binding)
    }

    class ItemViewHolder(binding:ViewDataBinding ) : RecyclerView.ViewHolder(binding.root) {
        var binding: ViewDataBinding
            private set

        init {
            this.binding = binding
        }
    }


}