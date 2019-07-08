package com.wxsoft.fcare.ui.details.comingby

import android.app.Activity
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.data.entity.EntityIdName
import com.wxsoft.fcare.core.data.entity.User
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.result.EventObserver
import com.wxsoft.fcare.core.utils.DateTimeUtils
import com.wxsoft.fcare.core.utils.lazyFast
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.databinding.ActivityComingByBinding
import com.wxsoft.fcare.ui.BaseTimingActivity
import com.wxsoft.fcare.ui.details.comingby.fragments.ComingByDoctorsActivity
import com.wxsoft.fcare.ui.details.comingby.fragments.ComingByItemListActivity
import kotlinx.android.synthetic.main.layout_new_title.*
import javax.inject.Inject

class ComingByActivity : BaseTimingActivity() {
    override fun selectTime(mills: Long) {
        val newTime=DateTimeUtils.formatter.format(mills)
        viewModel.changedTiming(Pair(timingType,newTime))
        timingType=""
    }


    companion object {
        const val PATIENT_ID = "PATIENT_ID"
    }
    private var timingType:String=""
    private lateinit var viewModel: ComingByViewModel
    @Inject
    lateinit var factory: ViewModelFactory

    lateinit var binding: ActivityComingByBinding

    private val patientId: String by lazyFast {
        intent?.getStringExtra(PATIENT_ID)?:""
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        viewModel = viewModelProvider(factory)
        binding = DataBindingUtil.setContentView<ActivityComingByBinding>(this, R.layout.activity_coming_by)
            .apply {
                viewModel=this@ComingByActivity.viewModel
                lifecycleOwner = this@ComingByActivity
            }
        setSupportActionBar(toolbar)
        title="来院方式"
        viewModel.patientId=patientId

        viewModel.timeLiveData.observe(this, Observer {
            val currentTime= it.second.let { txt->
                if(txt.isNullOrEmpty()) 0L else DateTimeUtils.formatter.parse(txt).time
            }

            timingType=it.first

            dialog = createDialog(currentTime)
            dialog?.show(supportFragmentManager, "all")
        })

//        viewModel.comingType.observe(this, Observer {  })
//        viewModel.comingFrom.observe(this, Observer {  })
//        viewModel.passingKs.observe(this, Observer {  })

//        viewModel.emergencyDoctors.observe(this, Observer {  })
//        viewModel.emergencyNurses.observe(this, Observer {  })
//        viewModel.consultantDoctors.observe(this, Observer {  })

        viewModel.selectType.observe(this, Observer {

            val inti=Intent(this,ComingByItemListActivity::class.java).apply {
                putExtra("type",it)
                putExtra(PATIENT_ID,patientId)

            }

            startActivityForResult(inti,COMING_WAY_TYPES)
        })

        viewModel.selectDoctor.observe(this, Observer {

            val ids=
                    when(it){
                        1->{
                            arrayListOf(EntityIdName(viewModel.comingBy.value?.emergencyDoctor?.id?:"",viewModel.comingBy.value?.emergencyDoctor?.trueName?:""))

                        }
                        2->{
                            arrayListOf(EntityIdName(viewModel.comingBy.value?.emergencyNurse?.id?:"",viewModel.comingBy.value?.emergencyNurse?.trueName?:""))
                        }
                        3->{
                            ArrayList<EntityIdName>().apply {
                                addAll(viewModel.cdoctors.map { EntityIdName(it.id,it.trueName) })
                            }
                        }
                        else->throw IllegalMonitorStateException("error type $it")
                    }

            val inti=Intent(this,ComingByDoctorsActivity::class.java).apply {
                putExtra("type",it)
                putExtra(PATIENT_ID,patientId)
                putExtra("ids",ids)

            }

            startActivityForResult(inti,COMING_WAY_DOCTOR)

        })

        viewModel.messageAction.observe(this, EventObserver {
            Toast.makeText(this,it,Toast.LENGTH_SHORT).show()
        })
        viewModel.saved.observe(this, Observer {
            if(it){
                setResult(Activity.RESULT_OK)
                finish()
            }

        })
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                COMING_WAY_DOCTOR->{
                    val returnType=data?.getIntExtra("type",0)?:0
                    val users=data?.getParcelableArrayListExtra<EntityIdName>("user")

                    viewModel.comingBy.value?.let {
                        when(returnType){
                            1->{
                                it.emergencyDoctor.trueName= users?.get(0)?.name?:""
                                it.emergencyDoctor.id= users?.get(0)?.id?:""
                            }
                            2->{
                                it.emergencyNurse.trueName= users?.get(0)?.name?:""
                                it.emergencyNurse.id= users?.get(0)?.id?:""
                            }
                            3->{
                                viewModel.cdoctors=users?.map {
                                    entity->
                                    User().apply {
                                    id=entity.id
                                        trueName=entity.name
                                }
                                }?: emptyList()
                                it.consultantDoctors=users?.joinToString(separator = ","){it.name}?:""
                            }
                        }
                    }

                }

                COMING_WAY_TYPES->{
                    val returnType=data?.getIntExtra("type",3)?:3
                    val id=data?.getStringExtra("id")?:""
                    val name=data?.getStringExtra("drugName")?:""

                    viewModel.comingBy.value?.let {
                        when(returnType){
                            3->{
                                it.comingWayCode=id
                                it.comingWayName=name
                            }
                            18->{
                                it.dispatchCode=id
                                it.dispatchName=name
                            }
                        }
                    }

                    viewModel.passing.value?.let {
                        when(returnType){
                            5->{
                                it.passingEmergencyCode=id
                                it.passingEmergencyName=name
                            }
                        }
                    }

                }
            }
        }
    }
}
