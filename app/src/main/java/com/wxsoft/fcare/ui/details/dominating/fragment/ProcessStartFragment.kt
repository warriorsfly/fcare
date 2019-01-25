package com.wxsoft.fcare.ui.details.dominating.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.databinding.FragmentProcessStartBinding
import com.wxsoft.fcare.ui.details.dominating.DoMinaViewModel
import com.wxsoft.fcare.utils.activityViewModelProvider
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class ProcessStartFragment : DaggerFragment() {

    companion object {

        @JvmStatic
        fun newInstance():ProcessStartFragment{

            return ProcessStartFragment()

        }
    }

    private lateinit var viewModel: DoMinaViewModel

    @Inject
    lateinit var factory: ViewModelFactory


    lateinit var binding: FragmentProcessStartBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = activityViewModelProvider(factory)
        binding=FragmentProcessStartBinding.inflate(inflater,container, false).apply {
            lifecycleOwner = this@ProcessStartFragment
            viewModel=this@ProcessStartFragment.viewModel
        }

        return binding.root
    }

}
