package com.wxsoft.fcare.ui.details.assistant

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.utils.activityViewModelProvider
import com.wxsoft.fcare.databinding.FragmentLisBinding
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class LisFragment(val position:Int) : DaggerFragment(){

    private lateinit var viewModel: AssistantExaminationViewModel

    @Inject
    lateinit var factory: ViewModelFactory

    lateinit var binding: FragmentLisBinding
    lateinit var adapter: LisAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel=activityViewModelProvider(factory)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding=FragmentLisBinding.inflate(inflater,container, false).apply {
            lifecycleOwner = this@LisFragment
            viewModel=this@LisFragment.viewModel
        }
        val datas = viewModel.lisRecords.value?.get(position)
        adapter = LisAdapter(this,viewModel)
        binding.list.adapter = adapter
        if (datas != null){
            binding.item = datas
            adapter.submitList(datas!!.lisRecordDetails)
        }

        return binding.root
    }

}