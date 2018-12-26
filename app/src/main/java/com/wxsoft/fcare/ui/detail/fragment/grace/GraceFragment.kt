package com.wxsoft.fcare.ui.detail.fragment.grace


import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.wxsoft.fcare.databinding.FragmentGraceBinding
import com.wxsoft.fcare.di.ViewModelFactory
import com.wxsoft.fcare.ui.detail.PatientDetailViewModel
import com.wxsoft.fcare.utils.activityViewModelProvider
import com.wxsoft.fcare.widget.WxDimDialogFragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject


class GraceFragment: WxDimDialogFragment(), HasSupportFragmentInjector {

    @Inject
    lateinit var factory: ViewModelFactory

    private lateinit var binding: FragmentGraceBinding

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    private lateinit var viewModel: PatientDetailViewModel

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return fragmentInjector
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = FragmentGraceBinding.inflate(inflater, container, false).apply {
            setLifecycleOwner(this@GraceFragment)
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
        viewModel.vital.observe(this, Observer {
            if (viewModel.vital.value!!.blood_Pressure!=null&&viewModel.vital.value!!.blood_Pressure!=""){
                checkGracevalue()
            }
        })
        viewModel.loadVitalSign()

        binding.graceSubmit.setOnClickListener{
            viewModel.grace.value!!.patientId = viewModel.patientId
            viewModel.saveGrace(viewModel.grace.value!!)
            dismiss()
        }
        binding.graceItem2Bt.setOnClickListener {
            if (viewModel.vital.value!!.blood_Pressure!=null&&viewModel.vital.value!!.blood_Pressure!=""){
                checkGracevalue()
            }else{
                Toast.makeText(this.context,"请先填写生命体征和辅助检查",Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun checkGracevalue(){
        viewModel.grace.value?.gracevalue = calculationGracevalue().toString()
        viewModel.grace.value?.risk_Lamination = calculationRisk(viewModel.grace.value!!.grace_Value)
    }

    private fun calculationGracevalue():Int{

        var va = 0
        if (viewModel.grace.value!!.arrest) va += 39
        if (viewModel.grace.value!!.change) va += 28
        if (viewModel.grace.value!!.rise) va += 14

        va += calculationKillp("III")
        va +=calculationPressure(viewModel.vital.value!!.blood_Pressure.toInt())
        va +=calculationHeartRate(viewModel.vital.value!!.heart_Rate)
        va +=calculationAge(viewModel.patient.value!!.age_Value.toInt())
        va +=calculationCK(50)
        return va
    }

    //Killp分级得分
    private fun calculationKillp(va:String):Int{
        when(va){
            "I" ->{ return 0 }
            "II" ->{ return 20 }
            "III" ->{ return 39 }
            "IV" ->{ return 59 }
        }
        return 0
    }

    //收缩压得分
    private fun calculationPressure(va:Int):Int{
        return when {
            va<80 -> 58
            va in 80..99 -> 53
            va in 100..119 -> 43
            va in 120..139 -> 34
            va in 140..159 -> 24
            va in 160..199 -> 10
            else -> 0
        }
    }
    //心率得分
    private fun calculationHeartRate(va:Int):Int{
        return when {
            va<50 -> 0
            va in 50..69 -> 3
            va in 70..89 -> 9
            va in 90..109 -> 15
            va in 110..149 -> 24
            va in 150..199 -> 38
            else -> 46
        }
    }
    //年龄得分
    private fun calculationAge(va:Int):Int{
        return when {
            va<30 -> 0
            va in 30..39 -> 8
            va in 40..49 -> 25
            va in 50..59 -> 41
            va in 60..69 -> 58
            va in 70..79 -> 75
            else -> 91
        }
    }

    //CK得分
    private fun calculationCK(va:Int):Int{
        return when (va) {
            in 0..34 -> 1
            in 35..69 -> 4
            in 70..104 -> 7
            in 105..139 -> 10
            in 140..174 -> 13
            in 175..349 -> 21
            else -> 28
        }
    }
    //危险程度
    private fun calculationRisk(va:Int):String{
        return when {
            va<=99 -> "4"
            va in 100..140 -> "3"
            va in 141..200 -> "2"
            else -> "1"
        }
    }

    companion object {
        const val DIALOG_GRACE = "dialog_grace"
        const val REQ_PERMISSION_CODE = 0x12
    }


}
