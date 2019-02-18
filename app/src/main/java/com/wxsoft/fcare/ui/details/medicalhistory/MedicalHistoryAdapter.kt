package com.wxsoft.fcare.ui.details.medicalhistory

import androidx.lifecycle.LifecycleOwner
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.wxsoft.fcare.BR
import com.wxsoft.fcare.R
import com.wxsoft.fcare.databinding.ItemMedicalHistoryOtherBinding
import com.wxsoft.fcare.databinding.ItemMedicalHistoryVoiceBinding
import kotlinx.android.synthetic.main.item_medical_history_other.view.*

class MedicalHistoryAdapter constructor(private val owner: LifecycleOwner, val viewModel: MedicalHistoryViewModel) :
    androidx.recyclerview.widget.RecyclerView.Adapter<MedicalHistoryAdapter.ItemViewHolder>() {

    private var titleArray:Array<String> = arrayOf("", "既往病史", "病历提供者")

    override fun getItemCount(): Int {
        return 3
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.binding.apply {
            lifecycleOwner = owner
            if (position == 0){//主诉、现病史
                setVariable(BR.listener,viewModel)

            }else{//既往病史、病历提供者
                if (root.medical_other_items_rv.adapter == null){
                    val adapter = MedicalHistoryItemAdapter(owner,viewModel)
                    adapter.section = position
                    root.medical_other_title_name.text = titleArray[position]
                    root.medical_other_items_rv.adapter = adapter
                }
            }
            executePendingBindings()
        }


    }

    override fun getItemViewType(position: Int): Int {
        var type = 0
        when (position) {
            0 -> {
                type = R.layout.item_medical_history_voice
            }
            1 -> {
                type = R.layout.item_medical_history_other
            }
            2 -> {
                type = R.layout.item_medical_history_other
            }
        }
        return type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        when (viewType) {
            R.layout.item_medical_history_voice -> {
                val binding = ItemMedicalHistoryVoiceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return ItemViewHolder(binding)
            }
            R.layout.item_medical_history_other -> {
                val binding = ItemMedicalHistoryOtherBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return ItemViewHolder(binding)
            }
            else -> {
                val binding: ViewDataBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_medical_history_voice,
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