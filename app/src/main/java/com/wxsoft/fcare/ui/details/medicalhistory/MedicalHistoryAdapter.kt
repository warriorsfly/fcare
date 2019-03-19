package com.wxsoft.fcare.ui.details.medicalhistory

import androidx.lifecycle.LifecycleOwner
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.wxsoft.fcare.BR
import com.wxsoft.fcare.R
import com.wxsoft.fcare.databinding.ItemMedicalHistoryOtherBinding
import com.wxsoft.fcare.databinding.ItemMedicalHistoryVoiceBinding
import kotlinx.android.synthetic.main.item_medical_history_other.view.*

class MedicalHistoryAdapter constructor(private val owner: LifecycleOwner, val viewModel: MedicalHistoryViewModel) :
    RecyclerView.Adapter<MedicalHistoryAdapter.ItemViewHolder>() {

    private var titleArray:Array<String> = arrayOf("", "既往病史","近期药史", "病历提供者")

    override fun getItemCount(): Int {
        return 4
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.binding.apply {
            lifecycleOwner = owner
            if (position == 0){//主诉、现病史
                setVariable(BR.listener,viewModel)

            }else if(position==2){

                if (root.medical_other_items_rv.adapter == null){
                    root.medical_other_items_rv.layoutManager=LinearLayoutManager(root.context)
                    val adapter = DrugHistoryItemAdapter(owner)
                    viewModel.drugHistory.observe(owner, Observer {
                        adapter.submitList(it)
                    })
                    root.medical_other_title_name.text = titleArray[position]
                    root.medical_other_items_rv.adapter = adapter
                }
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

        return when (position) {
            0 -> {
                 R.layout.item_medical_history_voice
            }
            1 -> {
                R.layout.item_medical_history_other
            }
            2 -> {
                R.layout.item_medical_history_other
            }
            3 -> {
                R.layout.item_medical_history_other
            }

            else -> throw IllegalStateException("error position!")
        }
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