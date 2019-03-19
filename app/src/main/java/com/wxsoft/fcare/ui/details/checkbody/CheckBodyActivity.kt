package com.wxsoft.fcare.ui.details.checkbody

import android.app.Activity
import androidx.lifecycle.Observer
import android.content.Intent
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.databinding.ActivityCheckBodyBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.ui.details.checkbody.select.SelectBodyItemsActivity
import kotlinx.android.synthetic.main.layout_common_title.*
import javax.inject.Inject

class CheckBodyActivity : BaseActivity()  {

    private lateinit var patientId:String
    companion object {
        const val PATIENT_ID = "PATIENT_ID"
    }

    private lateinit var viewModel: CheckBodyViewModel
    @Inject
    lateinit var factory: ViewModelFactory

    lateinit var binding: ActivityCheckBodyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        viewModel = viewModelProvider(factory)
        binding = DataBindingUtil.setContentView<ActivityCheckBodyBinding>(this, R.layout.activity_check_body)
            .apply {
                lifecycleOwner = this@CheckBodyActivity
            }
        patientId=intent.getStringExtra(CheckBodyActivity.PATIENT_ID)?:""
        viewModel.patientId = patientId
        binding.viewModel = viewModel
        back.setOnClickListener { onBackPressed() }


        viewModel.backToLast.observe(this, Observer {
            Intent().let { intent->
                setResult(RESULT_OK, intent)
                finish()
            }
        })

        viewModel.selectItem.observe(this, Observer {
            toSelect(it)
        })

    }


    private fun toSelect(ids:String){
        val intent = Intent(this, SelectBodyItemsActivity::class.java).apply {
            putExtra(SelectBodyItemsActivity.PATIENT_ID, patientId)
            putExtra(SelectBodyItemsActivity.SELECT_TYPE_ID, ids)
            when(ids){
                "1"->  putExtra(SelectBodyItemsActivity.SELECT_ID_1, viewModel.checkBody.value?.coordination)
                "2"->  putExtra(SelectBodyItemsActivity.SELECT_ID_1, viewModel.checkBody.value?.skin)
                "3"->  {
                    putExtra(SelectBodyItemsActivity.SELECT_ID_1, viewModel.checkBody.value?.leftPupils)
                    putExtra(SelectBodyItemsActivity.SELECT_ID_2, viewModel.checkBody.value?.leftResponseLight)
                }
                "4"->  {
                    putExtra(SelectBodyItemsActivity.SELECT_ID_1, viewModel.checkBody.value?.rightPupils)
                    putExtra(SelectBodyItemsActivity.SELECT_ID_2, viewModel.checkBody.value?.rightResponseLight)
                }
            }
        }
        startActivityForResult(intent, ids.toInt())
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== Activity.RESULT_OK) {
            when(requestCode){
                1 ->{//检查过程
                    val coordination = data?.getSerializableExtra("coordination") as String
                    val coordinationName = data?.getSerializableExtra("coordinationName") as String
                    binding.select1.setText(coordinationName)
                    viewModel.checkBody.value?.coordination = coordination
                }
                2 ->{//皮肤
                    val skin = data?.getSerializableExtra("skin") as String
                    val skinName = data?.getSerializableExtra("skinName") as String
                    binding.select2.setText(skinName)
                    viewModel.checkBody.value?.skin = skin
                }
                3 ->{//左瞳孔
                    val leftPupils = data?.getSerializableExtra("leftPupils") as String
                    val leftPupilsName = data?.getSerializableExtra("leftPupilsName") as String
                    val leftResponse = data?.getSerializableExtra("leftResponse") as String
                    val leftResponseName = data?.getSerializableExtra("leftResponseName") as String
                    binding.select3.setText(leftPupilsName + " " + leftResponseName)
                    viewModel.checkBody.value?.leftPupils = leftPupils
                    viewModel.checkBody.value?.leftResponseLight = leftResponse
                }
                4 ->{//右瞳孔
                    val rightPupils = data?.getSerializableExtra("rightPupils") as String
                    val rightPupilsName = data?.getSerializableExtra("rightPupilsName") as String
                    val rightResponse = data?.getSerializableExtra("rightResponse") as String
                    val rightResponseName = data?.getSerializableExtra("rightResponseName") as String
                    binding.select4.setText(rightPupilsName + " " + rightResponseName)
                    viewModel.checkBody.value?.rightPupils = rightPupils
                    viewModel.checkBody.value?.rightResponseLight = rightResponse
                }

            }
        }
    }


}
