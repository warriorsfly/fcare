package com.wxsoft.fcare.ui.sign

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wxsoft.fcare.core.data.entity.SignInUser
import com.wxsoft.fcare.databinding.ItemSignInUserBinding


class SignInUserAdapter constructor(
    private val owner: LifecycleOwner,
    private val itemClickListener: (SignInUser) -> Unit,
    private val call: (String) -> Unit
) :
    ListAdapter<SignInUser, SignInUserAdapter.ItemViewHolder>(DiffCallback) {

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        holder.binding.apply {
            getItem(position).let {
                item = it
            }
            executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val binding =
            ItemSignInUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                .apply {
                    lifecycleOwner = owner
                }
        return ItemViewHolder(binding, itemClickListener,call)
    }

    class ItemViewHolder(
        binding: ItemSignInUserBinding, itemClickListener: (SignInUser) -> Unit,
        call: (String) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {
        var binding: ItemSignInUserBinding
            private set

        init {
            this.binding = binding.apply {
                d4.setOnClickListener {
                    item?.let(itemClickListener)
                }
                callIcon.setOnClickListener {
                    item?.user?.tel?.let(call)
                }
            }
        }
    }


    object DiffCallback : DiffUtil.ItemCallback<SignInUser>() {
        override fun areItemsTheSame(oldItem: SignInUser, newItem: SignInUser): Boolean {

            return oldItem.shiftsCode == newItem.shiftsCode
        }

        override fun areContentsTheSame(oldItem: SignInUser, newItem: SignInUser): Boolean {

            return oldItem.userId == newItem.userId && oldItem.user == newItem.user
        }
    }
}