package com.wxsoft.fcare.ui.details.dominating.fragment.emr

import android.arch.lifecycle.LifecycleOwner
import android.databinding.ViewDataBinding
import android.sax.EndElementListener
import android.support.v7.recyclerview.extensions.AsyncListDiffer
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.data.entity.EmrItem
import com.wxsoft.fcare.core.data.entity.Patient
import com.wxsoft.fcare.databinding.ItemEmrNoneBinding
import com.wxsoft.fcare.databinding.ItemEmrProfileBinding
import com.wxsoft.fcare.ui.EventActions

class EmrAdapter constructor(private val lifecycleOwner: LifecycleOwner) :
    RecyclerView.Adapter<ItemViewHolder>() {

    private var action:EventActions?=null

    fun setActionListener(actions: EventActions){
        this.action=actions
    }
    private val differ = AsyncListDiffer<EmrItem>(this, DiffCallback)

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    var items: List<EmrItem> = emptyList()
        set(value) {
            field = value
            differ.submitList(value)
        }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        when (holder) {
            is ItemViewHolder.NoneViewHolder -> holder.binding.apply {
                setLifecycleOwner(lifecycleOwner)
                item=differ.currentList[position]
                visiable=position<differ.currentList.size-1
                action?.let {
                    root.setOnClickListener {
                        action?.onOpen(differ.currentList[position].code!!) }
                }
                executePendingBindings()
            }
            is ItemViewHolder.ProfileViewHolder -> holder.binding.apply {
                item=differ.currentList[position]
                action?.let {
                    root.setOnClickListener {
                        action?.onOpen(differ.currentList[position].code!!) }
                }

                visiable= position<differ.currentList.size-1
                patient=differ.currentList[position].result  as Patient
                setLifecycleOwner(lifecycleOwner)
                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            R.layout.item_emr_none -> ItemViewHolder.NoneViewHolder(
                ItemEmrNoneBinding.inflate(inflater, parent, false)
            )
            R.layout.item_emr_profile -> ItemViewHolder.ProfileViewHolder(
                ItemEmrProfileBinding.inflate(inflater, parent, false)
            )
            else -> throw IllegalStateException("Unknown viewType $viewType")
        }
    }


    override fun getItemViewType(position: Int): Int {
        return when (differ.currentList[position].result) {
            null->R.layout.item_emr_none
            is Patient -> R.layout.item_emr_profile
            else -> throw IllegalStateException("Unknown view type at position $position")
        }
    }


    object DiffCallback : DiffUtil.ItemCallback<EmrItem>() {
        override fun areItemsTheSame(oldItem: EmrItem, newItem: EmrItem): Boolean {
            val result1=oldItem.result
            val result2=newItem.result
            return when {
                result1 is Any && result2 is Any->
                    oldItem.code==newItem.code && result1==result2

                result1 is Patient && result2 is Patient ->
                    result1.id == result2.id &&  oldItem.code==newItem.code
                else -> false
            }
        }

        override fun areContentsTheSame(oldItem: EmrItem, newItem: EmrItem): Boolean {
            val result1=oldItem.result
            val result2=newItem.result
            return when {
                result1 is Any && result2 is Any->
                     oldItem.code==newItem.code && result1 == result2

                result1 is Patient && result2 is Patient ->
                    result1.id == result2.id &&  oldItem.code==newItem.code
                else -> false
            }
        }

    }


}

object ForNewItem


sealed class ItemViewHolder(binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

    class NoneViewHolder(
        val binding: ItemEmrNoneBinding
    ) : ItemViewHolder(binding)

    class ProfileViewHolder(
        val binding: ItemEmrProfileBinding
    ) : ItemViewHolder(binding)
}