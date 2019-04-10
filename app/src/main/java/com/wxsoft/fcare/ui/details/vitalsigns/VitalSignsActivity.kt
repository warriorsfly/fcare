package com.wxsoft.fcare.ui.details.vitalsigns

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.data.entity.Dictionary
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.databinding.ActivityVitalSignsBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.selecter.SelecterOfOneModelActivity
import kotlinx.android.synthetic.main.layout_new_title.*
import javax.inject.Inject

class VitalSignsActivity : BaseActivity() {

    private lateinit var patientId:String
    private lateinit var id:String
    private lateinit var typeId:String

    companion object {
        const val PATIENT_ID = "PATIENT_ID"
        const val ID = "ID"
        const val TYPE_ID = "TYPE_ID"
        const val SELECT_CONCIOUS = 100
    }

    private lateinit var viewModel: VitalSignsViewModel
    @Inject
    lateinit var factory: ViewModelFactory

    lateinit var binding: ActivityVitalSignsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = viewModelProvider(factory)
        binding = DataBindingUtil.setContentView<ActivityVitalSignsBinding>(this, R.layout.activity_vital_signs)
            .apply {
                lifecycleOwner = this@VitalSignsActivity
            }

        setSupportActionBar(toolbar)
        title="生命体征信息"
        patientId=intent.getStringExtra(PATIENT_ID)?:""
        id=intent.getStringExtra(ID)?:""
        typeId=intent.getStringExtra(TYPE_ID)?:""

        binding.viewModel = viewModel

        if(id.isNotEmpty()){
            viewModel.id = id
        }else {
            viewModel.patientId = patientId
        }
        viewModel.sceneTypeId = typeId
        viewModel.loadVitalSign()

        viewModel.backToLast.observe(this, Observer {
            Intent().let { intent->
                setResult(RESULT_OK, intent)
                finish()
            }
        })

        viewModel.clickConcious.observe(this, Observer {
            when(it){
                "Conscious" ->{
                    toSelectConscious()
                }
            }
        })

        binding.breath.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {//输入后的监听

            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {//输入后的监听

            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {//输入文字产生变化的监听

            }
        })



    }



    fun toSelectConscious(){
        val intent = Intent(this, SelecterOfOneModelActivity::class.java).apply {
            putExtra(SelecterOfOneModelActivity.PATIENT_ID, patientId)
            putExtra(SelecterOfOneModelActivity.COME_FROM, "Vital")
            putExtra(SelecterOfOneModelActivity.ID, viewModel.vital.value?.consciousness_Type)
        }
        startActivityForResult(intent,SELECT_CONCIOUS )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== Activity.RESULT_OK) {
            when(requestCode){
                SELECT_CONCIOUS ->{//
                    val conscious= data?.getSerializableExtra("SelectOne") as Dictionary
                    viewModel.vital.value?.consciousness_Type = conscious.id
                    viewModel.vital.value?.consciousnesTypeName = conscious.itemName
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
                viewModel.click()
                true
            }
            else->super.onOptionsItemSelected(item)
        }
    }
}
