package com.wxsoft.fcare.ui.details.dominating.fragment.emr

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.databinding.FragmentEmrBinding
import com.wxsoft.fcare.ui.details.dominating.fragment.ProcessStartFragment
import com.wxsoft.fcare.utils.activityViewModelProvider
import com.wxsoft.fcare.utils.lazyFast
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class EmrFragment : DaggerFragment() {

    companion object {

        private const val ARG_PATIENT = "arg.patient"
        @JvmStatic
        fun newInstance( patientId:String): ProcessStartFragment {

            val args = Bundle().apply {
                putString(ARG_PATIENT,patientId)
            }
            return ProcessStartFragment().apply { arguments = args }

        }
    }

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

    private val patientId: String by lazyFast {
        val args = arguments ?: throw IllegalStateException("Missing arguments!")
        args.getString(ARG_PATIENT)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = activityViewModelProvider(factory)

        binding.viewModel=viewModel
        viewModel.patientId=patientId
    }

}
