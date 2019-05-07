package com.wxsoft.fcare.ui.details.comingby.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wxsoft.fcare.core.data.entity.User
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.utils.activityViewModelProvider
import com.wxsoft.fcare.databinding.FragmentComingByListBinding
import com.wxsoft.fcare.databinding.ItemDoctorTextBinding
import com.wxsoft.fcare.ui.details.comingby.ComingByViewModel
import javax.inject.Inject
import dagger.android.support.DaggerFragment as DaggerFragment1

class ComingByDoctorListFragment : DaggerFragment1() {

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
        adapter = Adapter(this@ComingByDoctorListFragment,::select)
        adapter.singleSelect=type!=3
        when (type) {
            1->viewModel.emergencyDoctors.value?.let(adapter::submitList)
            2->viewModel.emergencyNurses.value?.let(adapter::submitList)
            3->viewModel.consultantDoctors.value?.let(adapter::submitList)
        }



        return FragmentComingByListBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = this@ComingByDoctorListFragment
            list.adapter = adapter
        }.root
    }

    private fun select(user: User) {
        viewModel.comingBy.value?.apply {

            when (type) {
                1 -> {
                    emergencyDoctor.id=user.id
                    emergencyDoctor.trueName=user.trueName
                    activity?.supportFragmentManager?.let {
                        if (it.popBackStackImmediate())
                            it.popBackStack()
                    }
                }
                2->{
                    emergencyNurse.id=user.id
                    emergencyNurse.trueName=user.trueName
                    activity?.supportFragmentManager?.let {
                        if (it.popBackStackImmediate())
                            it.popBackStack()
                    }
                }
                3 -> {
                    consultantDoctors=viewModel.consultantDoctors.value?.
                        filter { it.checked }?.joinToString (separator = ","){ it.trueName }?:""
                }
            }

        }
    }

    class Adapter constructor(private val owner: LifecycleOwner,
                              private val itemSelected:(User)->Unit,
                              var singleSelect:Boolean=true) :
        ListAdapter<User,Adapter.ItemViewHolder>(DiffCallback) {

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

            holder.binding.apply {
                item=getItem(position)

                executePendingBindings()

            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

            val binding =
                ItemDoctorTextBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                    .apply {
                        lifecycleOwner = this@Adapter.owner
                        root.setOnClickListener {
                            if (singleSelect) {
                                item?.let(itemSelected)
                            }else{
                                item?.let {
                                    it.checked=!it.checked
                                    it.let(itemSelected)

                                }
                            }
                        }
                    }
            return ItemViewHolder(binding)
        }

        class ItemViewHolder(val binding: ItemDoctorTextBinding) : RecyclerView.ViewHolder(binding.root)


        object DiffCallback : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {

                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {

                return oldItem.trueName == newItem.trueName && oldItem.checked==newItem.checked
            }
        }

    }
}
