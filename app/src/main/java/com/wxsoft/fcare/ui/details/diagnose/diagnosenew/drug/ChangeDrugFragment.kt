package com.wxsoft.fcare.ui.details.diagnose.diagnosenew.drug


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

import com.wxsoft.fcare.R
import com.wxsoft.fcare.databinding.FragmentChangeDrugBinding
import com.wxsoft.fcare.databinding.FragmentSelectDrugBagBinding
import com.wxsoft.fcare.ui.details.pharmacy.DrugBagAdapter
import com.wxsoft.fcare.ui.details.pharmacy.drugcar.DrugCarViewModel

class ChangeDrugFragment constructor(private val lifecycleOwner: LifecycleOwner, val viewModel: AcsDrugViewModel,val point:String): BottomSheetDialogFragment() {

    lateinit var binding: FragmentChangeDrugBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding= FragmentChangeDrugBinding.inflate(inflater,container,false).apply {

            viewModel=this@ChangeDrugFragment.viewModel
            lifecycleOwner = this@ChangeDrugFragment
        }
        val adapter = ChangeDrugAdapter(this@ChangeDrugFragment,this@ChangeDrugFragment.viewModel)
        binding.drugsList.adapter = adapter

        if (this@ChangeDrugFragment.point.equals("1")){
            viewModel.drugs1.observe(this, Observer { adapter.submitList(it) })
        }else{
            viewModel.drugs2.observe(this, Observer { adapter.submitList(it) })
        }


        return binding.root
    }

}