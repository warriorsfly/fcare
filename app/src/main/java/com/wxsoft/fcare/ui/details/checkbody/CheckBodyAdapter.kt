package com.wxsoft.fcare.ui.details.checkbody

import android.arch.lifecycle.LifecycleOwner
import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.BR
import com.wxsoft.fcare.databinding.ItemCheckBodyNomalBinding
import com.wxsoft.fcare.databinding.ItemCheckBodyOtherBinding
import kotlinx.android.synthetic.main.item_check_body_nomal.view.*
import kotlinx.android.synthetic.main.item_check_body_other.view.*

class CheckBodyAdapter constructor(private val lifecycleOwner: LifecycleOwner, val viewModel: CheckBodyViewModel) :
    RecyclerView.Adapter<CheckBodyAdapter.ItemViewHolder>() {

    var titleArray:Array<String> = arrayOf("体格检查","皮肤", "左瞳孔", "左瞳孔对光反应","右瞳孔", "右瞳孔对光反应","其他描述")

    override fun getItemCount(): Int {
        return 7
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.binding.apply {
            setLifecycleOwner(lifecycleOwner)
            if (position != 6){
                if (root.items_rv.adapter == null){
                    var adapter = CheckBodyItemAdapter(lifecycleOwner,viewModel)
                    adapter.section = position
                    root.title_name.setText(titleArray.get(position))
                    root.items_rv.adapter = adapter
                }
            }else{
                root.other_title_name.setText(titleArray.get(position))
                setVariable(BR.listener,viewModel)
            }
            executePendingBindings()
        }



    }


    override fun getItemViewType(position: Int): Int {
        var type:Int = 0
        when(position){
            0->{type = R.layout.item_check_body_nomal}
            1->{type = R.layout.item_check_body_nomal}
            2->{type = R.layout.item_check_body_nomal}
            3->{type = R.layout.item_check_body_nomal}
            4->{type = R.layout.item_check_body_nomal}
            5->{type = R.layout.item_check_body_nomal}
            6->{type = R.layout.item_check_body_other}
        }
        return type
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        when(viewType) {
            R.layout.item_check_body_nomal -> {
                val binding = ItemCheckBodyNomalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return ItemViewHolder(binding)
            }
            R.layout.item_check_body_other -> {
                val binding = ItemCheckBodyOtherBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return ItemViewHolder(binding)
            }
            else-> {
                val binding: ViewDataBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_check_body_nomal,
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