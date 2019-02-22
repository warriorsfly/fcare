package com.wxsoft.fcare.ui.details.informedconsent.informeddetails

import androidx.lifecycle.LifecycleOwner
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.data.entity.Attachment
import com.wxsoft.fcare.databinding.ItemImageRemoteBinding
import com.wxsoft.fcare.databinding.ItemVoiceRemoteBinding
import com.wxsoft.fcare.ui.PhotoEventAction
import com.wxsoft.fcare.ui.PlayVoiceEventAction

class InformedDetailsAdapter constructor(private val lifecycleOwner: LifecycleOwner) :
    RecyclerView.Adapter<ItemViewHolder>() {


    private var action: PhotoEventAction?=null
    private var voiceAction: PlayVoiceEventAction?=null

    fun setActionListener(actions: PhotoEventAction){
        this.action=actions
    }
    fun setVoiceActionListener(actions: PlayVoiceEventAction){
        this.voiceAction=actions
    }


    private val differ = AsyncListDiffer<Attachment>(this, DiffCallback)

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    var remotes:List<Attachment> = emptyList()
        set(value) {
            field = value
            differ.submitList(value)
        }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        when (holder) {
            is ItemViewHolder.VoiceViewHolder -> holder.binding.apply {
                val presenter =differ.currentList[position].httpUrl
//                presenter.first.num
                root.setOnClickListener{voiceAction?.play(image,presenter)}
//                uri=presenter.second
                lifecycleOwner =  this@InformedDetailsAdapter.lifecycleOwner
                executePendingBindings()
            }

            is ItemViewHolder.ImageRemoteViewHolder -> holder.binding.apply {
                val presenter =differ.currentList[position].httpUrl
                image.setOnClickListener{action?.enlargeRemote(root,presenter)}
                url=presenter
                lifecycleOwner = this@InformedDetailsAdapter.lifecycleOwner
                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            R.layout.item_voice_remote -> ItemViewHolder.VoiceViewHolder(
                ItemVoiceRemoteBinding.inflate(inflater, parent, false)
            )
            R.layout.item_image_remote -> ItemViewHolder.ImageRemoteViewHolder(
                ItemImageRemoteBinding.inflate(inflater, parent, false)
            )
            else -> throw IllegalStateException("Unknown viewType $viewType")
        }
    }


    override fun getItemViewType(position: Int): Int {
        return when (differ.currentList[position].fileType) {
            ".mp3" -> R.layout.item_voice_remote
            else -> R.layout.item_image_remote
        }
    }


    object DiffCallback : DiffUtil.ItemCallback<Attachment>() {
        override fun areItemsTheSame(oldItem: Attachment, newItem: Attachment): Boolean {

            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Attachment, newItem: Attachment): Boolean {

            return oldItem.id == newItem.id
        }

    }
}



sealed class ItemViewHolder(binding: ViewDataBinding) : androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root) {

    class VoiceViewHolder(
        val binding: ItemVoiceRemoteBinding
    ) : ItemViewHolder(binding)

    class ImageRemoteViewHolder(
        val binding: ItemImageRemoteBinding
    ) : ItemViewHolder(binding)
}