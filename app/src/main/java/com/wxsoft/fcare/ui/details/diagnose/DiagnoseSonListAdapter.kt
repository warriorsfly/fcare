package com.wxsoft.fcare.ui.details.diagnose

import androidx.lifecycle.LifecycleOwner
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.BR
import com.wxsoft.fcare.core.data.entity.Dictionary
import com.wxsoft.fcare.databinding.ItemDiagnoseIllnessBinding
import com.wxsoft.fcare.databinding.ItemDiagnoseSonListBinding
import kotlinx.android.synthetic.main.item_diagnose_illness.view.*

class DiagnoseSonListAdapter constructor(private val lifecycleOwner: LifecycleOwner, val viewModel: DiagnoseViewModel) :
    androidx.recyclerview.widget.RecyclerView.Adapter<DiagnoseSonListAdapter.ItemViewHolder>() {

    private val differ = AsyncListDiffer<Dictionary>(this, DiffCallback)

    var section:Int = 0

    var items: List<Dictionary> = emptyList()
        set(value) {
            field = value
            differ.submitList(value)
        }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        holder.binding.apply {
            differ.currentList[position].section = section
            if (section == 4){
                when(differ.currentList[position].itemName){
                    "急危" -> {
                        root.illness_container.setBackgroundResource(R.color.seriousnessest)
                        root.illness_icon.setImageResource(R.drawable.ic_item_icon_illness_level_one)
                    }
                    "急重" -> {
                        root.illness_container.setBackgroundResource(R.color.seriousness)
                        root.illness_icon.setImageResource(R.drawable.ic_item_icon_illness_level_two)
                    }
                    "急症" -> {
                        root.illness_container.setBackgroundResource(R.color.emergency)
                        root.illness_icon.setImageResource(R.drawable.ic_item_icon_illness_level_three)
                    }
                    "亚急症" -> {
                        root.illness_container.setBackgroundResource(R.color.lowemergency)
                        root.illness_icon.setImageResource(R.drawable.ic_item_icon_illness_level_four)
                    }
                    "非急症" -> {
                        root.illness_container.setBackgroundResource(R.color.lightemergency)
                        root.illness_icon.setImageResource(R.drawable.ic_item_icon_illness_level_five)
                    }
                }
            }
            setVariable(BR.item, differ.currentList[position])
            setVariable(BR.listener,viewModel)
            lifecycleOwner = this@DiagnoseSonListAdapter.lifecycleOwner
            executePendingBindings()

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return when(section){
            3->{val binding: ViewDataBinding = ItemDiagnoseSonListBinding.inflate(LayoutInflater.from(parent.context),parent,  false)
                ItemViewHolder(binding)
            }
            4->{val binding: ViewDataBinding = ItemDiagnoseIllnessBinding.inflate(LayoutInflater.from(parent.context),parent,  false)
                ItemViewHolder(binding)
            }
            else ->{val binding: ViewDataBinding = ItemDiagnoseSonListBinding.inflate(LayoutInflater.from(parent.context),parent,  false)
                ItemViewHolder(binding)
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


    object DiffCallback : DiffUtil.ItemCallback<Dictionary>() {
        override fun areItemsTheSame(oldItem: Dictionary, newItem: Dictionary): Boolean {

            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Dictionary, newItem: Dictionary): Boolean {

            return oldItem.id == newItem.id
        }
    }


}