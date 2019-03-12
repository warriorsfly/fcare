package com.wxsoft.fcare.ui.details.notification

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wxsoft.fcare.core.data.entity.User
import com.wxsoft.fcare.databinding.ItemNotiCheckedUsersBinding

class NotificationCheckedAdapter constructor(private val owner: LifecycleOwner, val viewModel: NotificationViewModel) :
    ListAdapter<User,NotificationCheckedAdapter.ItemViewHolder>(DiffCallback){

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        holder.binding.apply {
            item = getItem(position)
            listener = viewModel
            executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val binding: ItemNotiCheckedUsersBinding =
            ItemNotiCheckedUsersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                .apply {
                    lifecycleOwner=owner
                }
        return ItemViewHolder(binding)
    }

    class ItemViewHolder(binding: ItemNotiCheckedUsersBinding) : RecyclerView.ViewHolder(binding.root) {
        var binding: ItemNotiCheckedUsersBinding
            private set
        init {
            this.binding = binding
        }
    }


    object DiffCallback : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {

            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {

            return oldItem == newItem
        }
    }
}