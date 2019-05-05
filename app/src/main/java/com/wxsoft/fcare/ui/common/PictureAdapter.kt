package com.wxsoft.fcare.ui.common

import android.annotation.SuppressLint
import androidx.lifecycle.LifecycleOwner
import androidx.databinding.ViewDataBinding
import android.net.Uri
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.luck.picture.lib.entity.LocalMedia
import com.wxsoft.fcare.R
import com.wxsoft.fcare.databinding.ItemImageBinding
import com.wxsoft.fcare.databinding.ItemImageRemoteBinding
import com.wxsoft.fcare.databinding.ItemNewImageBinding
import com.wxsoft.fcare.generated.callback.OnClickListener
import com.wxsoft.fcare.ui.PhotoEventAction

class PictureAdapter constructor(private val lifecycleOwner: LifecycleOwner,
                                 private val max:Int=0,
                                 private val action: PhotoEventAction?=null,
                                 private val indexAction:(Int)->Unit ={}) :
    RecyclerView.Adapter<PictureAdapter.ItemViewHolder>() {

//    var pairs= arrayOfNulls<android.util.Pair<View, String>>(list.size)

    var indexing:Int=0

    private val differ = AsyncListDiffer<Any>(this, DiffCallback)

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

//    var pairs: List<Pair<View,String>> = emptyList()
//        set(value) {
//            field = value
//            differ.submitList(buildMergedList(remotes,value))
//        }

    var locals: List<Pair<LocalMedia,Uri>> = emptyList()
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
        local: List<Pair<LocalMedia,Uri>> =locals): List<Any> {
        val merged = mutableListOf<Any>()
        if(remote.isNotEmpty()){
            merged.addAll(remote)
        }
        if(max==0){
            merged.addAll(local)
        }else {
            if (local.isNotEmpty() && local.size + remote.size == max) {

                merged.addAll(local)
            } else {
                if (local.isNotEmpty()) {
                    merged.addAll(local)
                }
                if(local.size + remote.size < max)
                    merged += ForNewItem
            }
        }
        return merged
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        when (holder) {
            is ItemViewHolder.ImageViewHolder -> holder.binding.apply {
                val presenter =differ.currentList[position] as Pair<LocalMedia, Uri>
//                presenter.first.num
                root.setOnClickListener{

                    action?.localSelected()
                }
                uri=presenter.second
                lifecycleOwner = this@PictureAdapter.lifecycleOwner

                executePendingBindings()
            }

            is ItemViewHolder.ImageRemoteViewHolder -> holder.binding.apply {
                val presenter =differ.currentList[position] as String
                image.setOnClickListener{action?.enlargeRemote(root,presenter)}
                image.setOnLongClickListener {
                    action?.deleteRemote(presenter)
                    true
                }
                url=presenter
                lifecycleOwner = this@PictureAdapter.lifecycleOwner
                executePendingBindings()
            }
            is ItemViewHolder.PlaceViewHolder -> holder.binding.apply {
                image.setOnClickListener{

                    indexAction(indexing)
//                    if(indexing!=null)
//                        indexing
                    action?.localSelected()
                }
                lifecycleOwner = this@PictureAdapter.lifecycleOwner
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
            R.layout.item_image_remote -> ItemViewHolder.ImageRemoteViewHolder(
                ItemImageRemoteBinding.inflate(inflater, parent, false)
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
            is Pair<*, *> -> R.layout.item_image
            is String -> R.layout.item_image_remote
            else -> R.layout.item_image
        }
    }


    object DiffCallback : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
            return when {
                oldItem === ForNewItem && newItem === ForNewItem -> true
                oldItem is String && newItem is String -> newItem == oldItem
                oldItem is Pair<*, *> && newItem is Pair<*, *> -> newItem.first == oldItem.first
                else -> false
            }
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
            return when {
                oldItem is Pair<*, *> && newItem is Pair<*, *> -> newItem.first == oldItem.first
                oldItem is String && newItem is String -> newItem == oldItem
                else -> false
            }
        }

    }

    object ForNewItem

    sealed class ItemViewHolder(binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

        class ImageViewHolder(
            val binding: ItemImageBinding
        ) : ItemViewHolder(binding)

        class ImageRemoteViewHolder(
            val binding: ItemImageRemoteBinding
        ) : ItemViewHolder(binding)

        class PlaceViewHolder(
            val binding: ItemNewImageBinding
        ) : ItemViewHolder(binding)
    }
}

