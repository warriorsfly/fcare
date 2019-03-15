package com.wxsoft.fcare.ui.emr

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.data.entity.EmrItem
import com.wxsoft.fcare.core.data.entity.Patient
import com.wxsoft.fcare.core.data.entity.Record
import com.wxsoft.fcare.core.data.entity.VitalSign
import com.wxsoft.fcare.databinding.ItemNewEmrPatientInfoBinding
import com.wxsoft.fcare.databinding.ItemNewEmrVitalListBinding
import com.wxsoft.fcare.utils.ActionType


class EmrAdapter constructor(private val owner: LifecycleOwner,private val itemClick:(String)->Unit) : ListAdapter<EmrItem, EmrAdapter.ItemViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val inflater = LayoutInflater.from(parent.context)

        return when(viewType){

            R.layout.item_new_emr_patient_info->{
                ItemViewHolder.BaseInfoViewHolder(
                    ItemNewEmrPatientInfoBinding.inflate(inflater,parent,false)
                        .apply {lifecycleOwner=owner},itemClick)
            }

            R.layout.item_new_emr_vital_list->{
                ItemViewHolder.VitalListViewHolder(
                    ItemNewEmrVitalListBinding.inflate(inflater,parent,false)
                        .apply {lifecycleOwner=owner},itemClick)
            }

            else->{
                ItemViewHolder.BaseInfoViewHolder(
                    ItemNewEmrPatientInfoBinding.inflate(inflater,parent,false)
                        .apply {lifecycleOwner=owner},itemClick)
            }
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

            is ItemViewHolder.VitalListViewHolder->{
                holder.binding.apply {
                    item=emr
                    if(tabLayout.tabCount==0){
                        ( emr.result as? List<Record<VitalSign>>)?.forEach {
                            tabLayout.addTab(tabLayout.newTab().setText(it.typeName))
                        }
                    }
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
//        return  R.layout.item_new_emr_patient_info

        return when(getItem(position).code){
            ActionType.患者信息录入-> R.layout.item_new_emr_patient_info
            ActionType.生命体征-> R.layout.item_new_emr_vital_list
            else->0
//            else->throw IllegalStateException("unkown viewtype at $position")
        }
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

        class VitalListViewHolder(
            override val binding: ItemNewEmrVitalListBinding,
            override val click:(String)->Unit
        ) : ItemViewHolder(binding,click),TabLayout.OnTabSelectedListener{
            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabSelected(tab: TabLayout.Tab?) {
                binding.apply {
                    (item?.result  as List<Record<VitalSign>>)?.apply {
                        vital= this[tab?.position?:0].items.firstOrNull()
                        executePendingBindings()
                    }
                }

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {}

            init {
                binding.apply {
                    root.findViewById<ImageButton>(R.id.edit)
                        .setOnClickListener {
                            item?.let {
                                click(it.code)
                            }
                        }

                    tabLayout.addOnTabSelectedListener(this@VitalListViewHolder)
                }
            }
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<EmrItem>() {
        override fun areItemsTheSame(oldItem: EmrItem, newItem: EmrItem): Boolean {
            return oldItem.code==newItem.code && oldItem.id==newItem.id
        }

        override fun areContentsTheSame(oldItem: EmrItem, newItem: EmrItem): Boolean {
            val oldResult = oldItem.result
            val newResult = newItem.result
            if(oldResult==null && newResult==null)return true
            return when{
                oldResult is List<*> && newResult is List<*>-> {
                    oldResult==newResult
                }

                oldResult is Patient && newResult is Patient->{
                    oldResult.name==newResult.name
                }
                else->false
            }
        }
    }
}