package com.wxsoft.fcare.ui.details.dominating.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.databinding.FragmentProcessArriveHosBinding
import com.wxsoft.fcare.ui.details.dominating.DoMinaViewModel
import com.wxsoft.fcare.utils.activityViewModelProvider
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class ProcessArriveHosFragment : DaggerFragment() {

    companion object {

        @JvmStatic
        fun newInstance():ProcessArriveHosFragment{

            return ProcessArriveHosFragment()

        }
    }

    private lateinit var viewModel: DoMinaViewModel

    @Inject
    lateinit var factory: ViewModelFactory


    lateinit var binding: FragmentProcessArriveHosBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding=FragmentProcessArriveHosBinding.inflate(inflater,container, false).apply {
            setLifecycleOwner(this@ProcessArriveHosFragment)
        }

        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        viewModel = activityViewModelProvider(factory)
        binding.viewModel=viewModel

    }
}
