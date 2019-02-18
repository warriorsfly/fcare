package com.wxsoft.fcare.ui.details.measures

import androidx.lifecycle.LifecycleOwner
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.wxsoft.fcare.R
import com.wxsoft.fcare.databinding.ItemMeasuresNomalBinding
import com.wxsoft.fcare.databinding.ItemMeasuresRemarkBinding
import kotlinx.android.synthetic.main.item_measures_nomal.view.*
import kotlinx.android.synthetic.main.item_measures_remark.view.*


class MeasuresAdapter constructor(private val lifecycleOwner: LifecycleOwner, val viewModel: MeasuresViewModel) :
    androidx.recyclerview.widget.RecyclerView.Adapter<MeasuresAdapter.ItemViewHolder>() {

    private var titleArray:Array<String> = arrayOf("治疗措施","救治结果", "出诊结果","绕行")

    override fun getItemCount(): Int {
        return 4
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.binding.apply {
            lifecycleOwner = this@MeasuresAdapter.lifecycleOwner
                if (position != 3){
                    if (root.measures_items_rv.adapter == null){
                        val adapter = MeasuresItemAdapter(this@MeasuresAdapter.lifecycleOwner,viewModel)
                        adapter.section = position
                        root.measures_title_name.text = titleArray[position]
                        root.measures_items_rv.adapter = adapter
                    }
                }else{
                    val adapter = MeasuresItemAdapter(this@MeasuresAdapter.lifecycleOwner,viewModel)
                    adapter.section = position
                    root.measures_detour_department_rv.adapter = adapter
                }
            executePendingBindings()
        }



    }


    override fun getItemViewType(position: Int): Int {
        var type = 0
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




    class ItemViewHolder(binding: ViewDataBinding) : androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root) {
        var binding: ViewDataBinding
            private set

        init {
            this.binding = binding
        }
    }

}