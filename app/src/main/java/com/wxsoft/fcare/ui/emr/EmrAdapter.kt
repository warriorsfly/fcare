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
import com.wxsoft.fcare.core.data.entity.*
import com.wxsoft.fcare.core.data.entity.drug.DrugHistory
import com.wxsoft.fcare.core.data.entity.drug.DrugRecord
import com.wxsoft.fcare.core.data.entity.rating.RatingResult
import com.wxsoft.fcare.databinding.*
import com.wxsoft.fcare.ui.details.medicalhistory.DrugHistoryItemAdapter
import com.wxsoft.fcare.ui.details.pharmacy.drugrecords.DrugRecordsAdapter
import com.wxsoft.fcare.ui.rating.RatingAdapter
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

            R.layout.item_new_emr_rating_list->{
                ItemViewHolder.RatingListViewHolder(
                    ItemNewEmrRatingListBinding.inflate(inflater,parent,false)
                        .apply {lifecycleOwner=owner},itemClick,owner)
            }

            R.layout.item_new_emr_measure_list->{
                ItemViewHolder.MeasureViewHolder(
                    ItemNewEmrMeasureListBinding.inflate(inflater,parent,false)
                        .apply {lifecycleOwner=owner},itemClick)
            }

            R.layout.item_new_emr_drug_list->{
                ItemViewHolder.DrugViewHolder(
                    ItemNewEmrDrugListBinding.inflate(inflater,parent,false)
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
                    executePendingBindings()
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
                    executePendingBindings()
                }
            }

            is ItemViewHolder.RatingListViewHolder->{
                holder.binding.apply {
                    item=emr
                    if(tabLayout.tabCount==0){
                        ( emr.result as? List<Record<RatingResult>>)?.forEach {
                            tabLayout.addTab(tabLayout.newTab().setText(it.typeName))
                        }
                    }
                    executePendingBindings()
                }
            }

            is ItemViewHolder.MeasureViewHolder->{
                holder.binding.apply {
                    item=emr
                    measure=item?.result  as? Measure
                    executePendingBindings()
                }
            }

            is ItemViewHolder.DrugViewHolder->{
                holder.binding.apply {
                    item=emr
                    if(list.adapter==null){
                        list.adapter=DrugRecordsAdapter(owner,isEmr = true)
                    }
                    ( emr.result as? List<DrugRecord>)?.let {
                        (list.adapter as DrugRecordsAdapter).items=it
                    }
                    executePendingBindings()
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
//        return  R.layout.item_new_emr_patient_info

        return when(getItem(position).code){
            ActionType.患者信息录入-> R.layout.item_new_emr_patient_info
            ActionType.生命体征-> R.layout.item_new_emr_vital_list
            ActionType.GRACE-> R.layout.item_new_emr_rating_list
            ActionType.DispostionMeasures-> R.layout.item_new_emr_measure_list
            ActionType.给药-> R.layout.item_new_emr_drug_list
            else->0
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

        class MeasureViewHolder(
            override val binding: ItemNewEmrMeasureListBinding,
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

        class DrugViewHolder(
            override val binding: ItemNewEmrDrugListBinding,
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

        class RatingListViewHolder(
            override val binding: ItemNewEmrRatingListBinding,
            override val click:(String)->Unit,
            private val owner: LifecycleOwner
        ) : ItemViewHolder(binding,click),TabLayout.OnTabSelectedListener{
            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabSelected(tab: TabLayout.Tab?) {
                binding.apply {
                    (item?.result  as List<Record<RatingResult>>)?.apply {
                        (list.adapter as? RatingAdapter)?.submitList(this[tab?.position?:0].items)
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

                    list.adapter=RatingAdapter(owner,scencely = false,showDetail = {})
                    tabLayout.addOnTabSelectedListener(this@RatingListViewHolder)
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