package com.wxsoft.fcare.ui.common

import android.annotation.SuppressLint
import android.arch.lifecycle.LifecycleOwner
import android.databinding.ViewDataBinding
import android.net.Uri
import android.support.v7.recyclerview.extensions.AsyncListDiffer
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.wxsoft.fcare.R
import com.wxsoft.fcare.databinding.ItemImageBinding
import com.wxsoft.fcare.databinding.ItemNewImageBinding
import com.wxsoft.fcare.generated.callback.OnClickListener

class AttachmentAdapter constructor(private val lifecycleOwner: LifecycleOwner,private val onClickListener: OnClickListener) :
    RecyclerView.Adapter<ItemViewHolder>() {


    private val differ = AsyncListDiffer<Any>(this, DiffCallback)

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    var locals: List<Uri> = emptyList()
        set(value) {
            field = value
            differ.submitList(buildMergedList(remotes,value))
        }

    var remotes:List<String> = emptyList()
        set(value) {
            field = value
            differ.submitList(buildMergedList(value,locals))
        }

    private fun buildMergedList(
        remote:List<String> =remotes,
        local: List<Uri> =locals): List<Any> {
        val merged = mutableListOf<Any>()
        if(remote.isNotEmpty()){
            merged.addAll(remote)
        }
        if (local.isNotEmpty() && local.size+remote.size==4) {

            merged.addAll(local)
        }else{
            if(local.isNotEmpty()){
                merged.addAll(local)

            }
            merged+=ForNewItem
        }
        return merged
    }



    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        when (holder) {
            is ItemViewHolder.ImageViewHolder -> holder.binding.apply {
                val presenter =differ.currentList[position] as Uri
                root.setOnClickListener(onClickListener)
                uri=presenter
                setLifecycleOwner(lifecycleOwner)
                executePendingBindings()
            }
            is ItemViewHolder.PlaceViewHolder -> holder.binding.apply {
                root.tag=ForNewItem
                root.setOnClickListener(onClickListener)
                setLifecycleOwner(lifecycleOwner)
                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            R.layout.item_image -> ItemViewHolder.ImageViewHolder(
                ItemImageBinding.inflate(inflater, parent, false)
            )
            R.layout.item_new_image -> ItemViewHolder.PlaceViewHolder(
                ItemNewImageBinding.inflate(inflater, parent, false)
            )
            else -> throw IllegalStateException("Unknown viewType $viewType")
        }
    }


    override fun getItemViewType(position: Int): Int {
        return when (differ.currentList[position]) {
            is ForNewItem -> R.layout.item_new_image
            is Uri -> R.layout.item_image
            else -> throw IllegalStateException("Unknown view type at position $position")
        }
    }


    object DiffCallback : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
            return when {
                oldItem === ForNewItem && newItem === ForNewItem -> true
                oldItem is String && newItem is String -> newItem == oldItem
                oldItem is Uri && newItem is Uri -> oldItem == newItem
                else -> false
            }
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
            return when {
                oldItem is Uri && newItem is Uri -> newItem == oldItem
                oldItem is String && newItem is String -> newItem == oldItem
                else -> false
            }
        }

    }


}

object ForNewItem


sealed class ItemViewHolder(binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

    class ImageViewHolder(
        val binding: ItemImageBinding
    ) : ItemViewHolder(binding)

    class PlaceViewHolder(
        val binding: ItemNewImageBinding
    ) : ItemViewHolder(binding)
}