package com.wxsoft.fcare.ui.patient.choice

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.databinding.ActivityChoicePatientBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.patient.ChoicePatientAdapter
import com.wxsoft.fcare.ui.patient.ProfileViewModel
import com.wxsoft.fcare.ui.selecter.SelecterOfOneModelActivity
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.layout_new_title.*
import javax.inject.Inject

class ChoicePatientActivity : BaseActivity() {

    private lateinit var patientId:String
    private var choosing:Boolean = false
    companion object {
        const val PATIENT_ID = "PATIENT_ID"
        const val FOR_TASK_CHOOSE = "FOR_TASK_CHOOSE"
    }

    private lateinit var viewModel: ChoicePatientViewModel
    private lateinit var adapter: ChoicePatientAdapter

    @Inject
    lateinit var factory: ViewModelFactory


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(factory)
        patientId=intent.getStringExtra(PATIENT_ID)?:""
        choosing=intent.getBooleanExtra(FOR_TASK_CHOOSE,false)

        DataBindingUtil.setContentView<ActivityChoicePatientBinding>(this, R.layout.activity_choice_patient)
            .apply {
                adapter = ChoicePatientAdapter(this@ChoicePatientActivity,this@ChoicePatientActivity.viewModel)
                list.adapter = adapter
                viewModel = this@ChoicePatientActivity.viewModel
                lifecycleOwner = this@ChoicePatientActivity
            }

        setSupportActionBar(toolbar)
        title="选择患者"

        viewModel.patientlist.observe(this, Observer {
            adapter.items = it
        })

        if(choosing)viewModel.getPatientsForChoose() else viewModel.getPatientsFromCis()

        viewModel.choiceSome.observe(this, Observer {
            when(it) {
                "choicePatient" ->{
                    Intent().let { intent->
                        val bundle = Bundle()
                        viewModel.patientlist.value?.filter { it.checked }?.map {
                            bundle.putSerializable("SelectPatient",it)
                        }
                        intent.putExtras(bundle)
                        setResult(DaggerAppCompatActivity.RESULT_OK, intent)
                        finish()
                    }
                }
            }
        })
    }
}
