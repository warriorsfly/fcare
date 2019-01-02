package com.wxsoft.fcare.ui.details.dominating.fragment.emr

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.databinding.FragmentEmrBinding
import com.wxsoft.fcare.utils.viewModelProvider
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class EmrFragment : DaggerFragment() {

    @Inject
    lateinit var factory: ViewModelFactory

    lateinit var binding: FragmentEmrBinding

    private lateinit var viewModel: EmrViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= FragmentEmrBinding.inflate(inflater, container, false).apply {
            setLifecycleOwner(this@EmrFragment)
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = viewModelProvider(factory)
    }

}
