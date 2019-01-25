package com.wxsoft.fcare.ui.details.measures


import android.app.AlertDialog
import android.arch.lifecycle.Observer
import android.content.DialogInterface
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.MenuItem
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.data.entity.Dictionary
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.databinding.ActivityMeasuresBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.details.pharmacy.PharmacyActivity
import com.wxsoft.fcare.ui.details.thrombolysis.ThrombolysisActivity
import com.wxsoft.fcare.utils.viewModelProvider
import kotlinx.android.synthetic.main.layout_common_title.*
import javax.inject.Inject

class MeasuresActivity : BaseActivity()  {

    private lateinit var patientId:String
    companion object {
        const val PATIENT_ID = "PATIENT_ID"
    }
    private lateinit var viewModel: MeasuresViewModel
    @Inject
    lateinit var factory: ViewModelFactory

    lateinit var binding: ActivityMeasuresBinding

    lateinit var adapter: MeasuresAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = viewModelProvider(factory)
        binding = DataBindingUtil.setContentView<ActivityMeasuresBinding>(this, R.layout.activity_measures)
            .apply {
                lifecycleOwner = this@MeasuresActivity
            }
        patientId=intent.getStringExtra(MeasuresActivity.PATIENT_ID)?:""
        viewModel.patientId = patientId

        back.setOnClickListener { onBackPressed() }

        adapter = MeasuresAdapter(this,viewModel)
        binding.viewModel = viewModel
        binding.measuresList.adapter = adapter

        viewModel.loadMeasure()

        viewModel.pharmacy.observe(this, Observer {
            showDialog(it!!)
        })

        viewModel.measure.observe(this, Observer {  })

        viewModel.resultString.observe(this, Observer {
            Intent().let { intent->
                setResult(RESULT_OK, intent);
                finish();
            }
        })

    }



    fun showDialog(item: Dictionary){
        AlertDialog.Builder(this)
            .setMessage("确定"+item.itemName+"吗？")
            .setTitle(item.itemName)
            .setPositiveButton("是", DialogInterface.OnClickListener { _, _ ->
                item.checked = true
                if (item.id.equals("212-5")){//用药
                    toPharmacy()
                }else if(item.id.equals("212-6")){//溶栓
                    toThrombolysis()
                }

            })
            .setNeutralButton("否", DialogInterface.OnClickListener { _, _ ->
                item.checked = false
            })
            .create()
            .show()
    }

    fun toPharmacy(){//用药界面
        var intent = Intent(this, PharmacyActivity::class.java)
        intent.putExtra(PharmacyActivity.PATIENT_ID,patientId)
        startActivity(intent)
    }

    fun toThrombolysis(){//溶栓
        var intent = Intent(this, ThrombolysisActivity::class.java)
        intent.putExtra(ThrombolysisActivity.PATIENT_ID,patientId)
        startActivity(intent)
    }




}
