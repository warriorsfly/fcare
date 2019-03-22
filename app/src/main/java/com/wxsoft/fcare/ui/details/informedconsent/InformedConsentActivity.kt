package com.wxsoft.fcare.ui.details.informedconsent

import android.app.Activity
import android.app.AlertDialog
import androidx.lifecycle.Observer
import android.content.Intent
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.data.entity.InformedConsent
import com.wxsoft.fcare.core.data.entity.Talk
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.databinding.ActivityInformedConsentBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.details.informedconsent.addinformed.AddInformedActivity
import com.wxsoft.fcare.ui.details.informedconsent.informeddetails.InformedConsentDetailsActivity
import kotlinx.android.synthetic.main.layout_common_title.*
import javax.inject.Inject


class InformedConsentActivity : BaseActivity()  {

    private lateinit var patientId:String
    companion object {
        const val PATIENT_ID = "PATIENT_ID"
    }
    private lateinit var viewModel: InformedConsentViewModel
    @Inject
    lateinit var factory: ViewModelFactory

    lateinit var binding: ActivityInformedConsentBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(factory)
        binding = DataBindingUtil.setContentView<ActivityInformedConsentBinding>(this, R.layout.activity_informed_consent)
            .apply {
                lifecycleOwner = this@InformedConsentActivity
            }
        patientId=intent.getStringExtra(InformedConsentActivity.PATIENT_ID)?:""
        viewModel.patientId = patientId
        binding.viewModel = viewModel
        back.setOnClickListener { onBackPressed() }

        val adapter = InformedConsentAdapter(this,viewModel)
        viewModel.talkRecords.observe(this, Observer { adapter.items = it ?: emptyList() })
        binding.informedList.adapter = adapter

        viewModel.getTalkRecords(patientId)
        viewModel.getInformedConsents()

        viewModel.informeds.observe(this, Observer {  })

        viewModel.addInformedConsent.observe(this, Observer {
            if (viewModel.informeds.value != null){
                val list = viewModel.informeds.value!!.map { it.name }.toTypedArray()
                val dialog = AlertDialog.Builder(this@InformedConsentActivity)
                dialog.setTitle("选择知情同意书")
                    .setItems(list) { _, i ->
                        toAddInformed(viewModel.informeds.value!![i])
                    }
                    .create().show()
            }
        })

        viewModel.talk.observe(this, Observer {
            toDetails(it!!)
        })

    }



    private fun toAddInformed(informed: InformedConsent){//新增知情同意书
        val intent = Intent(this, AddInformedActivity::class.java)
        intent.putExtra(AddInformedActivity.PATIENT_ID,patientId)
        intent.putExtra(AddInformedActivity.TITLE_NAME,informed.name)
        intent.putExtra(AddInformedActivity.TITLE_CONTENT,informed.content)
        intent.putExtra(AddInformedActivity.INFORMED_ID,informed.id)
        startActivityForResult(intent,100 )
    }

    private fun toDetails(talk:Talk){//知情同意书详情页
        val intent = Intent(this, AddInformedActivity::class.java)
        intent.putExtra(AddInformedActivity.PATIENT_ID,patientId)
        intent.putExtra(AddInformedActivity.TALK_ID,talk.id)
        intent.putExtra(AddInformedActivity.TITLE_NAME,talk.informedConsentName)
        intent.putExtra(AddInformedActivity.INFORMED_ID,talk.informedConsentId)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== Activity.RESULT_OK) {
            when(requestCode){
                100 ->{//
                    viewModel.getTalkRecords(patientId)
                }

            }
        }
    }

}
