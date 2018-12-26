package com.wxsoft.fcare.ui.detail.fragment.emr

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wxsoft.fcare.databinding.FragmentEmrBinding
import com.wxsoft.fcare.di.ViewModelFactory
import com.wxsoft.fcare.ui.detail.PatientDetailViewModel
import com.wxsoft.fcare.utils.activityViewModelProvider
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class EmrFragment : DaggerFragment() {

    @Inject
    lateinit var factory: ViewModelFactory


    private lateinit var viewModel: PatientDetailViewModel

    private lateinit var adapter: EmrAdapter
    private lateinit var binding: FragmentEmrBinding

    var patientId:String=""

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = activityViewModelProvider(factory)

        adapter = EmrAdapter(this,viewModel)
        binding.emrRv.layoutManager = LinearLayoutManager(this.context)
        binding.emrRv.adapter = adapter



    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentEmrBinding.inflate(inflater,container,false).apply {
            setLifecycleOwner(this@EmrFragment)
        }

        return binding.root
    }

    companion object {
        const val TAG = "emr_dialog"
    }

}
