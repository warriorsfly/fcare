package com.wxsoft.fcare.ui.details.pharmacy.drugcar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.wxsoft.fcare.R
import com.wxsoft.fcare.databinding.FragmentSelectDrugBagBinding
import com.wxsoft.fcare.ui.details.pharmacy.DrugBagAdapter

class SelectDrugBagFragment constructor(private val lifecycleOwner: LifecycleOwner, val viewModel: DrugCarViewModel): BottomSheetDialogFragment() {

    lateinit var binding: FragmentSelectDrugBagBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding=FragmentSelectDrugBagBinding.inflate(inflater,container,false).apply {

            viewModel=this@SelectDrugBagFragment.viewModel
            lifecycleOwner = this@SelectDrugBagFragment
        }

        val bagAdapter = DrugBagAdapter(lifecycleOwner,viewModel)
        viewModel.drugPackages.observe(this, Observer { bagAdapter.items = it ?: emptyList()  })
        binding.dugBagsList.adapter = bagAdapter

        return binding.root
    }




}