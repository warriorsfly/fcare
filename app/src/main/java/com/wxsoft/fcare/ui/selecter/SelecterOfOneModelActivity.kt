package com.wxsoft.fcare.ui.selecter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
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
    private lateinit var comFrom:String
    companion object {
        const val PATIENT_ID = "PATIENT_ID"
        const val ID = "ID"
        const val COME_FROM = "COME_FROM"
    }
    private lateinit var viewModel: SelecterOfOneViewModel
    private lateinit var adapter: SelecterOfOneAdapter
    private lateinit var notiadapter: SelecterOfNotifyTypeAdapter
    @Inject
    lateinit var factory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(factory)
        patientId=intent.getStringExtra(SelecterOfOneModelActivity.PATIENT_ID)?:""
        comFrom=intent.getStringExtra(SelecterOfOneModelActivity.COME_FROM)?:""
        viewModel.patientId = patientId
        viewModel.typeId = comFrom
        DataBindingUtil.setContentView<ActivitySelecterOfOneModelBinding>(this, R.layout.activity_selecter_of_one_model)
            .apply {
                back.setOnClickListener { onBackPressed() }
                submit.setOnClickListener { complit() }
                adapter = SelecterOfOneAdapter(this@SelecterOfOneModelActivity,this@SelecterOfOneModelActivity.viewModel)
                notiadapter = SelecterOfNotifyTypeAdapter(this@SelecterOfOneModelActivity,this@SelecterOfOneModelActivity.viewModel)
                when(comFrom){
                    "Vital" -> firstList.adapter = adapter
                    "Notify" -> firstList.adapter = notiadapter
                }
                viewModel = this@SelecterOfOneModelActivity.viewModel
                lifecycleOwner = this@SelecterOfOneModelActivity
            }


        when(comFrom){
            "Vital"->{
                viewModel.des.observe(this, Observer {
                    adapter.submitList(it?: emptyList())
                })
            }
            "Notify"->{
                viewModel.notifyTypes.observe(this, Observer {
                    notiadapter.submitList(it?: emptyList())
                })
            }
        }



    }



    fun complit(){//确定
        Intent().let { intent->
            val bundle = Bundle()
            when(comFrom){
                "Notify" ->{
                    viewModel.notifyTypes.value?.filter { it.checked }?.map {
                        bundle.putSerializable("SelectNotify",it)
                    }
                }

                else ->{
                    when(viewModel.clickAlone){
                        true ->{
                            viewModel.des.value?.filter { it.checked }?.map {
                                bundle.putSerializable("SelectConscious",it)
                            }
                        }
                        else ->{

                        }
                    }
                }
            }

            intent.putExtras(bundle)
            setResult(DaggerAppCompatActivity.RESULT_OK, intent)
            finish()
        }
    }
}
