package com.wxsoft.fcare.ui.detail.dialog

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wxsoft.fcare.data.entity.Patient
import com.wxsoft.fcare.databinding.DialogBindRfidBinding
import com.wxsoft.fcare.databinding.DialogShowPatientBinding
import com.wxsoft.fcare.di.ViewModelFactory
import com.wxsoft.fcare.ui.detail.PatientDetailActivity
import com.wxsoft.fcare.ui.detail.PatientDetailViewModel
import com.wxsoft.fcare.utils.activityViewModelProvider
import com.wxsoft.fcare.widget.CustomDimDialogFragment
import com.wxsoft.fcare.widget.WxDimDialogFragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class BindRfidDialog : WxDimDialogFragment(), HasSupportFragmentInjector {

    lateinit var rfid:String
    @Inject lateinit var factory: ViewModelFactory

    private lateinit var viewModel: PatientDetailViewModel
    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    private lateinit var binding: DialogBindRfidBinding

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return fragmentInjector
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding=DialogBindRfidBinding.inflate(inflater,container, false).apply {
            setLifecycleOwner(this@BindRfidDialog)
        }



        return binding.root
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel=activityViewModelProvider(factory)

        binding.viewModel=viewModel
        binding.apply {
            binding.content.text="扫描到RFID卡"+rfid+"，是否与"+viewModel?.patient?.value?.name+"绑定？"
            binding.btnOk.setOnClickListener {
                viewModel?.bindRfid(rfid)
                dismiss()
            }
            binding.btnCancel.setOnClickListener { dismiss() }

        }
    }


    companion object {
        const val DIALOG_BIND_RFID = "dialog_bind_rfid"
        const val SHOW_PATIENT_ACTIVITY_REQUEST_CODE = 45
    }
}
