package com.wxsoft.fcare.ui.detail.fragment.profile

import android.content.Context
import android.content.pm.PackageManager
import android.nfc.NfcAdapter
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wxsoft.fcare.databinding.FragmentProfileBinding
import com.wxsoft.fcare.di.ViewModelFactory
import com.wxsoft.fcare.ui.detail.PatientDetailViewModel
import com.wxsoft.fcare.utils.TimesUtils
import com.wxsoft.fcare.utils.activityViewModelProvider
import com.wxsoft.fcare.widget.WxDimDialogFragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.fragment_profile.*
import javax.inject.Inject

class ProfileFragment : WxDimDialogFragment(), HasSupportFragmentInjector, TimesUtils.SelectTimeListener  {
    override fun theTime(mTime: String, type: String) {
        when (type) {
            "attack" -> attack.text = mTime
            "help_time" -> help_time.text = mTime

        }
    }

    @Inject lateinit var factory: ViewModelFactory

    private lateinit var binding: FragmentProfileBinding

    private lateinit var viewModel: PatientDetailViewModel

    private lateinit var nfcAdapter: NfcAdapter

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>


    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return fragmentInjector
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = FragmentProfileBinding.inflate(inflater, container, false).apply {
            setLifecycleOwner(this@ProfileFragment)
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

        binding.submit.setOnClickListener { _ ->
            if(checkPatientAllowUpdate()){
                viewModel.savePatientInfo()
                dismiss()
            }
        }

        attack.setOnClickListener{

            if (attack.text.isEmpty()){
                attack.setText(TimesUtils.getCurrentTime())
            }else{
                TimesUtils.selectTime(activity!!,this,"attack")
            }
        }

        help_time.setOnClickListener{

            if (help_time.text.isEmpty()){
                help_time.setText(TimesUtils.getCurrentTime())
            }else{
                TimesUtils.selectTime(activity!!,this,"help_time")
            }
        }



//        binding.rfid.setOnClickListener { _ ->
//
//            if (
//                    PermissionChecker.checkSelfPermission(activity!!, Manifest.permission.NFC)== PackageManager.PERMISSION_GRANTED
////                    &&PermissionChecker.checkSelfPermission(activity!!, Manifest.permission.NFC_TRANSACTION_EVENT)== PackageManager.PERMISSION_GRANTED
//            ){
//                nfcAdapter= NfcAdapter.getDefaultAdapter(activity!!)
//                //readNFC()
//            }else {
//                ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.NFC, Manifest.permission.NFC_TRANSACTION_EVENT), REQ_PERMISSION_CODE)
//            }
//
//        }
    }
    fun checkPatientAllowUpdate():Boolean{
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQ_PERMISSION_CODE) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED
//                    &&grantResults[1] == PackageManager.PERMISSION_GRANTED
            ) {
                //readNFC()
                nfcAdapter= NfcAdapter.getDefaultAdapter(activity!!)
            }
        }
    }




    companion object {
        const val DIALOG_PROFILE = "dialog_profile"
        const val REQ_PERMISSION_CODE = 0x12
    }
}

