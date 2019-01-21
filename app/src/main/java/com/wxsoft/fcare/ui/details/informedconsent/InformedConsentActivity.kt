package com.wxsoft.fcare.ui.details.informedconsent

import android.app.AlertDialog
import android.app.Dialog
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.data.entity.InformedConsent
import com.wxsoft.fcare.core.data.entity.Talk
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.databinding.ActivityInformedConsentBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.details.informedconsent.addinformed.AddInformedConsentActivity
import com.wxsoft.fcare.ui.details.informedconsent.informeddetails.InformedConsentDetailsActivity
import com.wxsoft.fcare.utils.viewModelProvider
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
                setLifecycleOwner(this@InformedConsentActivity)
            }
        patientId=intent.getStringExtra(InformedConsentActivity.PATIENT_ID)?:""
        viewModel.patientId = patientId
        binding.viewModel = viewModel
        back.setOnClickListener { onBackPressed() }

        var adapter = InformedConsentAdapter(this,viewModel)
        viewModel.talkRecords.observe(this, Observer { it -> adapter.items = it ?: emptyList() })
        binding.informedList.adapter = adapter

        viewModel.getTalkRecords(patientId)
        viewModel.getInformedConsents()

        viewModel.informeds.observe(this, Observer {  })

        viewModel.addInformedConsent.observe(this, Observer {
            if (viewModel.informeds.value != null){
                val list = viewModel.informeds.value!!.map { it.name }?.toTypedArray()
                val dialog = AlertDialog.Builder(this@InformedConsentActivity)
                dialog.setTitle("选择知情同意书")
                    .setItems(list, DialogInterface.OnClickListener { _, i ->
                        toAddInformed(viewModel.informeds.value!!.get(i))
                    })
                    .create().show()
            }
        })

        viewModel.talk.observe(this, Observer {
            toDetails(it!!)
        })

    }



    fun toAddInformed(informed: InformedConsent){//新增知情同意书
        var intent = Intent(this, AddInformedConsentActivity::class.java)
        intent.putExtra(AddInformedConsentActivity.PATIENT_ID,patientId)
        intent.putExtra(AddInformedConsentActivity.TITLE_NAME,informed.name)
        intent.putExtra(AddInformedConsentActivity.TITLE_CONTENT,informed.content)
        intent.putExtra(AddInformedConsentActivity.INFORMED_ID,informed.id)
        startActivity(intent)
    }

    fun toDetails(talk:Talk){//知情同意书详情页
        var intent = Intent(this, InformedConsentDetailsActivity::class.java)
        intent.putExtra(InformedConsentDetailsActivity.PATIENT_ID,patientId)
        intent.putExtra(InformedConsentDetailsActivity.TALK_ID,talk.id)
        intent.putExtra(InformedConsentDetailsActivity.TALK_NAME,talk.informedConsentName)
        intent.putExtra(InformedConsentDetailsActivity.INFORMED_ID,talk.informedConsentId)
        startActivity(intent)
    }



}