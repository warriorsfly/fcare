package com.wxsoft.fcare.ui.emr

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wxsoft.fcare.core.data.entity.EmrImage
import com.wxsoft.fcare.core.data.entity.EmrRecord
import com.wxsoft.fcare.databinding.ItemOperationListBinding
import com.wxsoft.fcare.ui.PhotoEventAction
import com.wxsoft.fcare.ui.common.PictureAdapter


class RecordEmrImageAdapter constructor(private val owner:LifecycleOwner,
                                        private val mcontext: Context,
                                        private var action: PhotoEventAction?=null) :
    ListAdapter<EmrRecord, RecordEmrImageAdapter.ItemViewHolder>(DiffCallback) {


    private val adapters= mutableListOf<PictureAdapter>()
    private var selectedIndex=0

    val theOne
        get() = adapters[selectedIndex]

    val theRecord
        get() = getItem(selectedIndex)

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        holder.binding.apply {
            getItem(position).let {
                head.text=it.typeName
                (list.adapter as? PictureAdapter)?.apply {
                    indexing=position
                    remotes=it.items.map { it.httpUrl }
                    if(selectedIndex==position)locals= emptyList()
                }
            }
            executePendingBindings()
        }
    }

    private fun indexing(index:Int){
        selectedIndex=index
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val binding = ItemOperationListBinding.inflate(LayoutInflater.from(parent.context), parent, false).apply {
            lifecycleOwner=owner
            val acuter=PictureAdapter(owner,10,mcontext,action,::indexing)
            list.adapter=acuter
            adapters.add(acuter)
//            list.setRecycledViewPool(pool)
        }
        return ItemViewHolder(binding)
    }

    class ItemViewHolder(val binding: ItemOperationListBinding) : RecyclerView.ViewHolder(binding.root)


    object DiffCallback : DiffUtil.ItemCallback<EmrRecord>() {
        override fun areItemsTheSame(oldItem: EmrRecord, newItem: EmrRecord): Boolean {

            return oldItem.typeId == newItem.typeId
        }

        override fun areContentsTheSame(oldItem: EmrRecord, newItem: EmrRecord): Boolean {

            return oldItem.typeName == newItem.typeName  && oldItem.items == newItem.items
        }
    }
}