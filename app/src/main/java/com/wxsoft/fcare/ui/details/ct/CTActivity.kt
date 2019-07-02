package com.wxsoft.fcare.ui.details.ct

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.result.EventObserver
import com.wxsoft.fcare.core.result.Resource
import com.wxsoft.fcare.core.utils.DateTimeUtils
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.databinding.ActivityCtBinding
import com.wxsoft.fcare.ui.BaseTimingActivity
import kotlinx.android.synthetic.main.activity_ct.*
import kotlinx.android.synthetic.main.layout_new_title.*
import javax.inject.Inject

class CTActivity : BaseTimingActivity(), View.OnClickListener {
    override fun selectTime(mills: Long) {
        (findViewById<Button>(selectedId))?.text= DateTimeUtils.formatter.format(mills)
    }

    override fun onClick(v: View?) {


                (v as? Button)?.let {
                    selectedId = it.id
                    val currentTime = it.text.toString().let { text ->
                        if (text.isEmpty()) 0L else DateTimeUtils.formatter.parse(text).time
                    }

                    dialog = createDialog(currentTime)
                    dialog?.show(supportFragmentManager, "all")
                }

    }

    private var selectedId=0

    private lateinit var patientId:String
    companion object {
        const val PATIENT_ID = "PATIENT_ID"
    }
    private lateinit var viewModel: CTViewModel
    @Inject
    lateinit var factory: ViewModelFactory


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(factory)
        DataBindingUtil.setContentView<ActivityCtBinding>(this, R.layout.activity_ct)
            .apply {
                viewModel = this@CTActivity. viewModel
                lifecycleOwner = this@CTActivity
            }
        patientId=intent.getStringExtra(CTActivity.PATIENT_ID)?:""
        viewModel.patientId = patientId

//        back.setOnClickListener { onBackPressed() }

        bill.setOnClickListener  (this)
        start.setOnClickListener  (this)
        end_thromboly_time.setOnClickListener  (this)
        patient_arrive.setOnClickListener  (this)
        start_puncture.setOnClickListener  (this)
        punctured.setOnClickListener  (this)
        start_angiography.setOnClickListener  (this)
        end.setOnClickListener  (this)

        viewModel.mesAction.observe(this,EventObserver{
            Toast.makeText(this,it,Toast.LENGTH_SHORT).show()
        })

//        viewModel.patient.observe(this, Observer {
//            if(it.diagnosisCode=="")
//                model3_3.visibility=View.GONE
//        })

        viewModel.commitResult .observe(this, Observer {
            when(it) {
                is Resource.Success -> {
                    Intent().let { intent ->
                        setResult(RESULT_OK, intent)
                        finish()
                    }
                }
            }
        })

        setSupportActionBar(toolbar)
        title="CT室操作"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_subject,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        return  when(item?.itemId){
            R.id.submit->{
                viewModel.click()
                true
            }
            else->super.onOptionsItemSelected(item)
        }
    }
}
