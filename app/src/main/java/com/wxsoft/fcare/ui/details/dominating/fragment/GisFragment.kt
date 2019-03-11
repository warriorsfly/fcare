package com.wxsoft.fcare.ui.details.dominating.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.ui.details.dominating.DoMinaViewModel
import com.wxsoft.fcare.core.utils.activityViewModelProvider
import com.wxsoft.fcare.databinding.FragmentTaskGisBinding
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class GisFragment : DaggerFragment() {

    private lateinit var viewModel: DoMinaViewModel

    @Inject
    lateinit var factory: ViewModelFactory


    lateinit var binding: FragmentTaskGisBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel=activityViewModelProvider(factory)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding=FragmentTaskGisBinding.inflate(inflater,container, false).apply {
            lifecycleOwner = this@GisFragment
            viewModel=this@GisFragment.viewModel
        }

        return binding.root
    }
}
