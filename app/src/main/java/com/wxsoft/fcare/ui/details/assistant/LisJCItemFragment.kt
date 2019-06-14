package com.wxsoft.fcare.ui.details.assistant


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.utils.activityViewModelProvider
import com.wxsoft.fcare.databinding.FragmentLisJcitemBinding
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class LisJCItemFragment(val position:Int) : DaggerFragment(){

    private lateinit var viewModel: AssistantExaminationViewModel

    @Inject
    lateinit var factory: ViewModelFactory

    lateinit var binding: FragmentLisJcitemBinding
    lateinit var adapter: LisRecordItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel=activityViewModelProvider(factory)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= FragmentLisJcitemBinding.inflate(inflater,container, false).apply {
            lifecycleOwner = this@LisJCItemFragment
            viewModel=this@LisJCItemFragment.viewModel
        }
        val datas = viewModel.lisJCRecords.value?.get(position)
        binding.item = datas
        return binding.root
    }

}
