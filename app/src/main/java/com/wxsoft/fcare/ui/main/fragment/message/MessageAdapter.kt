package com.wxsoft.fcare.ui.main.fragment.message

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.data.entity.Message
import com.wxsoft.fcare.databinding.ItemUserMessageBinding
import kotlin.reflect.KFunction1


class MessageAdapter(private val owner: LifecycleOwner,
                     val action:(Message)->Unit) :
    PagedListAdapter<Message, MessageAdapter.ItemViewHolder>(DiffCallback){



    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        holder.binding.apply {
            val message=getItem(position)
            item = message
            icon.setImageResource(if(message?.messageType==2) R.drawable.ic_message_type2 else R.drawable.ic_message_type1)
            ignoreMessage.setOnClickListener {
                if (message!=null) action(message)
            }
            executePendingBindings()
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val binding = ItemUserMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            .apply {
                lifecycleOwner = owner
            }
        return ItemViewHolder(binding)
    }

    class ItemViewHolder(val binding: ItemUserMessageBinding) : RecyclerView.ViewHolder(binding.root)


    object DiffCallback : DiffUtil.ItemCallback<Message>() {
        override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {

            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {

            return oldItem.messageContent == newItem.messageContent
                    && oldItem.messageSubject == newItem.messageSubject
        }
    }


}