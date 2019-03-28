package com.wxsoft.fcare.ui.details.diagnose

import android.graphics.Color
import androidx.lifecycle.LifecycleOwner
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.wxsoft.fcare.core.BR
import com.wxsoft.fcare.core.data.entity.Dictionary
import com.wxsoft.fcare.databinding.ItemDiagnoseIllnessBinding
import com.wxsoft.fcare.databinding.ItemDiagnoseSonListBinding
import kotlinx.android.synthetic.main.item_diagnose_illness.view.*

class DiagnoseSonListAdapter constructor(private val lifecycleOwner: LifecycleOwner, val viewModel: DiagnoseViewModel) :
    RecyclerView.Adapter<DiagnoseSonListAdapter.ItemViewHolder>() {

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
            val item = differ.currentList[position]
            item.section = section
            item.position = position
            if (section == 4){
                when(item.id){
                    "216-1" -> {
                        root.illness_icon.setText("I")
                        root.illness_name.setTextColor(if(!item.checked) Color.parseColor("#FE5F55")else Color.parseColor("#FFFFFF"))
                    }
                    "216-2" -> {
                        root.illness_icon.setText("II")
                        root.illness_name.setTextColor(if(!item.checked) Color.parseColor("#FCA542")else Color.parseColor("#FFFFFF"))
                    }
                    "216-3" -> {
                        root.illness_icon.setText("III")
                        root.illness_name.setTextColor(if(!item.checked) Color.parseColor("#FFDA6F")else Color.parseColor("#FFFFFF"))
                    }
                    "216-4" -> {
                        root.illness_icon.setText("IV")
                        root.illness_name.setTextColor(if(!item.checked) Color.parseColor("#30C890")else Color.parseColor("#FFFFFF"))
                    }
                    "216-5" -> {
                        root.illness_icon.setText("IV")
                        root.illness_name.setTextColor(if(!item.checked) Color.parseColor("#30C890")else Color.parseColor("#FFFFFF"))
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

            return oldItem.checked == newItem.checked
        }
    }


}