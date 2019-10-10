package com.wxsoft.fcare.ui.details.catheter

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.data.entity.EntityIdName
import com.wxsoft.fcare.core.data.entity.User
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.result.EventObserver
import com.wxsoft.fcare.core.result.Resource
import com.wxsoft.fcare.core.utils.DateTimeUtils
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.databinding.ActivityCatheterBinding
import com.wxsoft.fcare.ui.BaseTimingActivity
import com.wxsoft.fcare.ui.details.comingby.ComingByActivity
import com.wxsoft.fcare.ui.details.informedconsent.addinformed.AddInformedActivity
import kotlinx.android.synthetic.main.layout_new_title.*
import javax.inject.Inject

class CatheterActivity : BaseTimingActivity(){
    override fun clearTime(mills: Long) {
        dialog?.onDestroy()
        dialog=null
        (findViewById<TextView>(selectedId))?.text= ""
    }

    override fun selectTime(mills: Long) {
        dialog?.onDestroy()
        dialog=null
        (findViewById<TextView>(selectedId))?.text= DateTimeUtils.formatter.format(mills)
    }

    private val selectedIndex= mutableListOf<Int>()

    private fun showDatePicker(v: View?){
        (v as? TextView)?.let {
            selectedId=it.id
            val currentTime= it.text.toString().let { txt->
                if(txt.isEmpty()) 0L else DateTimeUtils.formatter.parse(txt).time
            }

            dialog = createDialog(currentTime)
            dialog?.show(supportFragmentManager, "all")
        }
    }

    private fun selectPlace(){
//        val list= viewModel.docs.map { it.trueName }.toTypedArray()
//
//        val selectedItems= viewModel.docs.map { user ->
//
//            viewModel.intervention.value?.interventionMateIds?.contains(user.id)?:false
//        }.toBooleanArray()
//
//        AlertDialog.Builder(this).setMultiChoiceItems(list,selectedItems)
//        { _, which, isChecked ->
//            if(selectedIndex.contains(which) && !isChecked){
//                selectedIndex.remove(which)
//
//            }else if(!selectedIndex.contains(which) && isChecked){
//                selectedIndex.add(which)
//            }
//
//            viewModel.intervention.value?.interventionMateIds=selectedIndex.joinToString {
//                viewModel.docs[it].id
//            }
//
//            viewModel.intervention.value?.interventionMates=selectedIndex.joinToString {
//                viewModel.docs[it].trueName
//            }
//        }.setPositiveButton("确定") { _, _ ->
//
//        }.show()


        val ids=
            ArrayList<EntityIdName>().apply {
                addAll(viewModel.intervention.value?.interventionMateIds?.split(',')!!.map {
                    EntityIdName(it,"")
                })
            }

        val inti=Intent(this,CatheterDoctorsActivity::class.java).apply {
            putExtra("type",3)
            putExtra(ComingByActivity.PATIENT_ID,patientId)
            putExtra("ids",ids)

        }

        startActivityForResult(inti,CATHETER_DOCTOR)


    }

    private fun selectDoc(){
//        val list= viewModel.docs.map { it.trueName }.toTypedArray()
//
//        val selectedPos= viewModel.docs.indexOfFirst { user ->
//
//            viewModel.intervention.value?.doctorId==user.id
//        }
//
//        AlertDialog.Builder(this).setSingleChoiceItems(list,selectedPos){
//            dialog,which->
//            dialog.dismiss()
//            viewModel.intervention.value?.apply {
//                doctorId=viewModel.docs[which].id
//                doctorName=viewModel.docs[which].trueName
//            }
//        }.setPositiveButton("确定") { _, _ ->
//
//        }.show()


        val ids=
            arrayListOf(EntityIdName(viewModel.intervention.value?.doctorId?:"",viewModel.intervention.value?.doctorName?:""))
        val inti=Intent(this,CatheterDoctorsActivity::class.java).apply {
            putExtra("type",1)
            putExtra(ComingByActivity.PATIENT_ID,patientId)
            putExtra("ids",ids)
        }

        startActivityForResult(inti,CATHETER_DOCTOR)


    }

    private var selectedId=0
    private lateinit var xt:String

    private lateinit var patientId:String
    companion object {
        const val PATIENT_ID = "PATIENT_ID"
        const val IS_XT = "IS_XT"
        const val INFORMED_CONSENT = 20
    }
    private lateinit var viewModel: CatheterViewModel
    @Inject
    lateinit var factory: ViewModelFactory


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(factory)
        DataBindingUtil.setContentView<ActivityCatheterBinding>(this, R.layout.activity_catheter)
            .apply {
                viewModel = this@CatheterActivity. viewModel
                lifecycleOwner = this@CatheterActivity
            }
        patientId=intent.getStringExtra(CatheterActivity.PATIENT_ID)?:""
        viewModel.patientId = patientId

//        back.setOnClickListener { onBackPressed() }
        xt=intent.getStringExtra(IS_XT)?:""

        viewModel.xtShow.set(xt.equals("xt"))

        setSupportActionBar(toolbar)
        title="导管室操作"

        viewModel.informed.observe(this, Observer {  })

        viewModel.mesAction.observe(this,EventObserver{
            Toast.makeText(this,it,Toast.LENGTH_SHORT).show()
        })

        viewModel.commitResult.observe(this, Observer {
            when(it) {
                is Resource.Success -> {
                    Intent().let { intent ->

                        setResult(RESULT_OK, intent)
                        finish()
                    }
                }
            }
        })

        viewModel.modifySome.observe(this, Observer {
            when(it){
                "-2"->showDatePicker(findViewById(R.id.doc_1))
                "-1"->selectDoc()
                "0"->selectPlace()
//                "1"->showDatePicker(findViewById(R.drugId.start))
                "1"->showDatePicker(findViewById(R.id.start))
                "2"->showDatePicker(findViewById(R.id.end_thromboly_time))

                "3"->showDatePicker(findViewById(R.id.patient_arrive))
                "4"->showDatePicker(findViewById(R.id.start_puncture))
                "5"->showDatePicker(findViewById(R.id.punctured))
                "6"->showDatePicker(findViewById(R.id.start_angiography))
                "7"->showDatePicker(findViewById(R.id.angiographied))
                "8"->showDatePicker(findViewById(R.id.wire))
                "9"->showDatePicker(findViewById(R.id.end))
                "10"->showDatePicker(findViewById(R.id.leave))
                "21"->showDatePicker(findViewById(R.id.start1))
                "22"->showDatePicker(findViewById(R.id.start1_1))
//                "11"->showDatePicker(findViewById(R.id.end_1))
            }
        })

        viewModel.clickLine.observe(this, Observer {
            when(it){
                "informedConsent" ->{
                    if (viewModel.intervention.value?.informedConsentId.isNullOrEmpty()){
                        toInformedConsent()
                    }else{
                        toSeeInformedConsent()
                    }
                }
            }
        })


    }


    private fun  toInformedConsent(){
        val intent = Intent(this@CatheterActivity, AddInformedActivity::class.java).apply {
            putExtra(AddInformedActivity.PATIENT_ID,patientId)
            putExtra(AddInformedActivity.TITLE_NAME,viewModel.informed.value?.name)
            putExtra(AddInformedActivity.TITLE_CONTENT,viewModel.informed.value?.content)
            putExtra(AddInformedActivity.INFORMED_ID,viewModel.informed.value?.id)
            putExtra(AddInformedActivity.COME_FROM,"THROMBOLYSIS")
        }
        startActivityForResult(intent, INFORMED_CONSENT)
    }

    private fun toSeeInformedConsent(){
        val intent = Intent(this@CatheterActivity, AddInformedActivity::class.java).apply {
            putExtra(AddInformedActivity.PATIENT_ID,patientId)
            putExtra(AddInformedActivity.TALK_ID,viewModel.intervention.value?.informedConsentId)
            putExtra(AddInformedActivity.TITLE_NAME,viewModel.informed.value?.name)
            putExtra(AddInformedActivity.INFORMED_ID,viewModel.informed.value?.id)
            putExtra(AddInformedActivity.COME_FROM,"THROMBOLYSIS")
        }
        startActivityForResult(intent, INFORMED_CONSENT)
    }




    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== Activity.RESULT_OK) {
            when(requestCode){
                INFORMED_CONSENT ->{//知情同意书
                    viewModel.intervention.value?.informedConsentId = data?.getStringExtra("informedConsentId")?:""
                    viewModel.intervention.value?.start_Agree_Time = data?.getStringExtra("startTime")?:""
                    viewModel.intervention.value?.sign_Agree_Time = data?.getStringExtra("endTime")?:""
                    viewModel.intervention.value?.getInformedTime()
//                    viewModel.intervention.value?.allTime = data?.getStringExtra("allTime")?:""
                }
                CATHETER_DOCTOR ->{
                    val returnType=data?.getIntExtra("type",0)?:0
                    val users=data?.getParcelableArrayListExtra<EntityIdName>("user")
                    viewModel.intervention.value?.let {
                        when(returnType){
                            1->{
                                it.doctorName= users?.get(0)?.name?:""
                                it.doctorId= users?.get(0)?.id?:""
                            }
                            3->{
                                it.interventionMateIds=users?.joinToString(separator = ","){it.id}?:""
                                it.interventionMates=users?.joinToString(separator = ","){it.name}?:""
                            }
                        }
                    }
                }
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_subject,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        return  when(item?.itemId){
            R.id.submit->{
                viewModel.saving()
                true
            }
            else->super.onOptionsItemSelected(item)
        }
    }

}
