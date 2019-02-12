package com.wxsoft.fcare.ui.details.informedconsent.informeddetails

import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.os.Bundle
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.databinding.ActivityInformedConsentDetailsBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.core.utils.viewModelProvider
import kotlinx.android.synthetic.main.layout_common_title.*
import javax.inject.Inject

class InformedConsentDetailsActivity : BaseActivity() {

    private lateinit var patientId:String
    private lateinit var talkId:String
    private lateinit var talkName:String
    private lateinit var informedId:String
    companion object {
        const val PATIENT_ID = "PATIENT_ID"
        const val TALK_ID = "TALK_ID"
        const val TALK_NAME = "TALK_NAME"
        const val INFORMED_ID = "INFORMED_ID"
    }
    private lateinit var viewModel: InformedConsentDetailsViewModel
    @Inject
    lateinit var factory: ViewModelFactory

    lateinit var binding: ActivityInformedConsentDetailsBinding

    private lateinit var adapter: InformedDetailsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(factory)
        binding = DataBindingUtil.setContentView<ActivityInformedConsentDetailsBinding>(this, R.layout.activity_informed_consent_details)
            .apply {
                lifecycleOwner = this@InformedConsentDetailsActivity
            }
        patientId=intent.getStringExtra(InformedConsentDetailsActivity.PATIENT_ID)?:""
        talkId=intent.getStringExtra(InformedConsentDetailsActivity.TALK_ID)?:""
        talkName=intent.getStringExtra(InformedConsentDetailsActivity.TALK_NAME)?:""
        informedId=intent.getStringExtra(InformedConsentDetailsActivity.INFORMED_ID)?:""

        viewModel.patientId = patientId
        binding.viewModel = viewModel

        back.setOnClickListener { onBackPressed() }

        viewModel.getTalkById(talkId)


//        adapter = InformedDetailsAdapter(this,10)
//        binding.attachments.adapter = adapter
//
        viewModel.talk.observe(this, Observer {
            if (it != null){
//                adapter.remotes = it.attachments.map { it.httpUrl }
                viewModel.getInformedConsentById(it.informedConsentId)
                viewModel.title = it.informedConsentName
            }
        })


    }
}
