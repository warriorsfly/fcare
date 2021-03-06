package com.wxsoft.fcare.ui.details.pharmacy.drugrecords

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.databinding.ActivityDrugRecordsBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.details.pharmacy.drugcar.DrugCarActivity
import com.wxsoft.fcare.ui.details.pharmacy.selectdrugs.SelectDrugsActivity
import kotlinx.android.synthetic.main.layout_new_title.*
import javax.inject.Inject

class DrugRecordsActivity : BaseActivity() {

    private lateinit var patientId:String
    companion object {
        const val PATIENT_ID = "PATIENT_ID"
        const val AddDrug = 1
    }

    private lateinit var viewModel: DrugRecordsViewModel
    @Inject
    lateinit var factory: ViewModelFactory

    lateinit var binding: ActivityDrugRecordsBinding

    private lateinit var drugRecordsAdapter: DrugRecordsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(factory)
        binding = DataBindingUtil.setContentView<ActivityDrugRecordsBinding>(this, R.layout.activity_drug_records)
            .apply {
                lifecycleOwner = this@DrugRecordsActivity
            }
        patientId=intent.getStringExtra(DrugRecordsActivity.PATIENT_ID)?:""
        viewModel.patientId = patientId
        binding.viewModel = viewModel

        drugRecordsAdapter = DrugRecordsAdapter(this@DrugRecordsActivity,viewModel,false,::clickItem)
        binding.drugRecordsList.adapter = drugRecordsAdapter


        viewModel.drugrecords.observe(this, Observer {
            drugRecordsAdapter.items = it
        })

        viewModel.clikSomething.observe(this, Observer {
            when(it){
                "add"->{//新增
                    val intent = Intent(this, DrugCarActivity::class.java).apply {
                        putExtra(SelectDrugsActivity.PATIENT_ID, patientId)
                    }
                    startActivityForResult(intent,AddDrug)
                }
                "delete" ->{//删除
                    viewModel.refreshList()
                }
            }
        })

        setSupportActionBar(toolbar)
        title="用药记录"
    }

    private fun clickItem(view:View,pos :Int){
        showPopMenu(view,pos)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== Activity.RESULT_OK) {
            when (requestCode) {
                AddDrug->{
                    viewModel.refreshList()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_new,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        return  when(item?.itemId){
            R.id.new_item->{
                viewModel.click()
                true
            }
            else->super.onOptionsItemSelected(item)
        }
    }

    fun showPopMenu(view: View,pos:Int ){
        val popupMenu = PopupMenu(this@DrugRecordsActivity,view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_item_delete,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener {
            viewModel.removeItem(pos)
            return@OnMenuItemClickListener false
        })
        popupMenu.show()
    }




}
