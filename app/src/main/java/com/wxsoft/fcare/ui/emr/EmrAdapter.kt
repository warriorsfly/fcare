package com.wxsoft.fcare.ui.emr

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.data.entity.EmrItem
import com.wxsoft.fcare.core.data.entity.Patient
import com.wxsoft.fcare.databinding.ItemNewEmrPatientInfoBinding
import com.wxsoft.fcare.utils.ActionType


class EmrAdapter constructor(private val owner: LifecycleOwner,private val itemClick:(String)->Unit) : ListAdapter<EmrItem, EmrAdapter.ItemViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val inflater = LayoutInflater.from(parent.context)

        return when(viewType){

            R.layout.item_new_emr_patient_info->{
                ItemViewHolder.BaseInfoViewHolder(
                    ItemNewEmrPatientInfoBinding.inflate(inflater,parent,false).apply {

                        lifecycleOwner=owner

                    },itemClick)
            }

            else->throw IllegalStateException("unkown viewtype $viewType")
        }
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val emr=getItem(position)
        when(holder){
            is ItemViewHolder.BaseInfoViewHolder->{
                holder.binding.apply {
                    item=emr
                    patient=emr.result as? Patient
                    patient?.let {
                        if(!panel.isInflated){
                            panel.viewStub?.inflate()
                        }
                    }
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return  R.layout.item_new_emr_patient_info

//        when(getItem(position).code){
//            ActionType.患者信息录入-> R.layout.item_new_emr_patient_info
//            else->0
////            else->throw IllegalStateException("unkown viewtype at $position")
//        }
    }

    sealed class ItemViewHolder(open val binding: ViewDataBinding,open val click:(String)->Unit) :
        RecyclerView.ViewHolder(binding.root) {



        class BaseInfoViewHolder(
            override val binding: ItemNewEmrPatientInfoBinding,
            override val click:(String)->Unit
        ) : ItemViewHolder(binding,click){
            init {
                binding.apply {
                    root.findViewById<ImageButton>(R.id.edit)
                        .setOnClickListener {
                            item?.let {
                                click(it.code)
                            }
                        }
                }
            }
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<EmrItem>() {
        override fun areItemsTheSame(oldItem: EmrItem, newItem: EmrItem): Boolean {
            return oldItem.code==newItem.code && oldItem.id==newItem.id
        }

        override fun areContentsTheSame(oldItem: EmrItem, newItem: EmrItem): Boolean {
            val result1 = oldItem.result
            val result2 = newItem.result
            return result1 == result2
        }
    }
}