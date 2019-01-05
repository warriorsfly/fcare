package com.wxsoft.fcare.ui.common

import android.arch.lifecycle.LifecycleOwner
import android.databinding.ViewDataBinding
import android.graphics.Bitmap
import android.support.v7.recyclerview.extensions.AsyncListDiffer
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.wxsoft.fcare.R
import com.wxsoft.fcare.databinding.ItemAttachmentBinding
import com.wxsoft.fcare.databinding.ItemNewAttachmentBinding
import com.wxsoft.fcare.generated.callback.OnClickListener

class AttachmentAdapter constructor(private val lifecycleOwner: LifecycleOwner,private val onClickListener: OnClickListener) :
    RecyclerView.Adapter<ItemViewHolder>() {


    private val differ = AsyncListDiffer<Any>(this, DiffCallback)

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    var attachs: List<Bitmap> = emptyList()
        set(value) {
            field = value
            differ.submitList(buildMergedList(value))
        }

    private fun buildMergedList(
        images: List<Bitmap> =attachs): List<Any> {
        val merged = mutableListOf<Any>()
        if (images.isNotEmpty() && images.size==4) {

            merged.addAll(images)
        }else{
            if(images.isNotEmpty()){
                merged.addAll(images)

            }
            merged+=ForNewItem
        }
        return merged
    }



    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        when (holder) {
            is ItemViewHolder.ImageViewHolder -> holder.binding.apply {
                root.setOnClickListener(onClickListener)
                image.setImageBitmap(attachs[position])
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
            R.layout.item_attachment -> ItemViewHolder.ImageViewHolder(
                ItemAttachmentBinding.inflate(inflater, parent, false)
            )
            R.layout.item_new_attachment -> ItemViewHolder.PlaceViewHolder(
                ItemNewAttachmentBinding.inflate(inflater, parent, false)
            )
            else -> throw IllegalStateException("Unknown viewType $viewType")
        }
    }


    override fun getItemViewType(position: Int): Int {
        return when (differ.currentList[position]) {
            is ForNewItem -> R.layout.item_new_attachment
            is Bitmap -> R.layout.item_attachment
            else -> throw IllegalStateException("Unknown view type at position $position")
        }
    }


    object DiffCallback : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
            return when {
                oldItem === ForNewItem && newItem === ForNewItem -> true
                oldItem is Bitmap && newItem is Bitmap -> oldItem == newItem
                else -> false
            }
        }

        override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
            return when {
                oldItem is Bitmap && newItem is Bitmap -> newItem == oldItem
                else -> false
            }
        }

    }


}

object ForNewItem


sealed class ItemViewHolder(binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

    class ImageViewHolder(
        val binding: ItemAttachmentBinding
    ) : ItemViewHolder(binding)

    class PlaceViewHolder(
        val binding: ItemNewAttachmentBinding
    ) : ItemViewHolder(binding)
}