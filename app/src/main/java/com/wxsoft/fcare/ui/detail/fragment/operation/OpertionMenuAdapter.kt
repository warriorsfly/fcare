package com.wxsoft.fcare.ui.detail.fragment.timeline


import android.arch.lifecycle.LifecycleOwner
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.wxsoft.fcare.data.entity.OperationMenu
import com.wxsoft.fcare.databinding.FragmentOperationMenuItemBinding
import com.wxsoft.fcare.ui.detail.PatientDetailViewModel


class OpertionMenuAdapter(private val lifecycleOwner:LifecycleOwner,private val viewModel: PatientDetailViewModel): RecyclerView.Adapter<OpertionMenuAdapter.ItemViewHolder>() {

    var menus: List<OperationMenu> = emptyList()
    override fun getItemCount(): Int {
        return menus.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        holder.binding.apply {

            menu=menus[position]
            viewModel=this@OpertionMenuAdapter.viewModel
            setLifecycleOwner(lifecycleOwner)
            executePendingBindings()
        }



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val binding = FragmentOperationMenuItemBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return ItemViewHolder(binding)
    }

    class ItemViewHolder(binding:FragmentOperationMenuItemBinding ) : RecyclerView.ViewHolder(binding.root) {
        var binding: FragmentOperationMenuItemBinding
            private set

        init {
            this.binding = binding
        }
    }

}