package com.wxsoft.fcare.ui.detail.dialog

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wxsoft.fcare.databinding.DialogVitalSignsBinding
import com.wxsoft.fcare.di.ViewModelFactory
import com.wxsoft.fcare.ui.detail.PatientDetailViewModel
import com.wxsoft.fcare.utils.activityViewModelProvider
import com.wxsoft.fcare.widget.CustomDimDialogFragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class VitalSignDialog : CustomDimDialogFragment(), HasSupportFragmentInjector {



    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    @Inject lateinit var factory: ViewModelFactory

    private lateinit var viewModel: PatientDetailViewModel
    private lateinit var binding: DialogVitalSignsBinding

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return fragmentInjector
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding=DialogVitalSignsBinding.inflate(inflater,container, false)
            .apply {  setLifecycleOwner(this@VitalSignDialog) }

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
        viewModel.loadVitalSign()
        binding.submit.setOnClickListener {
            viewModel.saveVitalSign()

            dismiss()
        }
    }


    companion object {
        const val DIALOG_VITAL_SIGNS = "dialog_vital_signs"
        const val VITAL_SIGNS_ACTIVITY_REQUEST_CODE = 42
    }
}

