package com.wxsoft.fcare.ui.detail.fragment.emr

import android.arch.lifecycle.LifecycleOwner
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.wxsoft.fcare.BR
import com.wxsoft.fcare.R
import com.wxsoft.fcare.databinding.*
import com.wxsoft.fcare.ui.detail.PatientDetailViewModel

class EmrAdapter constructor(private val lifecycleOwner: LifecycleOwner,private val viewModel: PatientDetailViewModel) :  RecyclerView.Adapter<EmrAdapter.ItemViewHolder>() {

    private lateinit var list:List<Any>

    override fun getItemCount(): Int {
        return 6
    }
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        holder.binding.apply {
            setLifecycleOwner(lifecycleOwner)
            executePendingBindings()
        }
//        when(list[position]){
//            is VitalSign->{
//                val tholder=holder as ItemViewHolder2
////                tholder.binding.setVariable()
//            }
//        }

        var binding=holder.binding
        binding.setVariable(BR.listener,viewModel)
        binding.executePendingBindings()
    }

    override fun getItemViewType(position: Int): Int {
        var type:Int = 0
        when(position){
            0->{type = R.layout.item_emr_patient_info}
            1->{type = R.layout.item_emr_patient_morbidity}
            2->{type = R.layout.item_emr_come_hospital_type}
            3->{type = R.layout.item_emr_first_contact}
            4->{type = R.layout.item_emr_vital_signs}
            5->{type = R.layout.item_emr_assistant_examination}
        }
        return type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        when(viewType) {
            R.layout.item_emr_patient_info -> {
                val binding = ItemEmrPatientInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return ItemViewHolder(binding)
            }
            R.layout.item_emr_patient_morbidity -> {
                val binding = ItemEmrPatientMorbidityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return ItemViewHolder(binding)
            }
            R.layout.item_emr_come_hospital_type -> {
                val binding = ItemEmrComeHospitalTypeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return ItemViewHolder(binding)
            }
            R.layout.item_emr_first_contact -> {
                val binding = ItemEmrFirstContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return ItemViewHolder(binding)
            }
            R.layout.item_emr_vital_signs -> {
                val binding = ItemEmrVitalSignsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return ItemViewHolder(binding)
            }
            R.layout.item_emr_assistant_examination -> {
                val binding = ItemEmrAssistantExaminationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return ItemViewHolder(binding)
            }
            else-> {
                val binding: ViewDataBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_emr_patient_info,
                    parent,
                    false
                )

                return ItemViewHolder(binding)
            }
        }

    }

    open class ItemViewHolder(binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        var binding: ViewDataBinding
            private set

        init {
            this.binding = binding
        }
    }

    class ItemViewHolder2(binding: ItemEmrPatientInfoBinding) :ItemViewHolder(binding)

    class ItemViewHolder3(binding: ViewDataBinding) :ItemViewHolder(binding)


}