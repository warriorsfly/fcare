package com.wxsoft.fcare.ui.details.blood.pressure

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.data.entity.BloodPressureItem
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.result.EventObserver
import com.wxsoft.fcare.core.utils.DateTimeUtils
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.databinding.ActivityBloodPressureBinding
import com.wxsoft.fcare.ui.BaseTimingActivity
import kotlinx.android.synthetic.main.activity_blood_pressure.*
import kotlinx.android.synthetic.main.layout_new_title.*
import javax.inject.Inject

class BloodPressureActivity : BaseTimingActivity() , View.OnClickListener{
    override fun onClick(p0: View?) {
        val currentTime = time.text.toString().let { text ->
            if (text.isEmpty()) 0L else DateTimeUtils.formatter.parse(text).time
        }

        dialog = createDialog(currentTime)
        dialog?.show(supportFragmentManager, "all")
    }

    private var selectedId=0

    override fun selectTime(mills: Long) {
        time.text= DateTimeUtils.formatter.format(mills)
    }


    private lateinit var patientId:String
    companion object {
        const val PATIENT_ID = "PATIENT_ID"
    }

    private lateinit var viewModel: BloodPressureViewModel
    @Inject
    lateinit var factory: ViewModelFactory

    lateinit var binding: ActivityBloodPressureBinding

    private lateinit var adapter: BloodPressureAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(factory)
        adapter= BloodPressureAdapter(this,::click)
        binding = DataBindingUtil.setContentView<ActivityBloodPressureBinding>(this, R.layout.activity_blood_pressure)
            .apply {
                list.adapter=this@BloodPressureActivity.adapter
                viewModel=this@BloodPressureActivity.viewModel
                lifecycleOwner = this@BloodPressureActivity
            }
        patientId=intent.getStringExtra(PATIENT_ID)?:""
        viewModel.patientId = patientId

        viewModel.items.observe(this, Observer {
            adapter.submitList(it)
        })

        viewModel.mesAction.observe(this,EventObserver{
            Toast.makeText(this,it,Toast.LENGTH_LONG).show()
        })

        setSupportActionBar(toolbar)
        title="血压监测"
        base_info_item6.setOnClickListener  (this)
    }
    private fun click(item:BloodPressureItem){

        viewModel.select(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_subject,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        return  when(item?.itemId){
            R.id.submit->{
                viewModel.save()
                true
            }
            else->super.onOptionsItemSelected(item)
        }
    }
}
