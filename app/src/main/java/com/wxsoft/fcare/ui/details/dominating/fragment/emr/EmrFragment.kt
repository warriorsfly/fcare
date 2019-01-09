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
import com.wxsoft.fcare.ui.details.checkbody.CheckBodyActivity
import com.wxsoft.fcare.ui.details.measures.MeasuresActivity
import com.wxsoft.fcare.ui.details.medicalhistory.MedicalHistoryActivity
import com.wxsoft.fcare.ui.details.vitalsigns.VitalSignsActivity
import com.wxsoft.fcare.ui.patient.ProfileActivity
import com.wxsoft.fcare.ui.rating.RatingActivity
import com.wxsoft.fcare.utils.activityViewModelProvider
import com.wxsoft.fcare.utils.clearDecorations
import com.wxsoft.fcare.utils.lazyFast
import dagger.android.support.DaggerFragment
import java.lang.ref.WeakReference
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
        viewModel = activityViewModelProvider(factory)
        binding= FragmentEmrBinding.inflate(inflater, container, false).apply {
            setLifecycleOwner(this@EmrFragment)

            viewModel=this@EmrFragment.viewModel
        }
        adapter= EmrAdapter(this)
        adapter.setActionListener(EventAction(WeakReference(activity!!),patientId))
        binding.list.adapter=adapter

        viewModel.patientId=patientId

        viewModel.emrs.observe(this, Observer {
            //    binding.list?.clearDecorations()
            adapter.items=it ?: emptyList()

        })
        return binding.root




    }

    class EventAction constructor(private val context: WeakReference<Context>,private val patientId:String):EventActions{
        override fun onOpen(id: String) {
            when(id){
                ActionRes.ActionType.患者信息录入->{
                    var intent = Intent(context.get(), ProfileActivity::class.java).apply {
                        putExtra(ProfileActivity.PATIENT_ID, patientId)
                    }
                    context.get()?.startActivity(intent)
                }

                ActionRes.ActionType.GRACE->{
                    var intent = Intent(context.get(), RatingActivity::class.java)
                    context.get()?.startActivity(intent)
                }
                ActionRes.ActionType.生命体征->{
                    var intent = Intent(context.get(), VitalSignsActivity::class.java).apply {
                        putExtra(VitalSignsActivity.PATIENT_ID, patientId)
                    }
                    context.get()?.startActivity(intent)
                }
                ActionRes.ActionType.查体->{
                    var intent = Intent(context.get(), CheckBodyActivity::class.java).apply {
                        putExtra(CheckBodyActivity.PATIENT_ID, patientId)
                    }
                    context.get()?.startActivity(intent)
                }
                ActionRes.ActionType.病史->{
                    var intent = Intent(context.get(), MedicalHistoryActivity::class.java).apply {
                        putExtra(MedicalHistoryActivity.PATIENT_ID, patientId)
                    }
                    context.get()?.startActivity(intent)
                }
                ActionRes.ActionType.措施->{
                    var intent = Intent(context.get(), MeasuresActivity::class.java).apply {
                        putExtra(MeasuresActivity.PATIENT_ID, patientId)
                    }
                    context.get()?.startActivity(intent)
                }

            }
        }

    }

    private val patientId: String by lazyFast {
        val args = arguments ?: throw IllegalStateException("Missing arguments!")
        args.getString(ARG_PATIENT)
    }

}
