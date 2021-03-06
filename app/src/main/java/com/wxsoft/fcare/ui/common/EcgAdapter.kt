package com.wxsoft.fcare.ui.common

import android.annotation.SuppressLint
import android.content.Context
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
import com.wxsoft.fcare.ui.PhotoEventAction
import com.wxsoft.fcare.ui.photo.PhotoActivity
import android.util.Pair as ViewPair

class EcgAdapter constructor(private val owner: LifecycleOwner, private val max:Int=0, private val mcontext: Context ,private var action: PhotoEventAction?=null) :
    RecyclerView.Adapter<EcgAdapter.ItemViewHolder>() {

    private val differ = AsyncListDiffer<Any>(this, DiffCallback)

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    var locals: List<Pair<LocalMedia,Uri>> = emptyList()
        set(value) {
            field = value
            differ.submitList(buildMergedList(remotes,value))
        }

    var remotes:List<String> = emptyList()
        set(value) {
            field = value
            pairs=arrayOfNulls(remotes.size)
            differ.submitList(buildMergedList(value,locals))
        }


    lateinit var pairs:Array<android.util.Pair<View, String>?>

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
                root.setOnClickListener{action?.localSelected()}
                uri=presenter.second
                executePendingBindings()
            }

            is ItemViewHolder.ImageRemoteViewHolder -> holder.binding.apply {
                val presenter =differ.currentList[position] as String
                url=presenter
                executePendingBindings()
            }
            is ItemViewHolder.PlaceViewHolder -> holder.binding.apply {
                image.setOnClickListener{action?.localSelected()}
                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            R.layout.item_image -> ItemViewHolder.ImageViewHolder(
                ItemImageBinding.inflate(inflater, parent, false)
                    .apply {
                        lifecycleOwner = owner
                    }
            )
            R.layout.item_image_remote -> ItemViewHolder.ImageRemoteViewHolder(
                ItemImageRemoteBinding.inflate(inflater, parent, false)
                    .apply {


                        image.setOnLongClickListener {
                            url?.let {
                                action?.deleteRemote(it)
                            }

                            true
                        }
                        lifecycleOwner = owner
                    }
            ).apply {
                binding.image.setOnClickListener{
                    PhotoActivity.startActivity(mcontext, arrayOf(ViewPair(it,"img$adapterPosition")), adapterPosition, remotes.toTypedArray())
                }

            }
            R.layout.item_new_image -> ItemViewHolder.PlaceViewHolder(
                ItemNewImageBinding.inflate(inflater, parent, false)
                    .apply {
                        lifecycleOwner = owner
                    }
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
//            return when {
//                oldItem is Pair<*, *> && newItem is Pair<*, *> -> newItem.first == oldItem.first
//                oldItem is String && newItem is String -> newItem == oldItem
//                else -> false
//            }
            return false
        }

    }

    object ForNewItem


    sealed class ItemViewHolder(binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

        class ImageViewHolder(
            val binding: ItemImageBinding
        ) : ItemViewHolder(binding)

        class ImageRemoteViewHolder(
            val binding: ItemImageRemoteBinding
        ) :ItemViewHolder(binding)

        class PlaceViewHolder(
            val binding: ItemNewImageBinding
        ) : ItemViewHolder(binding)
    }
}

