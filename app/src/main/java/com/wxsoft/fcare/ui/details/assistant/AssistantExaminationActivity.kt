package com.wxsoft.fcare.ui.details.assistant

import androidx.lifecycle.Observer
import android.content.DialogInterface
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.data.entity.lis.LisRecord
import com.wxsoft.fcare.core.data.entity.lis.LisRecordItem
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.databinding.ActivityAssistantExaminationBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.details.assistant.troponin.TroponinFragment
import com.wxsoft.fcare.ui.details.vitalsigns.VitalSignsActivity
import com.wxsoft.fcare.core.utils.viewModelProvider
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
                lifecycleOwner = this@AssistantExaminationActivity
            }
        back.setOnClickListener { onBackPressed() }
        patientId=intent.getStringExtra(VitalSignsActivity.PATIENT_ID)?:""

        binding.viewModel = viewModel
        viewModel.patientId = patientId

        val typeAdapter = AssistantTypeAdapter(this,viewModel)
        viewModel.lisItems.observe(this, Observer { typeAdapter.items = it ?: emptyList() })
        binding.typesList.adapter = typeAdapter

        val containerAdapter = AssistantContainerAdapter(this,viewModel)
        viewModel.lisRecords.observe(this, Observer {
            if (it.isNullOrEmpty()&&!viewModel.selectedType.value.equals("3")) containerAdapter.items = viewModel.getDataModel() else containerAdapter.items = it
        })
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


    private fun toJGDB(id:String){
        val dialog= TroponinFragment()
        dialog.patientId=viewModel.patientId
        dialog.recordId = id
        dialog.show(supportFragmentManager, TroponinFragment.TAG)

    }

    override fun onDismiss(dialog: DialogInterface?) {
        viewModel.getLisRecords(viewModel.selectedType.value!!)
    }



}
