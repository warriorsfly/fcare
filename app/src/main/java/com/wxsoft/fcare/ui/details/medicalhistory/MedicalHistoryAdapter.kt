package com.wxsoft.fcare.ui.details.medicalhistory

import android.arch.lifecycle.LifecycleOwner
import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.wxsoft.fcare.BR
import com.wxsoft.fcare.R
import com.wxsoft.fcare.databinding.ItemMedicalHistoryOtherBinding
import com.wxsoft.fcare.databinding.ItemMedicalHistoryPhotoBinding
import com.wxsoft.fcare.databinding.ItemMedicalHistoryVoiceBinding
import com.wxsoft.fcare.generated.callback.OnClickListener
import com.wxsoft.fcare.ui.common.AttachmentAdapter
import com.wxsoft.fcare.ui.common.ForNewItem
import kotlinx.android.synthetic.main.item_medical_history_other.view.*
import kotlinx.android.synthetic.main.item_medical_history_photo.view.*

class MedicalHistoryAdapter constructor(private val lifecycleOwner: LifecycleOwner, val viewModel: MedicalHistoryViewModel, val context: Context) :
    RecyclerView.Adapter<MedicalHistoryAdapter.ItemViewHolder>() {

    var titleArray:Array<String> = arrayOf("","", "既往病史", "病历提供者")

    val photoAdapter:AttachmentAdapter

    init {
        photoAdapter = AttachmentAdapter(lifecycleOwner, OnClickListener(OnClickListener.Listener{ _, view ->
            when(view.tag){
                ForNewItem ->{
                    viewModel.loadPhoto.value = "111"
                }
            }
        },1))
        photoAdapter.attachs= emptyList()
    }

    override fun getItemCount(): Int {
        return 4
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.binding.apply {
            setLifecycleOwner(lifecycleOwner)
            if (position==0){//照片
                if (root.medical_photo_items_rv.adapter == null){
                    root.medical_photo_items_rv.adapter = photoAdapter
                }
            }else if (position == 1){//主诉、现病史
                setVariable(BR.listener,viewModel)

            }else{//既往病史、病历提供者
                if (root.medical_other_items_rv.adapter == null){
                    var adapter = MedicalHistoryItemAdapter(lifecycleOwner,viewModel)
                    adapter.section = position
                    root.medical_other_items_rv.setLayoutManager(GridLayoutManager(context,4))
                    root.medical_other_title_name.setText(titleArray.get(position))
                    root.medical_other_items_rv.adapter = adapter
                }
            }
            executePendingBindings()
        }


    }

    override fun getItemViewType(position: Int): Int {
        var type: Int = 0
        when (position) {
            0 -> {
                type = R.layout.item_medical_history_photo
            }
            1 -> {
                type = R.layout.item_medical_history_voice
            }
            2 -> {
                type = R.layout.item_medical_history_other
            }
            3 -> {
                type = R.layout.item_medical_history_other
            }
        }
        return type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        when (viewType) {
            R.layout.item_medical_history_photo -> {
                val binding = ItemMedicalHistoryPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return ItemViewHolder(binding)
            }
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
                    R.layout.item_medical_history_photo,
                    parent,
                    false
                )

                return ItemViewHolder(binding)
            }
        }
    }


    class ItemViewHolder(binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        var binding: ViewDataBinding
            private set

        init {
            this.binding = binding
        }
    }

}