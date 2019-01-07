package com.wxsoft.fcare.ui.details.dominating.fragment.emr

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.data.dictionary.ActionRes
import com.wxsoft.fcare.databinding.FragmentEmrBinding
import com.wxsoft.fcare.ui.EventActions
import com.wxsoft.fcare.ui.patient.ProfileActivity
import com.wxsoft.fcare.utils.activityViewModelProvider
import com.wxsoft.fcare.utils.clearDecorations
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
        adapter.setActionListener(EventAction(activity!!,patientId))
        binding.list.adapter=adapter
        return binding.root
    }

    class EventAction constructor(private val context: Context,private val patientId:String):EventActions{
        override fun onOpen(id: String) {
            when(id){
                ActionRes.ActionType.患者信息录入->{
                    var intent = Intent(context, ProfileActivity::class.java).apply {
                        putExtra(ProfileActivity.PATIENT_ID, patientId)
                    }
                    context.startActivity(intent)
                }
            }
        }

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
        //    binding.list?.clearDecorations()
            adapter.items=it ?: emptyList()


//            if (it != null && it.isNotEmpty()) {
//                binding.list?.addItemDecoration(
//                    EmrItemDecoration(context!!, it)
//                )
//
//            }
        })
    }

}
