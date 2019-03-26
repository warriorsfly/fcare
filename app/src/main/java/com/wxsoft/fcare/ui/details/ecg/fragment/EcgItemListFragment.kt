package com.wxsoft.fcare.ui.details.ecg.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.utils.activityViewModelProvider
import com.wxsoft.fcare.databinding.FragmentEcgDiagnosisBinding
import com.wxsoft.fcare.databinding.FragmentEditEcgDiagnoseBinding
import com.wxsoft.fcare.ui.details.ecg.EcgViewModel
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class EcgItemListFragment : DaggerFragment() {

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
       return FragmentEcgDiagnosisBinding.inflate(inflater,container, false).apply {
           lifecycleOwner = this@EcgItemListFragment
           viewModel=this@EcgItemListFragment.viewModel
//           list.adapter=
        }.root


    }
}
