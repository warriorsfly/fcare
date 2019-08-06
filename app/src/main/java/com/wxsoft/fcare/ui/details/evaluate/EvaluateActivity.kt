package com.wxsoft.fcare.ui.details.evaluate

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.data.entity.Dictionary
import com.wxsoft.fcare.core.data.entity.EvaluateItem
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.result.EventObserver
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.databinding.ActivityEvaluatesBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.details.vitalsigns.VitalSignsActivity
import com.wxsoft.fcare.ui.details.vitalsigns.VitalSignsActivity.Companion.SELECT_CONCIOUS
import com.wxsoft.fcare.ui.rating.RatingSubjectActivity
import com.wxsoft.fcare.ui.selecter.SelecterOfOneModelActivity
import com.wxsoft.fcare.utils.ActionCode.Companion.ARG_NEW_ITEM_CODE
import kotlinx.android.synthetic.main.activity_evaluates.*
import kotlinx.android.synthetic.main.layout_new_title.*
import javax.inject.Inject

class EvaluateActivity : BaseActivity(){



    private lateinit var patientId:String
    companion object {
        const val PATIENT_ID = "PATIENT_ID"
    }

    private lateinit var viewModel: EvaluateViewModel
    @Inject
    lateinit var factory: ViewModelFactory

    lateinit var binding: ActivityEvaluatesBinding

    private lateinit var adapter: EvaluateAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(factory)
        adapter= EvaluateAdapter(this,viewModel,::click)
        binding = DataBindingUtil.setContentView<ActivityEvaluatesBinding>(this, R.layout.activity_evaluates)
            .apply {
                list.adapter=this@EvaluateActivity.adapter
                viewModel=this@EvaluateActivity.viewModel
                lifecycleOwner = this@EvaluateActivity
            }
        patientId=intent.getStringExtra(PATIENT_ID)?:""
        viewModel.patientId = patientId

        viewModel.items.observe(this, Observer {
            adapter.submitList(it)
        })

        viewModel.mesAction.observe(this,EventObserver{
            Toast.makeText(this,it,Toast.LENGTH_LONG).show()
        })

        viewModel.rating.observe(this, Observer {
            it ?: return@Observer
            val intent = Intent(this, RatingSubjectActivity::class.java).apply {
                putExtra(RatingSubjectActivity.PATIENT_ID, patientId)
                putExtra(RatingSubjectActivity.RATING_NAME, it.second)
                putExtra(RatingSubjectActivity.RATING_ID, it.first)
                putExtra(RatingSubjectActivity.RECORD_ID, it.third)
            }
            startActivityForResult(intent, ARG_NEW_ITEM_CODE)
            viewModel.rating.value=null
        })

        setSupportActionBar(toolbar)
        title="评估表"
//        base_info_item6.setOnClickListener  (this)
        base_info_item1.setOnClickListener {
            val intent = Intent(this, SelecterOfOneModelActivity::class.java).apply {
                putExtra(SelecterOfOneModelActivity.PATIENT_ID, patientId)
                putExtra(SelecterOfOneModelActivity.COME_FROM, "Vital")
                putExtra(SelecterOfOneModelActivity.ID, viewModel.selectedItem.get()?.consciousness_Type)
            }
            startActivityForResult(intent, VitalSignsActivity.SELECT_CONCIOUS)
        }
    }
    private fun click(item:EvaluateItem){

        viewModel.select(item)
    }

    private fun checkUpDatas(){
        viewModel.selectedItem.get()?.let {
            if((it.consciousness_Type.isNullOrEmpty())&&(!it.evaluatePlanId.equals("9"))){
                showDiag("意识")
                return
            }
            if((it.sbp.isNullOrEmpty())&&(!it.evaluatePlanId.equals("9"))){
                showDiag("舒张压")
                return
            }
            if((it.dbp.isNullOrEmpty())&&(!it.evaluatePlanId.equals("9"))){
                showDiag("收缩压")
                return
            }
            if((it.heart_Rate==0 || it.heart_Rate == null)&&(!it.evaluatePlanId.equals("9"))){
                showDiag("心率")
                return
            }
            if((it.respiration_Rate==0 || it.respiration_Rate == null)&&(!it.evaluatePlanId.equals("9"))){
                showDiag("呼吸")
                return
            }
            if((it.height==0 || it.height == null)&&(it.evaluatePlanId.equals("1"))){
                showDiag("身高")
                return
            }
            if((it.weight==0 || it.weight == null)&&(it.evaluatePlanId.equals("1"))){
                showDiag("体重")
                return
            }
            if((it.spO2==0 || it.spO2 == null)&&(!it.evaluatePlanId.equals("9"))){
                showDiag("血氧")
                return
            }
            viewModel.save()
        }
    }

    private fun showDiag(message:String){
        AlertDialog.Builder(this@EvaluateActivity,R.style.Theme_FCare_Dialog)
            .setMessage(message+"没填写,是否保存？")
            .setPositiveButton("是") { _, _ ->
                viewModel.save()
            }
            .setNegativeButton("否") { _, _ ->
            }.show()
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

        if(resultCode==RESULT_OK) {
            when (requestCode) {
                ARG_NEW_ITEM_CODE -> {
                    val ratingId=data?.getStringExtra("RATINGID")
                    val score=data?.getIntExtra("SCORE",0)
                    val id=data?.getStringExtra("ID")

                    when(ratingId) {
                        "9" -> viewModel.selectedItem.get()?.let {
                            it.nihsS_AnswerRecordId = id
                            it.nihss = score
                        }
                        "10" -> viewModel.selectedItem.get()?.let {
                            it.mrS_AnswerRecordId = id
                            it.mrs = score
                        }
                        "25" -> viewModel.selectedItem.get()?.let {
                            it.bI_AnswerRecordId = id
                            it.bi = score
                        }
                        "21" -> viewModel.selectedItem.get()?.let {
                            it.swallow_AnswerRecordId = id
                            it.swallow = score
                        }
                    }

                }
                SELECT_CONCIOUS->{
                    val conscious= data?.getSerializableExtra("SelectOne") as Dictionary
                    viewModel.selectedItem.get()?.consciousness_Type = conscious.id
                    viewModel.selectedItem.get()?.consciousness_Type_Name = conscious.itemName
                }
            }
        }
    }
}
