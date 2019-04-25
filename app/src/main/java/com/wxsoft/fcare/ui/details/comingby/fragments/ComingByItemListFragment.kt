package com.wxsoft.fcare.ui.details.comingby.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wxsoft.fcare.core.data.entity.Dictionary
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.utils.activityViewModelProvider
import com.wxsoft.fcare.databinding.FragmentComingByListBinding
import com.wxsoft.fcare.databinding.ItemEcgDiagnoseTextBinding
import com.wxsoft.fcare.ui.details.comingby.ComingByViewModel
import javax.inject.Inject
import dagger.android.support.DaggerFragment as DaggerFragment1

class ComingByItemListFragment : DaggerFragment1() {

    /**
     * 1来院方式2发车单位3绕行急诊科后绕行科室
     */
    var type=1

    private lateinit var viewModel: ComingByViewModel

    @Inject
    lateinit var factory: ViewModelFactory
    lateinit var adapter: Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel=activityViewModelProvider(factory)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = activityViewModelProvider(factory)
        adapter = Adapter(this@ComingByItemListFragment, ::select)


        when (type) {
            1->viewModel.comingType.value?.let(adapter::submitList)
            2->viewModel.comingFrom.value?.let(adapter::submitList)
            3->viewModel.passingKs.value?.let(adapter::submitList)
        }

        return FragmentComingByListBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = this@ComingByItemListFragment
            list.adapter = adapter
        }.root
    }

    private fun select(dictionary: Dictionary) {
        viewModel.comingBy.value?.apply {

            when (type) {
                1 -> {

                    comingWayCode = dictionary.id
                    comingWayName = dictionary.itemName
                    if (dictionary.id == "3-3") {
                        viewModel.passing.value?.let {
                            it.passingEmergency = false
                        }
                    }
                }
                2->{
                    dispatchCode = dictionary.id
                    dispatchName = dictionary.itemName
                }
            }

        }
        viewModel.passing.value?.apply {

            when (type) {
                3 -> {
                    passingEmergencyCode=dictionary.id
                    passingEmergencyName=dictionary.itemName
                }
            }

        }

        activity?.supportFragmentManager?.let {
            if (it.popBackStackImmediate())
                it.popBackStack()
        }

    }


    class Adapter constructor(private val owner: LifecycleOwner,private val itemSelected:(Dictionary)->Unit) :
        ListAdapter<Dictionary,Adapter.ItemViewHolder>(DiffCallback) {

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

            holder.binding.apply {
                item=getItem(position)

                executePendingBindings()

            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

            val binding =
                ItemEcgDiagnoseTextBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                    .apply {
                        lifecycleOwner = this@Adapter.owner
                        root.setOnClickListener {
                            item?.let(itemSelected)
                        }
                    }
            return ItemViewHolder(binding)
        }

        class ItemViewHolder(val binding: ItemEcgDiagnoseTextBinding) : RecyclerView.ViewHolder(binding.root)


        object DiffCallback : DiffUtil.ItemCallback<Dictionary>() {
            override fun areItemsTheSame(oldItem: Dictionary, newItem: Dictionary): Boolean {

                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Dictionary, newItem: Dictionary): Boolean {

                return oldItem.itemName == newItem.itemName && oldItem.checked==newItem.checked
            }
        }

    }
}
