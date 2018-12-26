package com.wxsoft.fcare.ui.detail.fragment.pci



import android.arch.lifecycle.Observer
import android.os.Bundle
import android.content.Context
import android.content.DialogInterface
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wxsoft.fcare.databinding.FragmentPciBinding
import com.wxsoft.fcare.di.ViewModelFactory
import com.wxsoft.fcare.ui.detail.PatientDetailViewModel
import com.wxsoft.fcare.ui.detail.fragment.countdown.CountdownTimeFragment
import com.wxsoft.fcare.ui.detail.fragment.countdown.WxCoudownDialogFragment
import com.wxsoft.fcare.utils.TimesUtils
import com.wxsoft.fcare.utils.activityViewModelProvider
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class PCIFragment: WxCoudownDialogFragment(), HasSupportFragmentInjector ,TimesUtils.SelectTimeListener,
    CountdownTimeFragment.CutDownTimeListener {



    @Inject
    lateinit var factory: ViewModelFactory

    private lateinit var binding: FragmentPciBinding

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    private lateinit var viewModel: PatientDetailViewModel

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return fragmentInjector
    }

    private var progess:Array<String> = arrayOf("step1","step2","step3","step4","step5","step6","step7")


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = FragmentPciBinding.inflate(inflater, container, false).apply {
            setLifecycleOwner(this@PCIFragment)
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

        binding.pciSubmit.setOnClickListener{ _ ->
            for (step in progess){
                if (viewModel.pci.value!!.businessProcess.equals(step)){
                    if (!(step.equals("step7"))){
                        viewModel.pci.value!!.businessProcess = progess[progess.indexOf(step) + 1]
                        if (step.equals("step6")) viewModel.pci.value!!.submitBtnTxt = "提交"
                        break
                    }else{
                        viewModel.savePci(viewModel.pci.value!!)
                        dismiss()
                    }
                }
            }
        }

        binding.lastBtn.setOnClickListener{ _ ->
            for (step in progess){
                if (viewModel.pci.value!!.businessProcess.equals(step)){
                    if (!(step.equals("step1"))){
                        viewModel.pci.value!!.businessProcess = progess[progess.indexOf(step) - 1]
                        if (step.equals("step7")) viewModel.pci.value!!.submitBtnTxt = "下 一 步"
                        break
                    }
                }
            }
        }

        binding.surgeryStartTime.setOnClickListener{ _ ->
            if (viewModel.pci.value!!.decision_Operation_Time.isEmpty()){
                viewModel.pci.value!!.decision_Operation_Time = TimesUtils.getCurrentTime()
            }else{
                TimesUtils.selectTime(this.context!!,this,"surgeryStartTime")
            }
        }


        binding.startAgreeTime.setOnClickListener{ _ ->
            if (viewModel.pci.value!!.start_Agree_Time.isEmpty()){
                viewModel.pci.value!!.start_Agree_Time = TimesUtils.getCurrentTime()
                var dialog= CountdownTimeFragment()
                dialog.mCutDownTimeListener = this
                dialog.type = "startAgreeTime"
                dialog.show(childFragmentManager,CountdownTimeFragment.DIALOG_COUNTDOWN)
            }else{
                TimesUtils.selectTime(this.context!!,this,"startAgreeTime")
            }
        }
        binding.signAgreeTime.setOnClickListener{ _ ->
            if (viewModel.pci.value!!.sign_Agree_Time.isEmpty()){
                viewModel.pci.value!!.sign_Agree_Time = TimesUtils.getCurrentTime()
            }else{
                TimesUtils.selectTime(this.context!!,this,"signAgreeTime")
            }
        }

        binding.startPunctureTime.setOnClickListener{ _ ->
            if (viewModel.pci.value!!.start_Puncture_Time.isEmpty()){
                viewModel.pci.value!!.start_Puncture_Time = TimesUtils.getCurrentTime()
                var dialog= CountdownTimeFragment()
                dialog.mCutDownTimeListener = this
                dialog.type = "startPunctureTime"
                dialog.show(childFragmentManager,CountdownTimeFragment.DIALOG_COUNTDOWN)
            }else{
                TimesUtils.selectTime(this.context!!,this,"startPunctureTime")
            }
        }
        binding.punctureSuccessTime.setOnClickListener{ _ ->
            if (viewModel.pci.value!!.success_Puncture_Time.isEmpty()){
                viewModel.pci.value!!.success_Puncture_Time = TimesUtils.getCurrentTime()
            }else{
                TimesUtils.selectTime(this.context!!,this,"punctureSuccessTime")
            }
        }
        binding.startrRadiographyTime.setOnClickListener{ _ ->
            if (viewModel.pci.value!!.start_Radiography_Time.isEmpty()){
                viewModel.pci.value!!.start_Radiography_Time = TimesUtils.getCurrentTime()
                var dialog= CountdownTimeFragment()
                dialog.mCutDownTimeListener = this
                dialog.type = "startrRadiographyTime"
                dialog.show(childFragmentManager,CountdownTimeFragment.DIALOG_COUNTDOWN)
            }else{
                TimesUtils.selectTime(this.context!!,this,"startrRadiographyTime")
            }
        }
        binding.endRadiographyTime.setOnClickListener{ _ ->
            if (viewModel.pci.value!!.end_Radiography_Time.isEmpty()){
                viewModel.pci.value!!.end_Radiography_Time = TimesUtils.getCurrentTime()
            }else{
                TimesUtils.selectTime(this.context!!,this,"endRadiographyTime")
            }
        }
        binding.passCordisTime.setOnClickListener{ _ ->
            if (viewModel.pci.value!!.balloon_Expansion_Time.isEmpty()){
                viewModel.pci.value!!.balloon_Expansion_Time = TimesUtils.getCurrentTime()
            }else{
                TimesUtils.selectTime(this.context!!,this,"passCordisTime")
            }
        }
        binding.endSurgeryTime.setOnClickListener{ _ ->
            if (viewModel.pci.value!!.end_Operation_Time.isEmpty()){
                viewModel.pci.value!!.end_Operation_Time = TimesUtils.getCurrentTime()
            }else{
                TimesUtils.selectTime(this.context!!,this,"endSurgeryTime")
            }
        }

        binding.determinDoctor.setOnClickListener{
            viewModel.doctors.observe(this@PCIFragment, Observer {
                if (it!!.size>0){
                    val list : Array<String> = Array(it.size, {""})
                    for (i in 0..it.size-1) {
                        list[i] = it.get(i).trueName
                    }
                    AlertDialog.Builder(this.context!!).setItems(list) { _, i ->
                        val doctor = it[i]
                        viewModel.pci.value?.doctor_Name=doctor.trueName
                        viewModel.pci.value?.doctor_Id=doctor.id
                    }.create().show()
                }
            })
        }
        binding.otherDoctor.setOnClickListener{
            viewModel.interventionPerson.observe(this@PCIFragment, Observer {
                if (it!!.size>0){
                    val list : Array<String> = Array(it.size, {""})
                    val bools :BooleanArray = BooleanArray(it.size,{false})
                    for (i in 0..it.size-1) {
                        list[i] = it.get(i).trueName
                    }
                    AlertDialog.Builder(this.context!!)
                        .setMultiChoiceItems(list,bools){ dialogInterface: DialogInterface, i: Int, b: Boolean ->
                        bools[i] = b
                    }.setNegativeButton("确定"){ dialogInterface: DialogInterface, i: Int ->
                            var names = ""
                            var ids = ""
                            for (index in bools.indices){
                                if (bools[index]){
                                    if (names.isEmpty()) names = it.get(index).trueName else names = names+","+ it.get(index).trueName
                                    if (ids.isEmpty()) ids = it.get(index).id else ids = ids+","+ it.get(index).id
                                }
                            }
                            viewModel.pci.value?.intervention_Person_Id=ids
                            viewModel.pci.value?.intervention_Person=names
                     }
                     .create()
                     .show()
                }
            })
        }


    }

    override fun theTime(mTime:String,type:String) {
        when(type){
            "surgeryStartTime"->{viewModel.pci.value?.decision_Operation_Time = mTime}
            "startConduitRoomTime"->{viewModel.pci.value?.start_Conduit_Time = mTime}
            "startAgreeTime"->{viewModel.pci.value?.start_Agree_Time = mTime}
            "signAgreeTime"->{viewModel.pci.value?.sign_Agree_Time = mTime}
            "conduitRoomActiveTime"->{ viewModel.pci.value?.activate_Conduit_Time = mTime}
            "patientArriveTime"->{viewModel.pci.value?.arrive_Conduit_Time = mTime}
            "startPunctureTime"->{viewModel.pci.value?.start_Puncture_Time = mTime}
            "punctureSuccessTime"->{viewModel.pci.value?.success_Puncture_Time = mTime}
            "startrRadiographyTime"->{viewModel.pci.value?.start_Radiography_Time = mTime}
            "endRadiographyTime"->{viewModel.pci.value?.end_Radiography_Time = mTime}
            "passCordisTime"->{viewModel.pci.value?.balloon_Expansion_Time = mTime}
            "endSurgeryTime"->{viewModel.pci.value?.end_Operation_Time = mTime}
        }
    }


    override fun theCutDownTime(mTime: String, type: String) {
        if (type.equals("startAgreeTime")){
            viewModel.pci.value!!.sign_Agree_Time = mTime
        }else if(type.equals("startPunctureTime")){
            viewModel.pci.value!!.success_Puncture_Time = mTime
        }else if(type.equals("startrRadiographyTime")){
            viewModel.pci.value!!.end_Radiography_Time = mTime
        }
    }


    companion object {
        const val DIALOG_PCI = "dialog_pci"
        const val REQ_PERMISSION_CODE = 0x12
    }


}
