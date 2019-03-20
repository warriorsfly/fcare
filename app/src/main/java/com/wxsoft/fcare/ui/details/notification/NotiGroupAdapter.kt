package com.wxsoft.fcare.ui.details.notification

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.wxsoft.fcare.core.data.entity.NotiUserItem
import com.wxsoft.fcare.databinding.ItemNotiGroupBinding

class NotiGroupAdapter constructor(private val lifecycleOwner: LifecycleOwner, val viewModel: NotificationViewModel) :
    RecyclerView.Adapter<NotiGroupAdapter.ItemViewHolder>(){

    private val differ = AsyncListDiffer<NotiUserItem>(this, DiffCallback)

    var items: List<NotiUserItem> = emptyList()
        set(value) {
            field = value
            var arr = value.filter { it.users.size>0 }
            differ.submitList(arr)
        }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        holder.binding.apply {
            item = differ.currentList[position]
            listener = this@NotiGroupAdapter.viewModel
            lifecycleOwner = this@NotiGroupAdapter.lifecycleOwner
            if (groupList.adapter == null){
                val adapter = NotiGroupItemAdapter(this@NotiGroupAdapter.lifecycleOwner,this@NotiGroupAdapter.viewModel)
                adapter.items = differ.currentList[position].users
                groupList.adapter = adapter
            }
            executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val binding: ItemNotiGroupBinding =
            ItemNotiGroupBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    class ItemViewHolder(binding: ItemNotiGroupBinding) : androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root) {
        var binding: ItemNotiGroupBinding
            private set
        init {
            this.binding = binding
        }
    }


    object DiffCallback : DiffUtil.ItemCallback<NotiUserItem>() {
        override fun areItemsTheSame(oldItem: NotiUserItem, newItem: NotiUserItem): Boolean {

            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: NotiUserItem, newItem: NotiUserItem): Boolean {

            return oldItem.id == newItem.id
        }
    }

}