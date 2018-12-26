package com.wxsoft.fcare.ui.detail

import android.app.PendingIntent
import android.content.Intent
import android.databinding.DataBindingUtil
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.widget.Toast
//import com.squareup.leakcanary.LeakCanary
import com.wxsoft.fcare.R
import com.wxsoft.fcare.databinding.ActivityPatientDetailBinding
import com.wxsoft.fcare.di.ViewModelFactory
import com.wxsoft.fcare.result.EventObserver
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.detail.dialog.BindRfidDialog
import com.wxsoft.fcare.ui.detail.fragment.emr.EmrFragment
import com.wxsoft.fcare.ui.detail.fragment.map.MapFragment
import com.wxsoft.fcare.ui.detail.fragment.timeline.TimeLineFragment
import com.wxsoft.fcare.utils.NfcUtils
import com.wxsoft.fcare.utils.viewModelProvider
import kotlinx.android.synthetic.main.layout_action_bar_tab.*
import javax.inject.Inject


class PatientDetailActivity : BaseActivity() {

    companion object {
        const val PATIENT_ID = "PATIENT_ID"
        private val COUNT = 3 // Agenda
        private const val TIME_LINE_POSITION=0
        private const val PAPER_POSITION=1
        private const val MAP_POSITION=2
    }

    private val timeLineFragment:TimeLineFragment by lazy {
        TimeLineFragment()
    }

    private val emrFragment:EmrFragment by lazy {
        EmrFragment()
    }

    private val mapFragment:MapFragment by lazy {
        MapFragment()
    }

    @Inject lateinit var factory: ViewModelFactory

    private var nfcAdapter: NfcAdapter?=null
    private lateinit var pi:PendingIntent
    private var patientId=""
    private lateinit var viewModel:PatientDetailViewModel

    private lateinit var toast:Toast
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        patientId=intent.getStringExtra(PATIENT_ID)?:""
        viewModel=viewModelProvider(factory)

        var binding:ActivityPatientDetailBinding= DataBindingUtil.setContentView(this,
                R.layout.activity_patient_detail)

        binding.apply {
            setLifecycleOwner(this@PatientDetailActivity)

        }

        tabs.setupWithViewPager(binding.viewpager)


        binding.viewpager.adapter=PageAdapter(supportFragmentManager)
        binding.viewpager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                viewModel.changeTab(position)
            }
        })
        binding.viewModel=viewModel
        //patientId="1"
        viewModel.patientId=patientId

        nfcAdapter= NfcAdapter.getDefaultAdapter(this)

        pi = PendingIntent.getActivity(
            this, 0, Intent(this, javaClass)
                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0
        )

        toast=Toast.makeText(this@PatientDetailActivity,"",Toast.LENGTH_SHORT)

        viewModel.navigateToErrorAction.observe(this,EventObserver{
            toast.setText(it)
            toast.show()
        })
    }


    override fun onResume() {
        super.onResume()
        nfcAdapter?.enableForegroundDispatch(this, pi, null, null) //启动
    }

    override fun onPause() {
        super.onPause()

        nfcAdapter?.disableForegroundDispatch(this) //启动
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        if(NfcAdapter.ACTION_TAG_DISCOVERED==intent!!.action){

            val tagFromIntent = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
            val cardId = NfcUtils. toHexString(tagFromIntent.id)

            val dialog=BindRfidDialog()
            dialog.rfid=cardId
            dialog.show(supportFragmentManager,BindRfidDialog.DIALOG_BIND_RFID)
//            viewModel.list.value!!.card=cardId
        }
    }


    inner class PageAdapter(fm: FragmentManager) :
        FragmentPagerAdapter(fm) {

        override fun getCount() = COUNT

        override fun getItem(position: Int): Fragment {
            return when (position) {
                TIME_LINE_POSITION -> timeLineFragment
                MAP_POSITION->mapFragment
                else->emrFragment
            }
        }

        override fun getPageTitle(position: Int): CharSequence {
            return when (position) {
                TIME_LINE_POSITION -> "时间线"
                PAPER_POSITION -> "病历"
                MAP_POSITION -> "来院GPS"
                else -> ""
            }
        }
    }
}