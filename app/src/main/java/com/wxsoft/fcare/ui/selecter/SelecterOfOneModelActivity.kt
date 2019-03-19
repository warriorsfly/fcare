package com.wxsoft.fcare.ui.selecter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.databinding.ActivitySelecterOfOneModelBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.discharge.DisChargeViewModel
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class SelecterOfOneModelActivity : BaseActivity() {

    private lateinit var patientId:String
    companion object {
        const val PATIENT_ID = "PATIENT_ID"
        const val ID = "ID"
        const val COME_FROM = "COME_FROM"
    }
    private lateinit var viewModel: SelecterOfOneViewModel
    private lateinit var adapter: SelecterOfOneAdapter
    @Inject
    lateinit var factory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(factory)
        DataBindingUtil.setContentView<ActivitySelecterOfOneModelBinding>(this, R.layout.activity_selecter_of_one_model)
            .apply {
                back.setOnClickListener { onBackPressed() }
                submit.setOnClickListener { complit() }
                adapter = SelecterOfOneAdapter(this@SelecterOfOneModelActivity,this@SelecterOfOneModelActivity.viewModel)
                firstList.adapter = adapter
                viewModel = this@SelecterOfOneModelActivity.viewModel
                lifecycleOwner = this@SelecterOfOneModelActivity
            }
        patientId=intent.getStringExtra(SelecterOfOneModelActivity.PATIENT_ID)?:""
        viewModel.patientId = patientId




    }



    fun complit(){//确定
        Intent().let { intent->
            val bundle = Bundle()
//            bundle.putSerializable("coordination",it.id)
//            bundle.putSerializable("coordinationName",it.itemName)
            intent.putExtras(bundle)
            setResult(DaggerAppCompatActivity.RESULT_OK, intent)
            finish()
        }
    }
}
