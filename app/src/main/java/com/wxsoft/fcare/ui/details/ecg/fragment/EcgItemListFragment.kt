package com.wxsoft.fcare.ui.details.ecg.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wxsoft.fcare.core.data.entity.Dictionary
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.utils.activityViewModelProvider
import com.wxsoft.fcare.databinding.FragmentEcgDiagnosisBinding
import com.wxsoft.fcare.databinding.ItemEcgDiagnoseTextBinding
import com.wxsoft.fcare.ui.details.ecg.EcgViewModel
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.layout_search_bar.*
import javax.inject.Inject

class EcgItemListFragment : DaggerFragment() {

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
        adapter=Adapter(this@EcgItemListFragment,::select)
       return FragmentEcgDiagnosisBinding.inflate(inflater,container, false).apply {
           lifecycleOwner = this@EcgItemListFragment
           viewModel=this@EcgItemListFragment.viewModel
           list.adapter=adapter
           this@EcgItemListFragment.viewModel.diagnoses.observe(this@EcgItemListFragment, Observer {
               (list.adapter as? Adapter)?.submitList(it)
           })


        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        back.setOnClickListener {
            parentFragment?.childFragmentManager?.let {
                if(it.popBackStackImmediate())
                    it.popBackStack()
            }
        }
    }
    private fun select(dictionary: Dictionary){
        with(viewModel.selectedEcgDiagnosis){
            if(!contains(dictionary)){
                viewModel.selectedDiagnoseResult.value=dictionary
                parentFragment?.let {
                    if(it.childFragmentManager.popBackStackImmediate()){
                        it.childFragmentManager.popBackStack()
                    }
                }
            }else{
                Toast.makeText(activity,"该项目已添加",Toast.LENGTH_SHORT).show()
            }
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
