package com.wxsoft.fcare.ui.details.dominating.fragment.emr

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.databinding.FragmentEmrBinding
import com.wxsoft.fcare.utils.activityViewModelProvider
import com.wxsoft.fcare.utils.lazyFast
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class EmrFragment : DaggerFragment() {

    companion object {

        private const val ARG_PATIENT = "arg.patient"
        @JvmStatic
        fun newInstance( patientId:String): EmrFragment {

            val args = Bundle().apply {
                putString(ARG_PATIENT,patientId)
            }
            return EmrFragment().apply { arguments = args }

        }
    }


    @Inject
    lateinit var factory: ViewModelFactory

    lateinit var binding: FragmentEmrBinding

    private lateinit var viewModel: EmrViewModel

    private lateinit var adapter: EmrAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= FragmentEmrBinding.inflate(inflater, container, false).apply {
            setLifecycleOwner(this@EmrFragment)
        }
        adapter= EmrAdapter(this)
        binding.list.adapter=adapter
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

        viewModel.emrs.observe(this, Observer {
            adapter.items=it ?: emptyList()
        })
    }

}
