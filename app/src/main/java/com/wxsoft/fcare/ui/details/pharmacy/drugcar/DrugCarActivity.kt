package com.wxsoft.fcare.ui.details.pharmacy.drugcar

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.data.entity.drug.Drug
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.result.EventObserver
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.databinding.ActivityDrugCarBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.details.pharmacy.selectdrugs.SelectDrugsActivity
import kotlinx.android.synthetic.main.layout_new_title.*
import javax.inject.Inject

class DrugCarActivity : BaseActivity() , View.OnClickListener {


    private lateinit var patientId:String
    companion object {
        const val PATIENT_ID = "PATIENT_ID"
        const val AddDrugs = 1
    }

    private lateinit var viewModel: DrugCarViewModel
    @Inject
    lateinit var factory: ViewModelFactory

    lateinit var binding: ActivityDrugCarBinding

    private lateinit var drugCarAdapter: DrugCarAdapter
    private lateinit var selectDrugBagsFragment: SelectDrugBagFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(factory)
        binding = DataBindingUtil.setContentView<ActivityDrugCarBinding>(this, R.layout.activity_drug_car)
            .apply {
                lifecycleOwner = this@DrugCarActivity
            }
        patientId=intent.getStringExtra(DrugCarActivity.PATIENT_ID)?:""
        viewModel.patientId = patientId
        binding.viewModel = viewModel

        setSupportActionBar(toolbar)
        title="用药清单"
        binding.selectDrugbags.setOnClickListener(this)
        binding.selectDrugs.setOnClickListener(this)
        binding.submitContainer.setOnClickListener(this)
        binding.submitAllDrugs.setOnClickListener(this)

        drugCarAdapter = DrugCarAdapter(this,viewModel)
        binding.haveSelectedDugList.adapter = drugCarAdapter

        selectDrugBagsFragment = SelectDrugBagFragment(this,viewModel)

        viewModel.selectedDrugs.observe(this, Observer {
            drugCarAdapter.items= it
            viewModel.initselectNum.value = "已选择药物（ " + it.size + " ）"
            viewModel.initSubmitTitle.value = "提交药物清单"
        })

        viewModel.mesAction.observe(this, EventObserver{
            Toast.makeText(this,it, Toast.LENGTH_SHORT).show()
        })
        viewModel.submitSuccess.observe(this, Observer {
            Intent().let { intent->
                setResult(RESULT_OK, intent)
                finish()
            }
        })

    }


    override fun onClick(v: View?) {
        when(v?.id){
            R.id.select_drugbags -> clickSelectDrugBags()
            R.id.select_drugs -> clickSelectDrugs()
            R.id.submit_container -> clicSubmit()
            R.id.submit_all_drugs -> clicSubmit()
            R.id.cancel ->{//取消
                selectDrugBagsFragment.dismiss()
            }
            R.id.sure_btn ->{//确定
                selectDrugBagsFragment.dismiss()
            }
        }
    }

    fun clickSelectDrugBags(){//快速选择药包
        selectDrugBagsFragment.show(getSupportFragmentManager(), "Dialog");
    }

    fun clickSelectDrugs(){//药品列表选择药品
        val intent = Intent(this, SelectDrugsActivity::class.java).apply {
            putExtra(SelectDrugsActivity.PATIENT_ID, patientId)
        }
        startActivityForResult(intent, DrugCarActivity.AddDrugs)
    }

    fun clicSubmit(){//提交 或删除
        if (viewModel.isEditing.value!!){//编辑状态 删除
            viewModel.deleteSelectdDrug()
            viewModel.initIsEditing.value = false
        }else{ //提交数据
            viewModel.submitDrugs()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== Activity.RESULT_OK) {
            when(requestCode){
                DrugCarActivity.AddDrugs ->{//用药
                    var arr = viewModel.selectedDrugs.value?.map { it }?: emptyList()
                    val drugs = data?.getSerializableExtra("selectedDrugs") as ArrayList<Drug>
                    drugs.addAll(arr)
                    viewModel.initSelectedDrugs.value = drugs
                }

            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_edit,menu)
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
