package com.wxsoft.fcare.ui.discharge

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.data.entity.DisChargeEntity
import com.wxsoft.fcare.databinding.ItemDischargeSonItemBinding

class DisChargeSonAdapter constructor(private val owner: LifecycleOwner, val viewModel: DisChargeSonViewModel) :
    RecyclerView.Adapter<DisChargeSonAdapter.ItemViewHolder>(){

    private val differ = AsyncListDiffer<DisChargeEntity>(this, DiffCallback)

    var items: List<DisChargeEntity> = emptyList()
        set(value) {
            field = value
            differ.submitList(value)
        }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        holder.binding.apply {
            val tt = differ.currentList[position]
            item = tt
            if (tt.enumItemId.equals("303-1")){
                radio1.setOnCheckedChangeListener({ group, checkedId ->
                    when(checkedId){
                        R.id.radio_btn_1 ->{tt.enumItemId1 = "304-1"}
                        R.id.radio_btn_2 ->{tt.enumItemId1 = "304-2"}
                    }
                })
            }
            if (tt.enumItemId.equals("303-2")){
                radio2.setOnCheckedChangeListener({ group, checkedId ->
                    when(checkedId){
                        R.id.radio2_btn_1 ->{tt.enumItemId1 = "305-1"}
                        R.id.radio2_btn_2 ->{tt.enumItemId1 = "305-2"}
                        R.id.radio2_btn_3 ->{tt.enumItemId1 = "305-3"}
                        R.id.radio2_btn_4 ->{tt.enumItemId1 = "305-4"}
                    }
                })
            }
            if (tt.enumItemId.equals("303-3")){
                radio3.setOnCheckedChangeListener({ group, checkedId ->
                    when(checkedId){
                        R.id.radio3_btn_1 ->{tt.enumItemId1 = "250-1"}
                        R.id.radio3_btn_2 ->{tt.enumItemId1 = "250-2"}
                        R.id.radio3_btn_3 ->{tt.enumItemId1 = "250-3"}
                        R.id.radio3_btn_4 ->{tt.enumItemId1 = "250-4"}
                    }
                })
            }
            if (tt.enumItemId.equals("303-5")){
                radio4.setOnCheckedChangeListener({ group, checkedId ->
                    when(checkedId){
                        R.id.radio4_btn_1 ->{tt.enumItemId1 = "306-1"}
                        R.id.radio4_btn_2 ->{tt.enumItemId1 = "306-2"}
                    }
                })
            }
            if (tt.enumItemId.equals("251-4")){
                radio5.setOnCheckedChangeListener({ group, checkedId ->
                    when(checkedId){
                        R.id.radio5_btn_1 ->{tt.enumItemId1 = "302-1"}
                        R.id.radio5_btn_2 ->{tt.enumItemId1 = "302-2"}
                    }
                })
            }
            viewModel = this@DisChargeSonAdapter.viewModel
            lifecycleOwner = owner
            executePendingBindings()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val binding: ItemDischargeSonItemBinding =
            ItemDischargeSonItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    class ItemViewHolder(binding: ItemDischargeSonItemBinding) : RecyclerView.ViewHolder(binding.root) {
        var binding: ItemDischargeSonItemBinding
            private set
        init {
            this.binding = binding
        }
    }


    object DiffCallback : DiffUtil.ItemCallback<DisChargeEntity>() {
        override fun areItemsTheSame(oldItem: DisChargeEntity, newItem: DisChargeEntity): Boolean {

            return oldItem.enumItemId == newItem.enumItemId
        }

        override fun areContentsTheSame(oldItem: DisChargeEntity, newItem: DisChargeEntity): Boolean {

            return oldItem.enumItemId == newItem.enumItemId
        }
    }



}