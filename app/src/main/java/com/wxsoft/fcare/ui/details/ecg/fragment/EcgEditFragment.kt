package com.wxsoft.fcare.ui.details.ecg.fragment

import android.animation.Animator
import android.os.Bundle
import android.view.*
import android.view.animation.Animation
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.data.entity.Dictionary
import com.wxsoft.fcare.core.data.entity.EcgDiagnose
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.utils.activityViewModelProvider
import com.wxsoft.fcare.core.utils.inTransaction
import com.wxsoft.fcare.databinding.FragmentEditEcgDiagnoseBinding
import com.wxsoft.fcare.databinding.ItemEcgDiagnoseCheckedBinding
import com.wxsoft.fcare.databinding.ItemEcgDiagnoseTextBinding
import com.wxsoft.fcare.ui.details.ecg.EcgViewModel
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_edit_ecg_diagnose.*
import javax.inject.Inject

class EcgEditFragment : DaggerFragment() {

    private val  fragment by lazy{
        EcgItemListFragment()
    }


    private lateinit var viewModel: EcgViewModel

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
        viewModel=activityViewModelProvider(factory)

        adapter=Adapter(this,::select)
        adapter.submitList(viewModel.selectedEcgDiagnosis)
        viewModel.selectedDiagnoseResult.observe(this, Observer {
            viewModel.selectedEcgDiagnosis.add(it)
            adapter.notifyDataSetChanged()
        })

        viewModel.saved.observe(this, Observer {

            if(isAdded) {
                activity?.supportFragmentManager?.let {
                    if (it.popBackStackImmediate())
                        it.popBackStack()
                }
            }
        })
       return FragmentEditEcgDiagnoseBinding.inflate(inflater,container, false).apply {
           lifecycleOwner = this@EcgEditFragment
           viewModel=this@EcgEditFragment.viewModel

           list.adapter=adapter
           submit.setOnClickListener {
               if(viewModel?.selectedEcgDiagnosis?.isNotEmpty() == true ){
                   viewModel?.diagnose()
               }
           }
           back.setOnClickListener {
               activity?.supportFragmentManager?.let {
                   if(it.popBackStackImmediate())
                       it.popBackStack()
               }
           }
           add.setOnClickListener {

               if(fragment.isAdded)return@setOnClickListener
               childFragmentManager.inTransaction {
                   setCustomAnimations(
                       R.animator.left_enter,
                       R.animator.left_exit,
                       R.animator.right_enter,
                       R.animator.right_exit
                   )
                   addToBackStack(null)
                   add(R.id.container, fragment)
               }
           }
        }.root
    }

    private fun select(dictionary: Dictionary){
        viewModel.selectedEcgDiagnosis.remove(dictionary)
        adapter.notifyDataSetChanged()
    }

    class Adapter constructor(private val owner: LifecycleOwner, private val itemSelected:(Dictionary)->Unit) :
        ListAdapter<Dictionary, Adapter.ItemViewHolder>(DiffCallback) {

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

            holder.binding.apply {
                item=getItem(position)
                executePendingBindings()
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

            val binding =
                ItemEcgDiagnoseCheckedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                    .apply {
                        deleteIc.setOnClickListener {
                            item?.let {
                                itemSelected(it)
                            }
                        }
                        lifecycleOwner =owner
                    }
            return ItemViewHolder(binding)
        }

        class ItemViewHolder(val binding: ItemEcgDiagnoseCheckedBinding) : RecyclerView.ViewHolder(binding.root)


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
