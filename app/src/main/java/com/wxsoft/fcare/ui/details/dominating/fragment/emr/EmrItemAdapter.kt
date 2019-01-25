package com.wxsoft.fcare.ui.details.dominating.fragment.emr

import android.arch.lifecycle.LifecycleOwner
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.data.entity.Diagnosis
import com.wxsoft.fcare.core.data.entity.VitalSign
import com.wxsoft.fcare.core.data.entity.rating.Rating
import com.wxsoft.fcare.data.dictionary.ActionRes.ActionType.Companion.生命体征
import com.wxsoft.fcare.data.dictionary.ActionRes.ActionType.Companion.诊断
import com.wxsoft.fcare.databinding.ItemEmrListItemDiagnoseBinding
import com.wxsoft.fcare.databinding.ItemEmrListItemVitalSignsBinding
import com.wxsoft.fcare.databinding.ItemEmrRatingBinding
import com.wxsoft.fcare.ui.EmrEventAction

class EmrItemAdapter<T> constructor(private val owner: LifecycleOwner, private val  action: EmrEventAction?) :
    ListAdapter<T,ItemViewHolder>(DiffCallback<T>()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            R.layout.item_emr_rating->ItemViewHolder.RatingViewHolder(
                ItemEmrRatingBinding.inflate(inflater,parent,false)
            )

            R.layout.item_emr_list_item_vital_signs -> ItemViewHolder.VitalViewHolder(
                ItemEmrListItemVitalSignsBinding.inflate(inflater,parent,false)
            )

            R.layout.item_emr_list_item_diagnose -> ItemViewHolder.DiagnoseViewHolder(
                ItemEmrListItemDiagnoseBinding.inflate(inflater,parent,false)
            )

            else -> throw IllegalStateException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        when (holder){
            is ItemViewHolder.VitalViewHolder -> holder.binding.apply{

                vital = getItem(position)  as VitalSign
                root.setOnClickListener { action?.onOpen(生命体征,vital?.id?:"") }
                lifecycleOwner = owner
                executePendingBindings()
            }

            is ItemViewHolder.DiagnoseViewHolder -> holder.binding.apply{

                diagnose = getItem(position)  as Diagnosis
                root.setOnClickListener { action?.onOpen(诊断,diagnose?.id?:"") }
                lifecycleOwner = owner
                executePendingBindings()
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position)){
            is VitalSign-> R.layout.item_emr_list_item_vital_signs
            is Rating-> R.layout.item_emr_rating
            is Diagnosis-> R.layout.item_emr_list_item_diagnose
            else -> throw IllegalStateException("Unknown viewType at position $position")
        }
    }


    class DiffCallback<T> : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
            when{
                oldItem is VitalSign && newItem is VitalSign->oldItem.id==newItem.id
                oldItem is Rating && newItem is Rating->oldItem.id==newItem.id
                oldItem is Diagnosis && newItem is Diagnosis->oldItem.id==newItem.id
            }
            return false
        }

        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
            when{
                oldItem is VitalSign && newItem is VitalSign->oldItem.id==newItem.id
                oldItem is Rating && newItem is Rating->oldItem.id==newItem.id
                oldItem is Diagnosis && newItem is Diagnosis->oldItem.id==newItem.id
            }
            return false
        }
    }
}
