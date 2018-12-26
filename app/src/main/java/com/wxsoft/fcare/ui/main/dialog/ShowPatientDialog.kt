package com.wxsoft.fcare.ui.main.dialog

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wxsoft.fcare.data.entity.Patient
import com.wxsoft.fcare.databinding.DialogShowPatientBinding
import com.wxsoft.fcare.ui.detail.PatientDetailActivity
import com.wxsoft.fcare.widget.WxDimDialogFragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class ShowPatientDialog : WxDimDialogFragment(), HasSupportFragmentInjector {


    lateinit var patient:Patient

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    private lateinit var binding: DialogShowPatientBinding

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return fragmentInjector
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding=DialogShowPatientBinding.inflate(inflater,container, false).apply {
            setLifecycleOwner(this@ShowPatientDialog)
        }

        return binding.root
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.apply {
            binding.content.text="扫描到"+patient.name+"，是否查看其详细信息？"
            binding.btnOk.setOnClickListener {
                toDetail(patient.id)
                dismiss()
            }
            binding.btnCancel.setOnClickListener { dismiss() }

        }
    }

    fun toDetail(id:String){
        var intent= Intent(activity!!, PatientDetailActivity::class.java)
        intent.putExtra(PatientDetailActivity.PATIENT_ID,id)
        startActivity(intent)
    }

    companion object {
        const val DIALOG_SHOW_PATIENT = "dialog_show_patient"
        const val SHOW_PATIENT_ACTIVITY_REQUEST_CODE = 44
    }
}
