package com.wxsoft.fcare.ui.details.notification

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.wxsoft.fcare.core.data.entity.User
import com.wxsoft.fcare.databinding.ItemNotiGroupItemBinding

class NotiGroupItemAdapter constructor(private val lifecycleOwner: LifecycleOwner, val viewModel: NotificationViewModel) :
    RecyclerView.Adapter<NotiGroupItemAdapter.ItemViewHolder>(){

    private val differ = AsyncListDiffer<User>(this, DiffCallback)

    var items: List<User> = emptyList()
        set(value) {
            field = value
            differ.submitList(value)
        }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        holder.binding.apply {
            item = differ.currentList[position]
            listener = this@NotiGroupItemAdapter.viewModel
            lifecycleOwner = this@NotiGroupItemAdapter.lifecycleOwner
            executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val binding: ItemNotiGroupItemBinding =
            ItemNotiGroupItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    class ItemViewHolder(binding: ItemNotiGroupItemBinding) : androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root) {
        var binding: ItemNotiGroupItemBinding
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

            return oldItem.id == newItem.id
        }
    }

}