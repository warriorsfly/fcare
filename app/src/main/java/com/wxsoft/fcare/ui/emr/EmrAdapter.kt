package com.wxsoft.fcare.ui.emr

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
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
import com.wxsoft.fcare.core.data.entity.drug.ACSDrug
import com.wxsoft.fcare.core.data.entity.drug.DrugHistory
import com.wxsoft.fcare.core.data.entity.drug.DrugRecord
import com.wxsoft.fcare.core.data.entity.rating.RatingResult
import com.wxsoft.fcare.databinding.*
import com.wxsoft.fcare.ui.common.EmrImageAdapter
import com.wxsoft.fcare.ui.details.medicalhistory.DrugHistoryItemAdapter
import com.wxsoft.fcare.ui.details.pharmacy.drugrecords.DrugRecordsAdapter
import com.wxsoft.fcare.ui.rating.RatingAdapter
import com.wxsoft.fcare.utils.ActionType


class EmrAdapter constructor(private val owner: LifecycleOwner,private val itemClick:(String)->Unit,private val isHeader:Boolean=false) : ListAdapter<EmrItem, EmrAdapter.ItemViewHolder>(DiffCallback) {



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

            R.layout.item_new_emr_acs_medicine->{
                ItemViewHolder.AcsViewHolder(
                    ItemNewEmrAcsMedicineBinding.inflate(inflater,parent,false)
                        .apply {lifecycleOwner=owner},itemClick)
            }
            R.layout.item_emr_header->{
                ItemViewHolder.HeadViewHolder(
                    ItemEmrHeaderBinding.inflate(inflater,parent,false)
                        .apply {lifecycleOwner=owner},itemClick)
            }
            R.layout.item_new_emr_diagnose_result->{
                ItemViewHolder.DiagnoseViewHolder(
                    ItemNewEmrDiagnoseResultBinding.inflate(inflater,parent,false)
                        .apply {lifecycleOwner=owner},itemClick)
            }

            R.layout.item_new_emr_complaints->{
                ItemViewHolder.ComplaintsViewHolder(
                    ItemNewEmrComplaintsBinding.inflate(inflater,parent,false)
                        .apply {lifecycleOwner=owner},itemClick)
            }

            R.layout.item_new_emr_med_his->{
                ItemViewHolder.MedHisViewHolder(
                    ItemNewEmrMedHisBinding.inflate(inflater,parent,false)
                        .apply {lifecycleOwner=owner},itemClick)
            }
            R.layout.item_new_emr_ecg->{
                ItemViewHolder.EcgViewHolder(
                    ItemNewEmrEcgBinding.inflate(inflater,parent,false)
                        .apply {lifecycleOwner=owner},itemClick)
            }

            R.layout.item_new_emr_cb_result->{
                ItemViewHolder.CbViewHolder(
                    ItemNewEmrCbResultBinding.inflate(inflater,parent,false)
                        .apply {lifecycleOwner=owner},itemClick)
            }

            else->{
                ItemViewHolder.NoneViewHolder(
                    ItemNewEmrNoneBinding.inflate(inflater,parent,false)
                        .apply {lifecycleOwner=owner},itemClick)
            }
        }
    }

    private fun clickItem(view: View, pos :Int){

    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val emr=getItem(position)
        when(holder){

            is ItemViewHolder.NoneViewHolder->{
                holder.binding.apply {
                    item=emr
                    executePendingBindings()
                }
            }
            is ItemViewHolder.BaseInfoViewHolder->{
                holder.binding.apply {
                    item=emr
                    patient=emr.result as? Patient
//                    patient?.let {
//                        if(!panel.isInflated){
//                            panel.viewStub?.inflate()
//                        }
//                    }
                    executePendingBindings()
                }
            }

            is ItemViewHolder.VitalListViewHolder->{
                holder.binding.apply {
                    item=emr
                    if(emr.result != null ) {
                        if((emr.result as? List<VitalSign>)!!.size>0){
                            vital=(emr.result as? List<VitalSign>)?.get(0)
                        }
                    }
//                    if(tabLayout.tabCount==0){
//                        val tabs=( emr.result as? List<Record<VitalSign>>)
//                            tabs?.forEach {
//                                tabLayout.addTab(tabLayout.newTab().setText(it.typeName))
//                            }
//                        if(tabs?.size?:0==1){
//                            tabLayout.visibility=View.GONE
//                        }
//                    }
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

            is ItemViewHolder.DiagnoseViewHolder->{
                holder.binding.apply {
                    item=emr
                    diag=item?.result  as? Diagnosis
                    executePendingBindings()
                }
            }

            is ItemViewHolder.HeadViewHolder->{
                holder.binding.apply {
                    item=emr
                    executePendingBindings()
                }
            }

            is ItemViewHolder.DrugViewHolder->{
                holder.binding.apply {
                    item=emr
                    if(list.adapter==null){
                        list.adapter=DrugRecordsAdapter(owner,isEmr = true, viewModel = null, itemClick = ::clickItem)
                    }
                    ( emr.result as? List<DrugRecord>)?.let {
                        (list.adapter as DrugRecordsAdapter).items=it
                    }
                    executePendingBindings()
                }
            }

            is ItemViewHolder.ComplaintsViewHolder->{
                holder.binding.apply {
                    item=emr
                    memo.text=(emr.result as? List<Complain>)?.joinToString (","){it.ccCode_Name}
                    executePendingBindings()
                }
            }

            is ItemViewHolder.AcsViewHolder->{
                holder.binding.apply {
                    item=emr
                    medicine=emr.result as? ACSDrug
                    executePendingBindings()
                }
            }

            is ItemViewHolder.MedHisViewHolder->{
                holder.binding.apply {
                    item=emr
                    val his=emr.result as? MedicalHistory
                    history=his
                    if(list.adapter==null){
                        list.adapter=EmrImageAdapter(owner)
                    }
                    (list.adapter as? EmrImageAdapter)?.submitList(his?.attachments?.map { it.httpUrl })
                    memo3.text=his?.pastHistorys?.joinToString (","){it.phCode_Name}
                    executePendingBindings()
                }
            }

            is ItemViewHolder.EcgViewHolder->{
                holder.binding.apply {
                    item=emr
                    val ec=emr.result as? Ecg
                    ecg=ec
                    if(list.adapter==null){
                        list.adapter=EmrImageAdapter(owner)
                    }
                    (list.adapter as? EmrImageAdapter)?.submitList(ec?.attachments?.map { it.httpUrl })
                    executePendingBindings()
                }
            }


            is ItemViewHolder.CbViewHolder->{
                holder.binding.apply {
                    item=emr
                    checkBody=emr.result as? CheckBody

                    executePendingBindings()
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(isHeader)
            R.layout.item_emr_header
            else {
            when (getItem(position).code) {
                ActionType.患者信息录入 -> R.layout.item_new_emr_patient_info
                ActionType.生命体征 -> R.layout.item_new_emr_vital_list
                ActionType.GRACE -> R.layout.item_new_emr_rating_list
                ActionType.DispostionMeasures -> R.layout.item_new_emr_measure_list
                ActionType.给药 -> R.layout.item_new_emr_drug_list
                ActionType.诊断 -> R.layout.item_new_emr_diagnose_result
                ActionType.主诉及症状 -> R.layout.item_new_emr_complaints
                ActionType.IllnessHistory -> R.layout.item_new_emr_med_his
                ActionType.心电图-> R.layout.item_new_emr_ecg
                ActionType.PhysicalExamination-> R.layout.item_new_emr_cb_result
                ActionType.ACS给药-> R.layout.item_new_emr_acs_medicine
                else -> R.layout.item_new_emr_none
            }
        }
    }

    sealed class ItemViewHolder(open val binding: ViewDataBinding,open val click:(String)->Unit) :
        RecyclerView.ViewHolder(binding.root) {

        class NoneViewHolder(
            override val binding:ItemNewEmrNoneBinding,
            override val click:(String)->Unit
        ) : ItemViewHolder(binding,click){
            init {
                binding.apply {
                    root.setOnClickListener {
                        item?.code?.let(click)
                    }
                }
            }
        }

        class HeadViewHolder(
            override val binding:ItemEmrHeaderBinding,
            override val click:(String)->Unit
        ) : ItemViewHolder(binding,click){
            init {
                binding.apply {
                    root.setOnClickListener {
                        item?.code?.let(click)
                    }
                }
            }
        }

        class BaseInfoViewHolder(
            override val binding: ItemNewEmrPatientInfoBinding,
            override val click:(String)->Unit
        ) : ItemViewHolder(binding,click){
            init {
                binding.apply {
                    root.findViewById<ImageButton>(R.id.edit)
                        .setOnClickListener {
                            item?.code?.let(click)
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
                            item?.code?.let(click)
                        }
                }
            }
        }


        class ComplaintsViewHolder(
            override val binding: ItemNewEmrComplaintsBinding,
            override val click:(String)->Unit
        ) : ItemViewHolder(binding,click){
            init {
                binding.apply {
                    root.findViewById<ImageButton>(R.id.edit)
                        .setOnClickListener {
                            item?.code?.let(click)
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
                            item?.code?.let(click)
                        }
                }
            }
        }

        class AcsViewHolder(
            override val binding: ItemNewEmrAcsMedicineBinding,
            override val click:(String)->Unit
        ) : ItemViewHolder(binding,click){
            init {
                binding.apply {
                    root.findViewById<ImageButton>(R.id.edit)
                        .setOnClickListener {
                            item?.code?.let(click)
                        }
                }
            }
        }

        class DiagnoseViewHolder(
            override val binding: ItemNewEmrDiagnoseResultBinding,
            override val click:(String)->Unit
        ) : ItemViewHolder(binding,click){
            init {
                binding.apply {
                    root.findViewById<ImageButton>(R.id.edit)
                        .setOnClickListener {
                            item?.code?.let(click)
                        }
                }
            }
        }

        class MedHisViewHolder(
            override val binding: ItemNewEmrMedHisBinding,
            override val click:(String)->Unit
        ) : ItemViewHolder(binding,click){
            init {
                binding.apply {
                    root.findViewById<ImageButton>(R.id.edit)
                        .setOnClickListener {
                            item?.code?.let(click)

                        }
                }
            }
        }

        class EcgViewHolder(
            override val binding: ItemNewEmrEcgBinding,
            override val click:(String)->Unit
        ) : ItemViewHolder(binding,click){
            init {
                binding.apply {
                    root.findViewById<ImageButton>(R.id.edit)
                        .setOnClickListener {
                            item?.code?.let(click)
                        }
                }
            }
        }

        class CbViewHolder(
            override val binding: ItemNewEmrCbResultBinding,
            override val click:(String)->Unit
        ) : ItemViewHolder(binding,click){
            init {
                binding.apply {
                    root.findViewById<ImageButton>(R.id.edit)
                        .setOnClickListener {
                            item?.code?.let(click)
                        }
                }
            }
        }

        class VitalListViewHolder(
            override val binding: ItemNewEmrVitalListBinding,
            override val click:(String)->Unit
        ) : ItemViewHolder(binding,click){

            init {
                binding.apply {
                    root.findViewById<ImageButton>(R.id.edit)
                        .setOnClickListener {
                            item?.code?.let(click)
                        }
                }
            }
//            override fun onTabUnselected(tab: TabLayout.Tab?) {}
//
//            override fun onTabSelected(tab: TabLayout.Tab?) {
//                binding.apply {
//                    (item?.result  as List<Record<VitalSign>>)?.apply {
//                        vital= this[tab?.position?:0].items.firstOrNull()
//                        executePendingBindings()
//                    }
//                }
//
//            }
//
//            override fun onTabReselected(tab: TabLayout.Tab?) {}
//
//            init {
//                binding.apply {
//                    root.findViewById<ImageButton>(R.id.edit)
//                        .setOnClickListener {
//                            item?.code?.let(click)
//                        }
//
//                    tabLayout.addOnTabSelectedListener(this@VitalListViewHolder)
//                }
//            }
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

        @SuppressLint("DiffUtilEquals")
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