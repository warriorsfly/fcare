package com.wxsoft.fcare.ui.detail.fragment.conduitroom


import android.os.Bundle
import android.app.Fragment
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.wxsoft.fcare.R
import com.wxsoft.fcare.databinding.FragmentActivateConduitRoomBinding
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


class ActivateConduitRoomFragment  : WxDimDialogFragment(), HasSupportFragmentInjector, TimesUtils.SelectTimeListener {

    @Inject
    lateinit var factory: ViewModelFactory

    private lateinit var binding: FragmentActivateConduitRoomBinding

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<android.support.v4.app.Fragment>

    private lateinit var viewModel: PatientDetailViewModel

    override fun supportFragmentInjector(): AndroidInjector<android.support.v4.app.Fragment> {
        return fragmentInjector
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = FragmentActivateConduitRoomBinding.inflate(inflater, container, false).apply {
            setLifecycleOwner(this@ActivateConduitRoomFragment)
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

        binding.activeConduitRoomTime.setOnClickListener{

            if (binding.activeConduitRoomTime.text.isEmpty()){
                binding.activeConduitRoomTime.setText(TimesUtils.getCurrentTime())
            }else{
                TimesUtils.selectTime(this.context!!,this,"activeConduitRoomTime")
            }
        }

       binding.activeConduitRoomTimeSubmit.setOnClickListener{
           viewModel.activateConduitTime(binding.activeConduitRoomTime.text.toString())
           dismiss()
       }

    }

    override fun theTime(mTime: String, type: String) {
        if (type.equals("activeConduitRoomTime")){
            binding.activeConduitRoomTime.setText(mTime)
        }
    }


    companion object {
        const val DIALOG_ACTIVE_CONDUIT_ROOM = "dialog_active_conduit_room"
        const val REQ_PERMISSION_CODE = 0x12
    }


}