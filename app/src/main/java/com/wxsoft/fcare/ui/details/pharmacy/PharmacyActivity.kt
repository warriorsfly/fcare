package com.wxsoft.fcare.ui.details.pharmacy


import androidx.lifecycle.Observer
import android.content.Intent
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.databinding.ActivityPharmacyBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.core.utils.viewModelProvider
import kotlinx.android.synthetic.main.layout_new_title.*
import javax.inject.Inject


class PharmacyActivity : BaseActivity() {

    private lateinit var patientId:String
    private lateinit var comeFrom:String
    companion object {
        const val PATIENT_ID = "PATIENT_ID"
        const val COME_FROM = "COME_FROM"
        const val DRUG_RECORD_ID = "DRUG_RECORD_ID"

    }

    private lateinit var viewModel: PharmacyViewModel
    @Inject
    lateinit var factory: ViewModelFactory

    lateinit var binding: ActivityPharmacyBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = viewModelProvider(factory)
        binding = DataBindingUtil.setContentView<ActivityPharmacyBinding>(this, R.layout.activity_pharmacy)
            .apply {
                lifecycleOwner = this@PharmacyActivity
            }
        patientId=intent.getStringExtra(PharmacyActivity.PATIENT_ID)?:""
        comeFrom=intent.getStringExtra(PharmacyActivity.COME_FROM)?:""
        viewModel.patientId = patientId
        viewModel.comeFrom = comeFrom
        binding.viewModel = viewModel

        viewModel.getDrugRecord()

        setSupportActionBar(toolbar)
        title="用药"

        val drugsAdapter = DrugsAdapter(this,viewModel)
        viewModel.drugs.observe(this, Observer { drugsAdapter.items = it ?: emptyList() })
        binding.drugsList.adapter = drugsAdapter

        viewModel.pharmacy.observe(this, Observer {  })
        viewModel.drugRecords.observe(this, Observer {  })

        val drugBottomListAdapter = DrugBottomListAdapter(this,viewModel)
        viewModel.selectedDrugs.observe(this, Observer { drugBottomListAdapter.items = it ?: emptyList()  })
        binding.bottomList.adapter = drugBottomListAdapter

        viewModel.backToLast.observe(this, Observer {
            if (comeFrom == "THROMBOLYSIS"){
                Intent().let { intent->
                    val bundle = Bundle()
                    bundle.putSerializable("drugRecords",viewModel.pharmacy.value)
                    intent.putExtras(bundle)
                    setResult(RESULT_OK, intent)
                    finish()
                }
            }else{
                setResult(RESULT_OK, intent)
                finish()
            }
        })
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.menu_subject,menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
//
//        return  when(item?.itemId){
//            R.id.submit->{
//                viewModel.click()
//                true
//            }
//            else->super.onOptionsItemSelected(item)
//        }
//    }

}
