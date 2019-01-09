package com.wxsoft.fcare.ui.details.measures

import android.arch.lifecycle.LifecycleOwner
import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.wxsoft.fcare.BR
import com.wxsoft.fcare.R
import com.wxsoft.fcare.databinding.ItemMeasuresNomalBinding
import com.wxsoft.fcare.databinding.ItemMeasuresRemarkBinding
import kotlinx.android.synthetic.main.item_measures_nomal.view.*


class MeasuresAdapter constructor(private val lifecycleOwner: LifecycleOwner, val viewModel: MeasuresViewModel) :
    RecyclerView.Adapter<MeasuresAdapter.ItemViewHolder>() {

    var titleArray:Array<String> = arrayOf("措施","救治结果", "出诊结果","备注")

    override fun getItemCount(): Int {
        return 4
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.binding.apply {
            setLifecycleOwner(lifecycleOwner)
                if (position != 3){
                    if (root.measures_items_rv.adapter == null){
                        var adapter = MeasuresItemAdapter(lifecycleOwner,viewModel)
                        adapter.section = position
                        root.measures_title_name.setText(titleArray.get(position))
                        root.measures_items_rv.adapter = adapter
                    }
                }else{
                    setVariable(BR.listener,viewModel)
                }
            executePendingBindings()
        }



    }


    override fun getItemViewType(position: Int): Int {
        var type:Int = 0
        when(position){
            0->{type = R.layout.item_measures_nomal}
            1->{type = R.layout.item_measures_nomal}
            2->{type = R.layout.item_measures_nomal}
            3->{type = R.layout.item_measures_remark}
        }
        return type
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        when(viewType) {
            R.layout.item_measures_nomal -> {
                val binding = ItemMeasuresNomalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return ItemViewHolder(binding)
            }
            R.layout.item_measures_remark -> {
                val binding = ItemMeasuresRemarkBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return ItemViewHolder(binding)
            }
            else-> {
                val binding: ViewDataBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_measures_nomal,
                    parent,
                    false
                )

                return ItemViewHolder(binding)
            }
        }
    }




    class ItemViewHolder(binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        var binding: ViewDataBinding
            private set

        init {
            this.binding = binding
        }
    }

}