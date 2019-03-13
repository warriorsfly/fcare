package com.wxsoft.fcare.ui.details.dominating.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.wxsoft.fcare.core.data.entity.Dictionary
import com.wxsoft.fcare.core.utils.activityViewModelProvider
import com.wxsoft.fcare.databinding.FragmentRatingsSheetBinding
import com.wxsoft.fcare.databinding.ItemDictSheetBinding
import com.wxsoft.fcare.ui.details.dominating.DoMinaViewModel
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class TaskSheetFragment constructor(private val itemClick: (Dictionary) -> Unit) : BottomSheetDialogFragment() , HasSupportFragmentInjector {

    companion object {
        const val TAG="TaskSheetFragment"
    }
    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private lateinit var viewModel: DoMinaViewModel
    private lateinit var adapter: ItemAdapter

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return fragmentInjector
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = activityViewModelProvider(factory)

        adapter=ItemAdapter(this@TaskSheetFragment,itemClick)
        viewModel.dicts.observe(this, Observer {
            adapter.submitList(it)
        })
        return FragmentRatingsSheetBinding.inflate(inflater,container,false).apply {
            list.adapter=this@TaskSheetFragment.adapter
            canncel.setOnClickListener { this@TaskSheetFragment.dismiss() }
            lifecycleOwner=this@TaskSheetFragment
        }.root
    }



    private class ItemAdapter constructor(private val owner: LifecycleOwner,private  val itemClick:(Dictionary)->Unit) :
        ListAdapter<Dictionary,ItemAdapter.ItemViewHolder>(DiffCallback) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
            val inflater=LayoutInflater.from(parent.context)
            return ItemViewHolder(ItemDictSheetBinding.inflate(inflater,parent,false) , itemClick)
        }

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
           holder.binding.apply {
               item = getItem(position)
               lifecycleOwner=owner
               executePendingBindings()
           }

        }

        object DiffCallback : DiffUtil.ItemCallback<Dictionary>() {
            override fun areItemsTheSame(oldItem: Dictionary, newItem: Dictionary): Boolean {
                return oldItem.id==newItem.id
            }

            override fun areContentsTheSame(oldItem: Dictionary, newItem: Dictionary): Boolean {
                return oldItem.itemName==newItem.itemName
            }
        }

        class ItemViewHolder(bind: ItemDictSheetBinding,itemClick:(Dictionary)->Unit) : RecyclerView.ViewHolder(bind.root) {

            var binding: ItemDictSheetBinding
                private set

            init {
                this.binding = bind.apply {
                    root.setOnClickListener {
                        item?.let{itemClick(it)}
                    }
                }


            }

        }
    }
}
