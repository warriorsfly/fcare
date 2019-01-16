package com.wxsoft.fcare.ui.details.dominating.fragment.emr

import android.arch.lifecycle.LifecycleOwner
import android.databinding.ViewDataBinding
import android.support.v7.recyclerview.extensions.AsyncListDiffer
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.data.entity.CheckBody
import com.wxsoft.fcare.core.data.entity.ElectroCardiogram
import com.wxsoft.fcare.core.data.entity.EmrItem
import com.wxsoft.fcare.core.data.entity.Patient
import com.wxsoft.fcare.core.data.entity.rating.RatingRecord
import com.wxsoft.fcare.data.dictionary.ActionRes
import com.wxsoft.fcare.core.data.entity.*
import com.wxsoft.fcare.databinding.*
import com.wxsoft.fcare.ui.CommitEventAction
import com.wxsoft.fcare.ui.EventActions
import com.wxsoft.fcare.ui.common.PictureAdapter

class EmrAdapter constructor(private val lifecycleOwner: LifecycleOwner) :
    RecyclerView.Adapter<ItemViewHolder>() {

    val pictureAdapter=PictureAdapter(lifecycleOwner,2)
    private var action:EventActions?=null

    fun setActionListener(actions: EventActions){
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
                setLifecycleOwner(lifecycleOwner)
                item=differ.currentList[position]
                visiable=position<differ.currentList.size-1
                action?.let {
                    root.setOnClickListener {
                        action?.onOpen(differ.currentList[position].code!!) }
                }
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
                setLifecycleOwner(lifecycleOwner)
                executePendingBindings()
            }

            is ItemViewHolder.EcgViewHolder -> holder.binding.apply {

                item = differ.currentList[position]

                val presenter = differ.currentList[position].result as ElectroCardiogram
                ecg = presenter
                visiable= position<differ.currentList.size-1
                action=commitAction
                list.adapter = this@EmrAdapter.pictureAdapter
                this@EmrAdapter.pictureAdapter.remotes = presenter.attachments.map { it.httpUrl }

            }

            is ItemViewHolder.RatingViewHolder -> holder.binding.apply {

                item = differ.currentList[position]
                action?.let {
                    newOne.setOnClickListener {
                        action?.onOpen(differ.currentList[position].code!!) }
                }
                visiable= position<differ.currentList.size-1

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
            }

            is ItemViewHolder.StringViewHolder -> holder.binding.apply {
                item = differ.currentList[position]
                action?.let {
                    root.setOnClickListener {
                        action?.onOpen(differ.currentList[position].code!!) }
                }
                detail = (differ.currentList[position].result as CheckBody).toString()

                visiable= position<differ.currentList.size-1
            }

            is ItemViewHolder.VitalViewHolder -> holder.binding.apply {
                item = differ.currentList[position]
                action?.let {
                    root.setOnClickListener {
                        action?.onOpen(differ.currentList[position].code!!) }
                }
                val presenter = differ.currentList[position].result as VitalSign
                vital = presenter
                visiable= position<differ.currentList.size-1
            }

            is ItemViewHolder.DiagnoseViewHolder -> holder.binding.apply {
                item = differ.currentList[position]
                action?.let {
                    root.setOnClickListener {
                        action?.onOpen(differ.currentList[position].code!!) }
                }
                val presenter = differ.currentList[position].result as Diagnosis
                diagnose = presenter
                visiable= position<differ.currentList.size-1
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
                ItemEmrEcgBinding.inflate(inflater,parent,false)
            )

            R.layout.item_emr_rating->ItemViewHolder.RatingViewHolder(
                ItemEmrRatingBinding.inflate(inflater,parent,false)
            )
            R.layout.item_emr_medical_history -> ItemViewHolder.MedicalHistoryViewHolder(
                ItemEmrMedicalHistoryBinding.inflate(inflater,parent,false)
            )
            R.layout.item_emr_vital_signs -> ItemViewHolder.VitalViewHolder(
                ItemEmrVitalSignsBinding.inflate(inflater,parent,false)
            )
            R.layout.item_emr_diagnose -> ItemViewHolder.DiagnoseViewHolder(
                ItemEmrDiagnoseBinding.inflate(inflater,parent,false)
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
            is Diagnosis->R.layout.item_emr_diagnose
            is VitalSign->R.layout.item_emr_vital_signs
            is CheckBody->R.layout.item_emr_simple_string
            is List<*> ->{
                when(item.code){
                    ActionRes.ActionType.GRACE->{
                        R.layout.item_emr_rating
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
                result1 is Any && result2 is Any ->
                    oldItem.code == newItem.code && result1 == result2

                result1 is Patient && result2 is Patient ->
                    result1.id == result2.id && oldItem.code == newItem.code

                result1 is CheckBody && result2 is CheckBody ->
                    result1.id == result2.id && oldItem.code == newItem.code

                result1 is ElectroCardiogram && result2 is ElectroCardiogram ->
                    result1.id == result2.id && oldItem.code == newItem.code

                result1 is MedicalHistory && result2 is MedicalHistory ->
                    result1.id == result2.id && oldItem.code == newItem.code

                result1 is VitalSign && result2 is VitalSign ->
                    result1.id == result2.id && oldItem.code == newItem.code

                result1 is Diagnosis && result2 is Diagnosis ->
                    result1.id == result2.id && oldItem.code == newItem.code


                result1 is List<*> && result2 is List<*> ->
                    oldItem.code == newItem.code
                else -> false
            }
        }

        override fun areContentsTheSame(oldItem: EmrItem, newItem: EmrItem): Boolean {
            val result1 = oldItem.result
            val result2 = newItem.result
            return when {
                result1 is Any && result2 is Any ->
                    oldItem.code == newItem.code

                result1 is Patient && result2 is Patient ->
                    result1.id == result2.id && oldItem.code == newItem.code

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

                result1 is List<*> && result2 is List<*> ->
                    oldItem.code == newItem.code

                else -> false
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
        val binding: ItemEmrVitalSignsBinding
    ): ItemViewHolder(binding)

    //院前诊断
    class DiagnoseViewHolder(
        val binding: ItemEmrDiagnoseBinding
    ): ItemViewHolder(binding)

    //普通文本
    class StringViewHolder(
        val binding: ItemEmrSimpleStringBinding
    ): ItemViewHolder(binding)

}