package com.wxsoft.fcare.ui.details.assistant

import android.arch.lifecycle.Observer
import android.content.DialogInterface
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.databinding.ActivityAssistantExaminationBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.details.assistant.troponin.TroponinFragment
import com.wxsoft.fcare.ui.details.vitalsigns.VitalSignsActivity
import com.wxsoft.fcare.utils.viewModelProvider
import kotlinx.android.synthetic.main.layout_common_title.*
import javax.inject.Inject

class AssistantExaminationActivity : BaseActivity() , DialogInterface.OnDismissListener{


    private lateinit var patientId:String

    companion object {
        const val PATIENT_ID = "PATIENT_ID"
    }

    private lateinit var viewModel: AssistantExaminationViewModel
    @Inject
    lateinit var factory: ViewModelFactory

    lateinit var binding: ActivityAssistantExaminationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(factory)
        binding = DataBindingUtil.setContentView<ActivityAssistantExaminationBinding>(this, R.layout.activity_assistant_examination)
            .apply {
                setLifecycleOwner(this@AssistantExaminationActivity)
            }
        back.setOnClickListener { onBackPressed() }
        patientId=intent.getStringExtra(VitalSignsActivity.PATIENT_ID)?:""

        binding.viewModel = viewModel
        viewModel.patientId = patientId

        var typeAdapter = AssistantTypeAdapter(this,viewModel)
        viewModel.lisItems.observe(this, Observer { it -> typeAdapter.items = it ?: emptyList() })
        binding.typesList.adapter = typeAdapter

        var containerAdapter = AssistantContainerAdapter(this,viewModel)
        viewModel.lisRecords.observe(this, Observer { it -> containerAdapter.items = it ?: emptyList() })
        binding.containerList.adapter = containerAdapter

        viewModel.clickEdit.observe(this, Observer {
            when(it){
                "EDITJGDB"->{
                    toJGDB(viewModel.recordId)
                }
                "ADDJGDB"->{
                    toJGDB("")
                }
            }
        })

    }


    fun toJGDB(id:String){
        var dialog= TroponinFragment()
        dialog.patientId=viewModel.patientId
        dialog.recordId = id
        dialog.show(supportFragmentManager, TroponinFragment.TAG)

    }

    override fun onDismiss(dialog: DialogInterface?) {
        viewModel.getLisRecords(viewModel.selectedType.value!!)
    }

}
