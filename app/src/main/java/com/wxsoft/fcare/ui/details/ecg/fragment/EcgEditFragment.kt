package com.wxsoft.fcare.ui.details.ecg.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.utils.activityViewModelProvider
import com.wxsoft.fcare.databinding.FragmentEditEcgDiagnoseBinding
import com.wxsoft.fcare.ui.details.ecg.EcgViewModel
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class EcgEditFragment : DaggerFragment() {

    private lateinit var viewModel: EcgViewModel

    @Inject
    lateinit var factory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel=activityViewModelProvider(factory)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel=activityViewModelProvider(factory)
       return FragmentEditEcgDiagnoseBinding.inflate(inflater,container, false).apply {
           lifecycleOwner = this@EcgEditFragment
           viewModel=this@EcgEditFragment.viewModel
        }.root
    }
}
