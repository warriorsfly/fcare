package com.wxsoft.fcare.ui.details.thrombolysis

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wxsoft.fcare.core.data.entity.Dictionary
import com.wxsoft.fcare.databinding.ItemSelecterOneBinding

class GoAdapter constructor(private val owner: LifecycleOwner) ://, click:(Dictionary)->Unit
    ListAdapter<Dictionary, GoAdapter.ItemViewHolder>(DiffCallback){

    private var pos:Int =-1

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        holder.binding.apply {
            val yy = getItem(position)
            item = yy
            selected = pos ==position
            executePendingBindings()

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val binding = ItemSelecterOneBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            .apply {
                lifecycleOwner = owner
            }
        return ItemViewHolder(binding).apply {
            binding.root.setOnClickListener {
                if(pos == adapterPosition){
                    pos = -1
                }else{
                    pos = adapterPosition
                }
                notifyDataSetChanged()
            }
        }
    }

    fun current()=if(pos<0)null else getItem(pos)

    fun setCurrent(code:String){
        pos = currentList.indexOfFirst { it.id ==code }
        notifyDataSetChanged()
    }

    class ItemViewHolder(binding: ItemSelecterOneBinding) : RecyclerView.ViewHolder(binding.root) {
        var binding: ItemSelecterOneBinding
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