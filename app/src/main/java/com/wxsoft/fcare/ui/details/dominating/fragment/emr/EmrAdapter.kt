package com.wxsoft.fcare.ui.details.dominating.fragment.emr

import android.arch.lifecycle.LifecycleOwner
import android.databinding.ViewDataBinding
import android.support.v7.recyclerview.extensions.AsyncListDiffer
import android.support.v7.util.DiffUtil
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.data.entity.CheckBody
import com.wxsoft.fcare.core.data.entity.ElectroCardiogram
import com.wxsoft.fcare.core.data.entity.EmrItem
import com.wxsoft.fcare.core.data.entity.Patient
import com.wxsoft.fcare.data.dictionary.ActionRes
import com.wxsoft.fcare.core.data.entity.*
import com.wxsoft.fcare.core.data.entity.chest.Intervention
import com.wxsoft.fcare.core.data.entity.chest.OutCome
import com.wxsoft.fcare.core.data.entity.drug.DrugRecord
import com.wxsoft.fcare.core.data.entity.rating.RatingRecord
import com.wxsoft.fcare.databinding.*
import com.wxsoft.fcare.ui.CommitEventAction
import com.wxsoft.fcare.ui.EmrEventAction
import com.wxsoft.fcare.ui.common.PictureAdapter

class EmrAdapter constructor(private val owner: LifecycleOwner,
                             private val pool:RecyclerView.RecycledViewPool) :
    RecyclerView.Adapter<ItemViewHolder>() {

    val pictureAdapter=PictureAdapter(owner,2)
    private var action: EmrEventAction?=null

    fun setActionListener(actions: EmrEventAction){
        this.action=actions
    }

    private var commitAction:CommitEventAction?=null

    fun setCommitEventActionListener(commitActions:CommitEventAction){
        this.commitAction=commitActions
    }

    private val differ = AsyncListDiffer<EmrItem>(this, DiffCallback)

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    var items: List<EmrItem> = emptyList()
        set(value) {
            field = value
            differ.submitList(value)
        }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        when (holder) {
            is ItemViewHolder.NoneViewHolder -> holder.binding.apply {
                lifecycleOwner = owner
                item=differ.currentList[position]
                visiable=position<differ.currentList.size-1
                action?.let {
                    root.setOnClickListener {
                        action?.onOpen(differ.currentList[position].code!!) }
                }
                executePendingBindings()
            }

            is ItemViewHolder.InterventionViewHolder -> holder.binding.apply {
                lifecycleOwner = this@EmrAdapter.owner
                item=differ.currentList[position]
                visiable=position<differ.currentList.size-1
                action?.let {
                    root.setOnClickListener {
                        action?.onOpen(differ.currentList[position].code!!) }
                }
                intervention=differ.currentList[position].result as Intervention
                executePendingBindings()
            }
            is ItemViewHolder.ProfileViewHolder -> holder.binding.apply {
                item=differ.currentList[position]
                action?.let {
                    root.setOnClickListener {
                        action?.onOpen(differ.currentList[position].code!!) }
                }

                visiable= position<differ.currentList.size-1
                patient=differ.currentList[position].result  as Patient
                lifecycleOwner = owner
                executePendingBindings()
            }

            is ItemViewHolder.EcgViewHolder -> holder.binding.apply {

                item = differ.currentList[position]

                val presenter = differ.currentList[position].result as ElectroCardiogram
                ecg = presenter
                visiable= position<differ.currentList.size-1
                action=commitAction
                list.adapter = pictureAdapter
                pictureAdapter.remotes = presenter.attachments.map { it.httpUrl }
                lifecycleOwner = owner
                executePendingBindings()

            }

            is ItemViewHolder.RatingViewHolder -> holder.binding.apply {

                item = differ.currentList[position]


                if(list.adapter==null){
                    list.adapter = EmrRatingRecordAdapter(owner,action)
                }
                action?.let {
                    newOne.setOnClickListener {
                        action?.onNew(differ.currentList[position].code!!) }
                }
                (list.adapter as? EmrRatingRecordAdapter)?.submitList((differ.currentList[position].result as? List<RatingRecord>)?: emptyList())

                visiable= position<differ.currentList.size-1
                lifecycleOwner = owner
                executePendingBindings()

            }

            is ItemViewHolder.DisDiagnoseViewHolder -> holder.binding.apply {

                item = differ.currentList[position]
                action?.let {
                    root.setOnClickListener {
                        action?.onOpen(differ.currentList[position].code!!) }
                }
                visiable= position<differ.currentList.size-1
                charge=differ.currentList[position].result  as DisChargeDiagnosis
                lifecycleOwner = owner
                executePendingBindings()

            }

            is ItemViewHolder.MedicalHistoryViewHolder -> holder.binding.apply {
                item = differ.currentList[position]
                action?.let {
                    root.setOnClickListener {
                        action?.onOpen(differ.currentList[position].code!!) }
                }
                val presenter = differ.currentList[position].result as MedicalHistory
                medhistory = presenter
                visiable= position<differ.currentList.size-1
                lifecycleOwner = owner
                executePendingBindings()
            }

            is ItemViewHolder.PacsViewHolder -> holder.binding.apply {
                item = differ.currentList[position]
                action?.let {
                    root.setOnClickListener {
                        action?.onOpen(differ.currentList[position].code!!) }
                }
                val presenter = differ.currentList[position].result as Pacs
                intervention = presenter
                visiable= position<differ.currentList.size-1
                lifecycleOwner = owner
                executePendingBindings()
            }

            is ItemViewHolder.StringViewHolder -> holder.binding.apply {
                item = differ.currentList[position]
                action?.let {
                    root.setOnClickListener {
                        action?.onOpen(differ.currentList[position].code!!) }
                }
                detail = differ.currentList[position].result.toString()

                visiable= position<differ.currentList.size-1
                lifecycleOwner = owner
                executePendingBindings()
            }


            is ItemViewHolder.CatheterViewHolder -> holder.binding.apply {
                item = differ.currentList[position]
                action?.let {
                    root.setOnClickListener {
                        action?.onOpen(differ.currentList[position].code!!) }
                }
                lifecycleOwner = owner
                executePendingBindings()
            }

            is ItemViewHolder.OTViewHolder -> holder.binding.apply {
                item = differ.currentList[position]
                action?.let {
                    root.setOnClickListener {
                        action?.onOpen(differ.currentList[position].code!!) }
                }
                ot=differ.currentList[position].result as OutCome
                lifecycleOwner = owner
                executePendingBindings()
            }

            is ItemViewHolder.CABGViewHolder -> holder.binding.apply {
                item = differ.currentList[position]
                visiable=position<differ.currentList.size-1
                action?.let {
                    root.setOnClickListener {
                        action?.onOpen(differ.currentList[position].code!!) }
                }
                intervention=differ.currentList[position].result as CABG
                lifecycleOwner = owner
                executePendingBindings()
            }

            is ItemViewHolder.ListViewHolder -> holder.binding.apply {
                val emr=differ.currentList[position]
                item = emr
                visiable= position<differ.currentList.size-1
                when(emr.code){
                    ActionRes.ActionType.生命体征->{
                        if(list.adapter==null){
                            list.adapter = EmrItemAdapter<VitalSign>(owner,action)
                        }
                         action?.let {
                             newOne.setOnClickListener {
                                action?.onNew(differ.currentList[position].code!!) }
                        }
                        (list.adapter as? EmrItemAdapter<VitalSign>)?.submitList((emr.result as? List<VitalSign>)?: emptyList<VitalSign>())
                    }

                    ActionRes.ActionType.诊断->{
                        if(list.adapter==null){
                            list.adapter = EmrItemAdapter<Diagnosis>(owner,action)
                        }
                        action?.let {
                            newOne.setOnClickListener {
                                action?.onNew(differ.currentList[position].code!!) }
                        }
                        (list.adapter as? EmrItemAdapter<Diagnosis>)?.submitList((emr.result as? List<Diagnosis>)?: emptyList<Diagnosis>())
                    }
                    ActionRes.ActionType.知情同意书->{
                        if(list.adapter==null){
                            list.adapter = EmrItemAdapter<Talk>(owner,action)
                        }
                        action?.let {
                            newOne.setOnClickListener {
                                action?.onNew(differ.currentList[position].code!!) }
                        }
                        (list.adapter as? EmrItemAdapter<Talk>)?.submitList((emr.result as? List<Talk>)?: emptyList<Talk>())
                    }

                    ActionRes.ActionType.溶栓处置->{
                        if(list.adapter==null){
                            list.adapter = EmrItemAdapter<Thrombolysis>(owner,action)
                        }
                        action?.let {
                            newOne.setOnClickListener {
                                action?.onNew(differ.currentList[position].code!!) }
                        }
                        (list.adapter as? EmrItemAdapter<Thrombolysis>)?.submitList((emr.result as? List<Thrombolysis>)?: emptyList<Thrombolysis>())
                    }

                    ActionRes.ActionType.给药->{
                        if(list.adapter==null){
                            list.adapter = EmrItemAdapter<DrugRecord>(owner,action)
                        }
                        action?.let {
                            newOne.setOnClickListener {
                                action?.onNew(differ.currentList[position].code!!) }
                        }
                        val manager =  LinearLayoutManager(this.container.context);
                        manager.setOrientation(LinearLayoutManager.VERTICAL);
                        list.setLayoutManager(manager);
                        (list.adapter as? EmrItemAdapter<DrugRecord>)?.submitList((emr.result as? List<DrugRecord>)?: emptyList<DrugRecord>())
                    }
                }
                lifecycleOwner = owner
                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            R.layout.item_emr_none -> ItemViewHolder.NoneViewHolder(
                ItemEmrNoneBinding.inflate(inflater, parent, false)
            )
            R.layout.item_emr_simple_string -> ItemViewHolder.StringViewHolder(
                ItemEmrSimpleStringBinding.inflate(inflater, parent, false)
            )
            R.layout.item_emr_profile -> ItemViewHolder.ProfileViewHolder(
                ItemEmrProfileBinding.inflate(inflater, parent, false)
            )
            R.layout.item_emr_ecg->ItemViewHolder.EcgViewHolder(
                ItemEmrEcgBinding.inflate(inflater,parent,false).apply {
                    list.setRecycledViewPool(pool)
                }
            )

            R.layout.item_emr_rating->ItemViewHolder.RatingViewHolder(
                ItemEmrRatingBinding.inflate(inflater,parent,false)
            )
            R.layout.item_emr_medical_history -> ItemViewHolder.MedicalHistoryViewHolder(
                ItemEmrMedicalHistoryBinding.inflate(inflater,parent,false)
            )
            R.layout.item_emr_catheter -> ItemViewHolder.CatheterViewHolder(
                ItemEmrCatheterBinding.inflate(inflater,parent,false)
            )

            R.layout.item_emr_dis_diagnosis -> ItemViewHolder.DisDiagnoseViewHolder(
                ItemEmrDisDiagnosisBinding.inflate(inflater,parent,false)
            )
            R.layout.item_emr_item_list->ItemViewHolder.ListViewHolder(
                ItemEmrItemListBinding.inflate(inflater,parent,false).apply {
                    list.setRecycledViewPool(pool)
                }
            )

            R.layout.item_emr_ct->ItemViewHolder.PacsViewHolder(
                ItemEmrCtBinding.inflate(inflater,parent,false)
            )

            R.layout.item_emr_opration_d->ItemViewHolder.InterventionViewHolder(
                ItemEmrOprationDBinding.inflate(inflater,parent,false)
            )

            R.layout.item_emr_cabg->ItemViewHolder.CABGViewHolder(
                ItemEmrCabgBinding.inflate(inflater,parent,false)
            )

            R.layout.item_emr_outcome->ItemViewHolder.OTViewHolder(
                ItemEmrOutcomeBinding.inflate(inflater,parent,false)
            )

            else -> throw IllegalStateException("Unknown viewType $viewType")
        }
    }


    override fun getItemViewType(position: Int): Int {
        val item=differ.currentList[position]
        return when (item.result) {
            null->R.layout.item_emr_none
            is Patient -> R.layout.item_emr_profile
            is ElectroCardiogram -> R.layout.item_emr_ecg
            is MedicalHistory -> R.layout.item_emr_medical_history
            is Diagnosis->R.layout.item_emr_list_item_diagnose
            is CheckBody->R.layout.item_emr_simple_string
            is Measure->R.layout.item_emr_simple_string
            is DisChargeDiagnosis->R.layout.item_emr_dis_diagnosis
            is Pacs->R.layout.item_emr_ct
            is Intervention->R.layout.item_emr_opration_d
            is CABG->R.layout.item_emr_cabg
            is OutCome->R.layout.item_emr_outcome
            is List<*> ->{
                when(item.code){
                    ActionRes.ActionType.GRACE->{
                        R.layout.item_emr_rating
                    }
                    ActionRes.ActionType.生命体征,
                    ActionRes.ActionType.诊断->{
                        R.layout.item_emr_item_list
                    }
                    ActionRes.ActionType.知情同意书->{
                        R.layout.item_emr_item_list
                    }
                    ActionRes.ActionType.溶栓处置->{
                        R.layout.item_emr_item_list
                    }
                    ActionRes.ActionType.给药->{
                        R.layout.item_emr_item_list
                    }

                    else->R.layout.item_emr_none
                }
            }
            else->R.layout.item_emr_none
//            else -> throw IllegalStateException("Unknown view type at position $position")
        }
    }


    object DiffCallback : DiffUtil.ItemCallback<EmrItem>() {
        override fun areItemsTheSame(oldItem: EmrItem, newItem: EmrItem): Boolean {
            val result1 = oldItem.result
            val result2 = newItem.result
            return when {
                result1 is Patient && result2 is Patient ->
                    result1 == result2

                result1 is CheckBody && result2 is CheckBody ->
                    result1.id == result2.id && oldItem.code == newItem.code

                result1 is ElectroCardiogram && result2 is ElectroCardiogram ->
                    result1.id == result2.id && oldItem.code == newItem.code

                result1 is MedicalHistory && result2 is MedicalHistory ->
                    result1.id == result2.id && oldItem.code == newItem.code

                result1 is VitalSign && result2 is VitalSign ->
                    result1.id == result2.id && oldItem.code == newItem.code
                result1 is Measure && result2 is Measure ->
                    oldItem.code == newItem.code

                result1 is Diagnosis && result2 is Diagnosis ->
                    result1.id == result2.id && oldItem.code == newItem.code

                result1 is DisChargeDiagnosis && result2 is DisChargeDiagnosis ->
                    result1.id == result2.id && oldItem.code == newItem.code

                result1 is Pacs && result2 is Pacs ->
                    result1.id == result2.id && oldItem.code == newItem.code

                result1 is Intervention && result2 is Intervention ->
                    result1.id == result2.id && oldItem.code == newItem.code

                result1 is OutCome && result2 is OutCome ->
                    result1.id == result2.id && oldItem.code == newItem.code

                result1 is CABG && result2 is CABG ->
                    result1.id == result2.id && oldItem.code == newItem.code

                result1 is List<*> && result2 is List<*> ->
                    oldItem.code == newItem.code && result1.size  == result2.size
                else -> oldItem.code == newItem.code
            }
        }

        override fun areContentsTheSame(oldItem: EmrItem, newItem: EmrItem): Boolean {
            val result1 = oldItem.result
            val result2 = newItem.result
            return when {
                result1 is Patient && result2 is Patient ->
                    result1 == result2

                result1 is ElectroCardiogram && result2 is ElectroCardiogram ->
                    result1.id == result2.id && oldItem.code == newItem.code

                result1 is MedicalHistory && result2 is MedicalHistory ->
                    result1.id == result2.id && oldItem.code == newItem.code

                result1 is CheckBody && result2 is CheckBody ->
                    result1.id == result2.id && oldItem.code == newItem.code

                result1 is VitalSign && result2 is VitalSign ->
                    result1.id == result2.id && oldItem.code == newItem.code

                result1 is Diagnosis && result2 is Diagnosis ->
                    result1.id == result2.id && oldItem.code == newItem.code

                result1 is DisChargeDiagnosis && result2 is DisChargeDiagnosis ->
                    result1.id == result2.id && oldItem.code == newItem.code

                result1 is Pacs && result2 is Pacs ->
                    result1.id == result2.id && oldItem.code == newItem.code

                result1 is Intervention && result2 is Intervention ->
                    result1.id == result2.id && oldItem.code == newItem.code

                result1 is CABG && result2 is CABG ->
                    result1.id == result2.id && oldItem.code == newItem.code

                result1 is OutCome && result2 is OutCome ->
                    result1.id == result2.id && oldItem.code == newItem.code

                result1 is List<*> && result2 is List<*> ->
                    oldItem.code == newItem.code && result1  == result2
                result1 is Measure && result2 is Measure ->
                    oldItem.code == newItem.code

                else -> oldItem.code == newItem.code
            }
        }
    }
}

sealed class ItemViewHolder(binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

    //无结果
    class NoneViewHolder(
        val binding: ItemEmrNoneBinding
    ) : ItemViewHolder(binding)

    //基本信息
    class ProfileViewHolder(
        val binding: ItemEmrProfileBinding
    ) : ItemViewHolder(binding)

    //心电图
    class EcgViewHolder(
        val binding: ItemEmrEcgBinding
    ) : ItemViewHolder(binding)

    //评分
    class RatingViewHolder(
        val binding: ItemEmrRatingBinding
    ) : ItemViewHolder(binding)

    //病史
    class MedicalHistoryViewHolder(
        val binding: ItemEmrMedicalHistoryBinding
    ): ItemViewHolder(binding)

    //生命体征
    class VitalViewHolder(
        val binding: ItemEmrListItemVitalSignsBinding
    ): ItemViewHolder(binding)

    //知情同意书
    class TalkViewHolder(
        val binding:ItemEmrListItemInformedConsentBinding
    ): ItemViewHolder(binding)

    //患者用药
    class DrugViewHolder(
        val binding:ItemEmrListItemDrugBinding
    ): ItemViewHolder(binding)

    //诊断
    class DiagnoseViewHolder(
        val binding: ItemEmrListItemDiagnoseBinding
    ): ItemViewHolder(binding)

    //导管室操作
    class CatheterViewHolder(
        val binding: ItemEmrCatheterBinding
    ): ItemViewHolder(binding)

    //普通文本
    class StringViewHolder(
        val binding: ItemEmrSimpleStringBinding
    ): ItemViewHolder(binding)

    //普通文本
    class ListViewHolder(
        val binding: ItemEmrItemListBinding
    ): ItemViewHolder(binding)

    //溶栓处置
    class ThrombolysisViewHolder(
        val binding: ItemEmrListItemThrombolysisBinding
    ): ItemViewHolder(binding)

    //出院诊断
    class DisDiagnoseViewHolder(
        val binding: ItemEmrDisDiagnosisBinding
    ): ItemViewHolder(binding)

    //CT
    class PacsViewHolder(
        val binding: ItemEmrCtBinding
    ): ItemViewHolder(binding)

    //导管室
    class InterventionViewHolder(
        val binding: ItemEmrOprationDBinding
    ): ItemViewHolder(binding)

    //CABG
    class CABGViewHolder(
        val binding: ItemEmrCabgBinding
    ): ItemViewHolder(binding)

    //转归
    class OTViewHolder(
        val binding: ItemEmrOutcomeBinding
    ): ItemViewHolder(binding)

}