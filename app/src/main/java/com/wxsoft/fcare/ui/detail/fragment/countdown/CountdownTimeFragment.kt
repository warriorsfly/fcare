package com.wxsoft.fcare.ui.detail.fragment.countdown


import android.app.AlertDialog
import android.os.Bundle
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer
import com.wxsoft.fcare.databinding.FragmentCountdownTimeBinding

import com.wxsoft.fcare.databinding.FragmentDrugBinding
import com.wxsoft.fcare.di.ViewModelFactory
import com.wxsoft.fcare.ui.detail.PatientDetailViewModel
import com.wxsoft.fcare.utils.TimesUtils
import com.wxsoft.fcare.utils.activityViewModelProvider
import com.wxsoft.fcare.widget.WxDimDialogFragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject


class CountdownTimeFragment: WxCoudownDialogFragment(), HasSupportFragmentInjector, Chronometer.OnChronometerTickListener {

    interface CutDownTimeListener{
        fun theCutDownTime(mTime:String,type:String)
    }
    public var mCutDownTimeListener: CutDownTimeListener? = null
    public var type:String? = null
    @Inject
    lateinit var factory: ViewModelFactory

    private lateinit var binding: FragmentCountdownTimeBinding

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<android.support.v4.app.Fragment>

    private lateinit var viewModel: PatientDetailViewModel

    override fun supportFragmentInjector(): AndroidInjector<android.support.v4.app.Fragment> {
        return fragmentInjector
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = FragmentCountdownTimeBinding.inflate(inflater, container, false).apply {
            setLifecycleOwner(this@CountdownTimeFragment)
        }
        viewModel = activityViewModelProvider(factory)


        return binding.root

    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.viewModel=viewModel
        binding.chronometer.start()
        binding.countdownTimeSubmit.setOnClickListener { _ ->
            binding.chronometer.stop()
            mCutDownTimeListener!!.theCutDownTime(TimesUtils.getCurrentTime(), type!!)
            dismiss()
        }

    }

    override fun onChronometerTick(chronometer: Chronometer?) {

    }

    companion object {
        const val DIALOG_COUNTDOWN = "dialog_countdown"
        const val REQ_PERMISSION_CODE = 0x12
    }

}
