package com.wxsoft.fcare.ui.details.operation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.data.entity.Dictionary
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.databinding.ActivityOperationBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.details.vitalsigns.VitalSignsActivity
import com.wxsoft.fcare.ui.selecter.SelecterOfOneModelActivity
import kotlinx.android.synthetic.main.layout_new_title.*
import javax.inject.Inject

class OperationActivity : BaseActivity() {
    private lateinit var patientId:String

    companion object {
        const val PATIENT_ID = "PATIENT_ID"
        const val SELECT_ROUTE = 100
        const val SELECT_InfarctPosition = 101
        const val SELECT_NarrowLevel = 102
        const val SELECT_NarrowPosition = 103
        const val SELECT_IntracavityImage = 104
        const val SELECT_FunctionTest = 105
        const val SELECT_BracketNum = 106
        const val SELECT_Complication = 107
    }

    private lateinit var viewModel: OperationViewModel
    @Inject
    lateinit var factory: ViewModelFactory

    lateinit var binding: ActivityOperationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(factory)
        binding = DataBindingUtil.setContentView<ActivityOperationBinding>(this, R.layout.activity_operation)
            .apply {
                viewModel = this@OperationActivity.viewModel
                line1.setOnClickListener {
                    toSelectDictionary("Route",SELECT_ROUTE)
                }
                line3.setOnClickListener {
                    toSelectDictionary("InfarctPosition",SELECT_InfarctPosition)
                }
                line4.setOnClickListener {
                    toSelectDictionary("NarrowLevel",SELECT_NarrowLevel)
                }
                line10.setOnClickListener {
                    toSelectDictionary("NarrowPosition",SELECT_NarrowPosition)
                }
                line11.setOnClickListener {
                    toSelectDictionary("IntracavityImage",SELECT_IntracavityImage)
                }
                line12.setOnClickListener {
                    toSelectDictionary("FunctionTest",SELECT_FunctionTest)
                }
                line18.setOnClickListener {
                    toSelectDictionary("BracketNum",SELECT_BracketNum)
                }
                line19.setOnClickListener {
                    toSelectDictionary("Complication",SELECT_Complication)
                }



                lifecycleOwner = this@OperationActivity
            }
        setSupportActionBar(toolbar)
        title="介入详情"
        patientId=intent.getStringExtra(VitalSignsActivity.PATIENT_ID)?:""
        viewModel.patientId = patientId


        viewModel.backToLast.observe(this, Observer {

            Intent().let { intent->
                setResult(RESULT_OK, intent)
                finish()
            }
        })

    }


    private fun toSelectDictionary(comefrom:String,type:Int){
        val intent = Intent(this, SelecterOfOneModelActivity::class.java).apply {
            putExtra(SelecterOfOneModelActivity.PATIENT_ID, patientId)
            putExtra(SelecterOfOneModelActivity.COME_FROM, comefrom)
        }
        startActivityForResult(intent, type)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== Activity.RESULT_OK) {
            when(requestCode){
                SELECT_ROUTE ->{
                    val route= data?.getSerializableExtra("SelectOne") as Dictionary
                    viewModel.operation.value?.route = route.id
                    viewModel.operation.value?.route_Name = route.itemName
                }
                SELECT_NarrowLevel ->{
                    val route= data?.getSerializableExtra("SelectOne") as Dictionary
                    viewModel.operation.value?.narrow_Level = route.id
                    viewModel.operation.value?.narrow_Level_Name = route.itemName
                }
                SELECT_IntracavityImage ->{
                    val route= data?.getSerializableExtra("SelectOne") as Dictionary
                    viewModel.operation.value?.intracavity_Image = route.id
                    viewModel.operation.value?.intracavity_Image_Name = route.itemName
                }
                SELECT_FunctionTest ->{
                    val route= data?.getSerializableExtra("SelectOne") as Dictionary
                    viewModel.operation.value?.function_Test = route.id
                    viewModel.operation.value?.function_Test_Name = route.itemName
                }
                SELECT_BracketNum ->{
                    val route= data?.getSerializableExtra("SelectOne") as Dictionary
                    viewModel.operation.value?.bracket_Num = route.id
                    viewModel.operation.value?.bracket_Num_Name = route.itemName
                }
                SELECT_InfarctPosition ->{
                    val dics = data?.getSerializableExtra("SelectArray") as Array<Dictionary>
                    viewModel.operation.value?.infarct_Position = dics.map { it.id }.joinToString(",")
                    viewModel.operation.value?.infarct_Position_Name = dics.map { it.itemName }.joinToString(",")
                }
                SELECT_NarrowPosition ->{
                    val dics = data?.getSerializableExtra("SelectArray") as Array<Dictionary>
                    viewModel.operation.value?.narrow_Position = dics.map { it.id }.joinToString(",")
                    viewModel.operation.value?.narrow_Position_Name = dics.map { it.itemName }.joinToString(",")
                }
                SELECT_Complication ->{
                    val dics = data?.getSerializableExtra("SelectArray") as Array<Dictionary>
                    viewModel.operation.value?.complication = dics.map { it.id }.joinToString(",")
                    viewModel.operation.value?.complication_Name = dics.map { it.itemName }.joinToString(",")
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
                viewModel.save()
                true
            }
            else->super.onOptionsItemSelected(item)
        }
    }



}
