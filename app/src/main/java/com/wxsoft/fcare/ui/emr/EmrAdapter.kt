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
import com.wxsoft.fcare.core.data.entity.chest.Intervention
import com.wxsoft.fcare.core.data.entity.chest.OutCome
import com.wxsoft.fcare.core.data.entity.drug.ACSDrug
import com.wxsoft.fcare.core.data.entity.drug.DrugRecord
import com.wxsoft.fcare.core.data.entity.lis.LisCr
import com.wxsoft.fcare.core.data.entity.rating.RatingResult
import com.wxsoft.fcare.databinding.*
import com.wxsoft.fcare.ui.common.EmrImageAdapter
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

            R.layout.item_new_emr_comming_by->{
                ItemViewHolder.CommingViewHolder(
                    ItemNewEmrCommingByBinding.inflate(inflater,parent,false)
                        .apply {lifecycleOwner=owner},itemClick)
            }

            R.layout.item_new_emr_xg->{
                ItemViewHolder.XGViewHolder(
                    ItemNewEmrXgBinding.inflate(inflater,parent,false)
                        .apply {lifecycleOwner=owner},itemClick)
            }

            R.layout.item_new_emr_ct_op->{
                ItemViewHolder.CtViewHolder(
                    ItemNewEmrCtOpBinding.inflate(inflater,parent,false)
                        .apply {lifecycleOwner=owner},itemClick)
            }

            R.layout.item_new_emr_dgs_op->{
                ItemViewHolder.DGSViewHolder(
                    ItemNewEmrDgsOpBinding.inflate(inflater,parent,false)
                        .apply {lifecycleOwner=owner},itemClick)
            }

            R.layout.item_new_emr_out_come->{
                ItemViewHolder.OutComeViewHolder(
                    ItemNewEmrOutComeBinding.inflate(inflater,parent,false)
                        .apply {lifecycleOwner=owner},itemClick)
            }

            R.layout.item_new_emr_cy->{
                ItemViewHolder.CyViewHolder(
                    ItemNewEmrCyBinding.inflate(inflater,parent,false)
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
                    executePendingBindings()
                }
            }

            is ItemViewHolder.VitalListViewHolder->{
                holder.binding.apply {
                    item=emr
                    if(emr.result != null ) {
                        if((emr.result as? List<VitalSign>)!!.isNotEmpty()){
                            vital=(emr.result as? List<VitalSign>)?.get(0)
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

            is ItemViewHolder.DiagnoseViewHolder->{
                holder.binding.apply {
                    item=emr
                    diag=item?.result  as? Diagnosis
                    executePendingBindings()
                }
            }

            is ItemViewHolder.CtViewHolder->{
                holder.binding.apply {
                    item=emr
                    cr=item?.result  as? Pacs
                    executePendingBindings()
                }
            }

            is ItemViewHolder.OutComeViewHolder->{
                holder.binding.apply {
                    item=emr
                    comming=item?.result  as? OutCome
                    executePendingBindings()
                }
            }

            is ItemViewHolder.DGSViewHolder->{
                holder.binding.apply {
                    item=emr
                    cr=item?.result  as? Intervention
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

            is ItemViewHolder.XGViewHolder->{
                holder.binding.apply {
                    item=emr
                    cr=emr.result as? LisCr

                    executePendingBindings()
                }
            }

            is ItemViewHolder.CyViewHolder->{
                holder.binding.apply {
                    item=emr
                    cy=emr.result as? DisChargeDiagnosis

                    executePendingBindings()
                }
            }

            is ItemViewHolder.CommingViewHolder-> {
                holder.binding.apply {
                    item = emr
                    comming = (emr.result as? ComingBy)?.apply {

                        emergencyDoctor = comingWayStaffs.firstOrNull { it.staffType == "1" }?.let {
                            User().apply {
                                this.id = it.staffId
                                trueName = it.staffName
                            }
                        } ?: User()

                        emergencyNurse = comingWayStaffs.firstOrNull { it.staffType == "2" }?.let {
                            User().apply {
                                this.id = it.staffId
                                trueName = it.staffName
                            }
                        } ?: User()
                        var cdoctors = comingWayStaffs.filter { it.staffType == "3" }?.map {
                            User().apply {
                                this.id = it.staffId
                                trueName = it.staffName
                            }
                        }
                        consultantDoctors = cdoctors.joinToString(separator = ",") { it.trueName }
                    }


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
                ActionType.CT_OPERATION -> R.layout.item_new_emr_ct_op
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
                ActionType.来院方式-> R.layout.item_new_emr_comming_by
                ActionType.肌钙蛋白-> R.layout.item_new_emr_xg
                ActionType.Catheter-> R.layout.item_new_emr_dgs_op
                ActionType.患者转归-> R.layout.item_new_emr_out_come
                ActionType.出院诊断-> R.layout.item_new_emr_cy
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
                    root.findViewById<ImageButton>(R.id.edit)
                        .setOnClickListener {
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
                    root.findViewById<ImageButton>(R.id.edit)
                        .setOnClickListener {
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

        class CtViewHolder(
            override val binding: ItemNewEmrCtOpBinding,
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

        class OutComeViewHolder(
            override val binding: ItemNewEmrOutComeBinding,
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

        class DGSViewHolder(
            override val binding: ItemNewEmrDgsOpBinding,
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

        class CommingViewHolder(
            override val binding: ItemNewEmrCommingByBinding,
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

        class CyViewHolder(
            override val binding: ItemNewEmrCyBinding,
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

        class XGViewHolder(
            override val binding: ItemNewEmrXgBinding,
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