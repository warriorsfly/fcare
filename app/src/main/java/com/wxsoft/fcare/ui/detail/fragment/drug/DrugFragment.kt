package com.wxsoft.fcare.ui.detail.fragment.drug

import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

class DrugFragment: WxDimDialogFragment(), HasSupportFragmentInjector, TimesUtils.SelectTimeListener {

    @Inject
    lateinit var factory: ViewModelFactory

    private lateinit var binding: FragmentDrugBinding

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    private lateinit var viewModel: PatientDetailViewModel

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return fragmentInjector
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = FragmentDrugBinding.inflate(inflater, container, false).apply {
            setLifecycleOwner(this@DrugFragment)
        }

        return binding.root

    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = activityViewModelProvider(factory)
        viewModel.otherDrugs.observe(this@DrugFragment, Observer {
            if (it!=null && it.isNotEmpty()){
                val list = it.map { it.itemName }.toTypedArray()

                AlertDialog.Builder(this.context!!).setItems(list) { _, i ->
                    val drug = it[i]
                    viewModel.drug.value?.anticoagulation_Drug=drug.itemCode
                    viewModel.drug.value?.anticoagulation_Drug_Name=drug.itemName

                }.create().show()
            }
        })
        binding.viewModel=viewModel

        //提交数据
        binding.drugSubmit.setOnClickListener { _ ->
            viewModel.saveAcs(viewModel.drug.value!!)
            dismiss()
        }

        binding.drugtype.setOnClickListener { _ ->

            viewModel.loadOrthDrugs()
        }

        binding.item1Et.setOnClickListener{ _ ->
            if (viewModel.drug.value!!.acs_Delivery_Time.isEmpty()){
                viewModel.drug.value!!.acs_Delivery_Time = TimesUtils.getCurrentTime()
            }else{
                TimesUtils.selectTime(this.context!!,this,"acs_Delivery_Time")
            }
        }
        binding.anticoagulationDate.setOnClickListener{ _ ->
            if (viewModel.drug.value!!.anticoagulation_Date.isEmpty()){
                viewModel.drug.value!!.anticoagulation_Date = TimesUtils.getCurrentTime()
            }else{
                TimesUtils.selectTime(this.context!!,this,"anticoagulationDate")
            }
        }



    }
    override fun theTime(mTime: String, type: String) {
        if (type.equals("acs_Delivery_Time")){
            viewModel.drug.value!!.acs_Delivery_Time = mTime
        }else if(type.equals("anticoagulationDate")){
            viewModel.drug.value!!.anticoagulation_Date = mTime
        }
    }


    companion object {
        const val DIALOG_DRUG = "dialog_drug"
        const val REQ_PERMISSION_CODE = 0x12
    }
}

