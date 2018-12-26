package com.wxsoft.fcare.ui.detail.dialog.share


import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wxsoft.fcare.databinding.DialogShareBinding
import com.wxsoft.fcare.di.ViewModelFactory
import com.wxsoft.fcare.utils.ShareShotUtils
import com.wxsoft.fcare.utils.viewModelProvider
import com.wxsoft.fcare.widget.WxDimDialogFragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 *
 */
class ShareDialog : WxDimDialogFragment(), HasSupportFragmentInjector {

    @Inject
    lateinit var factory: ViewModelFactory


    private lateinit var binding:DialogShareBinding
    private lateinit var viewModel: ShareViewModel

    var patientId:String=""

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return fragmentInjector
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel=viewModelProvider(factory)
        binding.viewModel=viewModel

        viewModel.vital.observe(this, Observer {
            if(it==null)return@Observer

            var shareInfo= arrayOf(
                Pair("生命体征",
                    arrayOf(Pair("意识：",it.consciousness_Type),Pair("血压：",it.blood_Pressure+"次/分钟")
                    )
                ))


            val bitmap= ShareShotUtils(activity!!,viewModel.patientId+"_vital",shareInfo).save()
            binding.image2.setImageBitmap(bitmap.second)
            viewModel.files[1]= bitmap.first
        })

        viewModel.evaluations.observe(this, Observer {

            if(it==null || it.isEmpty())return@Observer

            val details=it.map {item-> Pair("",item.name) }


            val bitmap= ShareShotUtils(activity!!,viewModel.patientId+"_vital", arrayOf(Pair("病情评估",details.toTypedArray()))).save()
            binding.image1.setImageBitmap(bitmap.second)
            viewModel.files[0]= bitmap.first
        })

        viewModel.diagnosis.observe(this, Observer {

            val diagnosis= it?.result ?: return@Observer
            val details=diagnosis.initialDiagnosisDetails.map { detail->Pair("",detail.code) }
            val pairs=ArrayList<Pair<String,String>>()
            pairs.add(Pair("诊断结果：", diagnosis.diagnosis_Code))
            pairs.addAll(details)
            pairs.add(Pair("诊断医生：", diagnosis.diagnosis_Doctor_Name))
            pairs.add(Pair("诊断时间：", diagnosis.diagnosis_Time.replace("T", " ")))

            val bitmap= ShareShotUtils(activity!!,viewModel.patientId+"_vital", arrayOf(Pair("初步诊断",pairs.toTypedArray()))).save()
            binding.image3.setImageBitmap(bitmap.second)
            viewModel.files[2]= bitmap.first
        })
        viewModel.patientId=patientId
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=DialogShareBinding.inflate(inflater,container,false).apply {
            setLifecycleOwner(this@ShareDialog)
        }

        return binding.root
    }

    companion object {
        const val TAG = "share_dialog"
    }

}


