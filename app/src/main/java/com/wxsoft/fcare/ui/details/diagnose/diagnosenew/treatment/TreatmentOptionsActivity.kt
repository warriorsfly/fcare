package com.wxsoft.fcare.ui.details.diagnose.diagnosenew.treatment

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.databinding.ActivityTreatmentOptionsBinding
import com.wxsoft.fcare.ui.BaseActivity
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.layout_new_title.*
import javax.inject.Inject

class TreatmentOptionsActivity : BaseActivity() {

    private lateinit var treatmentId:String
    private lateinit var code:String
    companion object {
        const val TREATMENT_ID = "TREATMENT_ID"
        const val CODE = "CODE"
    }

    private lateinit var viewModel: TreatmentOptionsViewModel
    @Inject
    lateinit var factory: ViewModelFactory

    lateinit var binding: ActivityTreatmentOptionsBinding
    lateinit var adapter1: TreatmentAdapter
    lateinit var adapter2: TreatmentAdapter
    lateinit var adapter3: TreatmentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(factory)
        binding = DataBindingUtil.setContentView<ActivityTreatmentOptionsBinding>(this, R.layout.activity_treatment_options)
            .apply {
                adapter1 = TreatmentAdapter(this@TreatmentOptionsActivity,this@TreatmentOptionsActivity.viewModel)
                list1.adapter = adapter1
                adapter2 = TreatmentAdapter(this@TreatmentOptionsActivity,this@TreatmentOptionsActivity.viewModel)
                list2.adapter = adapter2
                adapter3 = TreatmentAdapter(this@TreatmentOptionsActivity,this@TreatmentOptionsActivity.viewModel)
                list3.adapter = adapter3
                viewModel = this@TreatmentOptionsActivity.viewModel
                lifecycleOwner = this@TreatmentOptionsActivity
            }
        treatmentId=intent.getStringExtra(TreatmentOptionsActivity.TREATMENT_ID)?:""
        code=intent.getStringExtra(TreatmentOptionsActivity.CODE)?:""

        viewModel.treatId = treatmentId
        viewModel.code.set(code)

        setSupportActionBar(toolbar)
        title="治疗方案"

        viewModel.loadTreatments()

        viewModel.options.observe(this, Observer {

            when(code){
                "215-1"->{
                    adapter1.submitList(it.filter { it.memo.equals("group1") && it.type==code })
                    adapter2.submitList(it.filter { it.memo.equals("group2")&& it.type==code })
                    adapter3.submitList(it.filter { it.memo.equals("group3") && it.type==code})
                }
                "215-2"->{
                    adapter1.submitList(it.filter { it.type==code })
                }

            }

        })

        viewModel.submitSuccess.observe(this, Observer { haveSelected() })

    }

    private fun haveSelected(){
        Intent().let { intent ->
            val bundle = Bundle()
            viewModel.options.value?.filter { it.checked }?.map {
                bundle.putSerializable("SelectOption", it)
                intent.putExtras(bundle)
                setResult(DaggerAppCompatActivity.RESULT_OK, intent)
                finish()
            }
        }
    }


}
