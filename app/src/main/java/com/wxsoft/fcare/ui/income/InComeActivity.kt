package com.wxsoft.fcare.ui.income

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.wxsoft.fcare.R
import com.wxsoft.fcare.databinding.ActivityInComeBinding
import com.wxsoft.fcare.di.ViewModelFactory
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.utils.TimesUtils
import com.wxsoft.fcare.utils.viewModelProvider
import kotlinx.android.synthetic.main.activity_in_come.*
import kotlinx.android.synthetic.main.layout_income_120_transfer.*
import kotlinx.android.synthetic.main.layout_income_from_others_transfer.*
import kotlinx.android.synthetic.main.layout_income_in_department_transfer.*
import kotlinx.android.synthetic.main.layout_income_self_coming_transfer.*
import javax.inject.Inject

class InComeActivity : BaseActivity(), TimesUtils.SelectTimeListener {

    @Inject
    lateinit var factory: ViewModelFactory

    private lateinit var viewModel: InComeViewModel
    private lateinit var binding:ActivityInComeBinding


    override fun theTime(mTime: String, type: String) {

        when(type){
            "self_arrive"-> self_arrive.text = mTime
            "self_admission"-> self_admission.text = mTime

            "department_admission"-> department_admission.text = mTime
            "department_leave"-> department_leave.text = mTime

            "other_admission"-> other_admission.text = mTime
            "other_ambulance"-> other_ambulance.text = mTime
            "other_arrive"-> other_arrive.text = mTime
            "other_leave_out"-> other_leave_out.text = mTime
            "other_leave_out_arrive"-> other_leave_out_arrive.text = mTime
            "other_transfer"-> other_transfer.text = mTime

            "arrive_120"-> arrive_120.text = mTime
            "arrive_scence_120"-> arrive_scence_120.text = mTime
            "admission_120"-> admission_120.text = mTime
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= DataBindingUtil.setContentView<ActivityInComeBinding>(this,R.layout.activity_in_come)
            .apply{
                setLifecycleOwner(this@InComeActivity)

            }


        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        viewModel = viewModelProvider(factory)

        binding.viewModel=this@InComeActivity.viewModel

        viewModel.patientId=""

        onClick()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_income,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            android.R.id.home->onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun onClick(){
        /**
         * 自行来院
         */
        self_arrive.setOnClickListener{

            if (self_arrive.text.isEmpty()){
                self_arrive.text = TimesUtils.getCurrentTime()
            }else{
                TimesUtils.selectTime(this,this,"self_arrive")
            }

        }

        self_admission.setOnClickListener{

            if (self_admission.text.isEmpty()){
                self_admission.text = TimesUtils.getCurrentTime()
            }else{
                TimesUtils.selectTime(this,this,"self_admission")
            }

        }

        department_admission.setOnClickListener{

            if (department_admission.text.isEmpty()){
                department_admission.text = TimesUtils.getCurrentTime()
            }else{
                TimesUtils.selectTime(this,this,"department_admission")
            }

        }

        department_leave.setOnClickListener{

            if (department_leave.text.isEmpty()){
                department_leave.text = TimesUtils.getCurrentTime()
            }else{
                TimesUtils.selectTime(this,this,"department_leave")
            }

        }

        other_admission.setOnClickListener{

            if (other_admission.text.isEmpty()){
                other_admission.text = TimesUtils.getCurrentTime()
            }else{
                TimesUtils.selectTime(this,this,"other_admission")
            }

        }


        other_ambulance.setOnClickListener{

            if (other_ambulance.text.isEmpty()){
                other_ambulance.text = TimesUtils.getCurrentTime()
            }else{
                TimesUtils.selectTime(this,this,"other_ambulance")
            }
        }
        other_arrive.setOnClickListener{

            if (other_arrive.text.isEmpty()){
                other_arrive.text = TimesUtils.getCurrentTime()
            }else{
                TimesUtils.selectTime(this,this,"other_arrive")
            }
        }

        other_leave_out.setOnClickListener{

            if (other_leave_out.text.isEmpty()){
                other_leave_out.text = TimesUtils.getCurrentTime()
            }else{
                TimesUtils.selectTime(this,this,"other_leave_out")
            }
        }
        other_leave_out_arrive.setOnClickListener{

            if (other_leave_out_arrive.text.isEmpty()){
                other_leave_out_arrive.text = TimesUtils.getCurrentTime()
            }else{
                TimesUtils.selectTime(this,this,"other_leave_out_arrive")
            }
        }

        other_transfer.setOnClickListener{

            if (other_transfer.text.isEmpty()){
                other_transfer.text = TimesUtils.getCurrentTime()
            }else{
                TimesUtils.selectTime(this,this,"other_transfer")
            }
        }

        arrive_120.setOnClickListener{

            if (arrive_120.text.isEmpty()){
                arrive_120.text = TimesUtils.getCurrentTime()
            }else{
                TimesUtils.selectTime(this,this,"arrive_120")
            }
        }

        arrive_scence_120.setOnClickListener{

            if (arrive_scence_120.text.isEmpty()){
                arrive_scence_120.text = TimesUtils.getCurrentTime()
            }else{
                TimesUtils.selectTime(this,this,"arrive_scence_120")
            }
        }

        admission_120.setOnClickListener{

            if (admission_120.text.isEmpty()){
                admission_120.text = TimesUtils.getCurrentTime()
            }else{
                TimesUtils.selectTime(this,this,"admission_120")
            }
        }
    }

}
