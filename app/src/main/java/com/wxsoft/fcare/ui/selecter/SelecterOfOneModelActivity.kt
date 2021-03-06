package com.wxsoft.fcare.ui.selecter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.databinding.ActivitySelecterOfOneModelBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.discharge.DisChargeViewModel
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.layout_new_title.*
import javax.inject.Inject

class SelecterOfOneModelActivity : BaseActivity() {

    private lateinit var patientId:String
    private lateinit var comFrom:String
    private lateinit var idStr:String
    private lateinit var ids:List<String>
    companion object {
        const val PATIENT_ID = "PATIENT_ID"
        const val ID = "ID"
        const val COME_FROM = "COME_FROM"
        const val IDS = "IDS"
    }
    private lateinit var viewModel: SelecterOfOneViewModel
    private lateinit var adapter: SelecterOfOneAdapter
    private lateinit var notiadapter: SelecterOfNotifyTypeAdapter
    @Inject
    lateinit var factory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(factory)
        patientId=intent.getStringExtra(SelecterOfOneModelActivity.PATIENT_ID)?:""
        comFrom=intent.getStringExtra(SelecterOfOneModelActivity.COME_FROM)?:""
        idStr=intent.getStringExtra(SelecterOfOneModelActivity.ID)?:""
        ids=intent.getStringArrayListExtra(SelecterOfOneModelActivity.IDS)?: emptyList()
        viewModel.haveSelectedId = idStr
        viewModel.haveSelectedIds = ids
        viewModel.patientId = patientId
        viewModel.typeId = comFrom


        DataBindingUtil.setContentView<ActivitySelecterOfOneModelBinding>(this, R.layout.activity_selecter_of_one_model)
            .apply {
                adapter = SelecterOfOneAdapter(this@SelecterOfOneModelActivity,this@SelecterOfOneModelActivity.viewModel)
                notiadapter = SelecterOfNotifyTypeAdapter(this@SelecterOfOneModelActivity,this@SelecterOfOneModelActivity.viewModel)
                when(comFrom){
                    "Vital" -> firstList.adapter = adapter
                    "Notify" -> firstList.adapter = notiadapter
                    "MedicalHistoryProvider" -> firstList.adapter = adapter
                    "MedicalHistoryAnamnesis" -> firstList.adapter = adapter
                    "ThromSelectPlace" -> firstList.adapter = adapter
                    "Treatment" -> firstList.adapter = adapter
                    "Adress" -> firstList.adapter = adapter
                    "selectHandway" -> firstList.adapter = adapter
                    "selectPatientOutcom" -> firstList.adapter = adapter
                }
                viewModel = this@SelecterOfOneModelActivity.viewModel
                lifecycleOwner = this@SelecterOfOneModelActivity
            }

        setSupportActionBar(toolbar)
        when(comFrom){
            "Vital" -> {
                title="选择意识"
            }
            "Notify" -> title="选择通知类型"
            "MedicalHistoryProvider" -> title="选择病史提供者"
            "MedicalHistoryAnamnesis" -> title="选择既往病史"
            "ThromSelectPlace" -> title="选择溶栓地点"
            "Treatment" -> title="选择无再灌注措施原因"
            "Adress" -> title="选择发病地址"
            "selectHandway" -> title="选择处置措施"
            "selectPatientOutcom" -> title="选择患者去向"
        }


        when(comFrom){
            "Notify"->{
                viewModel.notifyTypes.observe(this, Observer {
                    notiadapter.submitList(it?: emptyList())
                })
            }
            else ->{
                viewModel.des.observe(this, Observer {
                    adapter.submitList(it?: emptyList())
                })
            }
        }

        viewModel.submit.observe(this, Observer { complit() })

    }



    fun complit(){//确定
        Intent().let { intent->
            val bundle = Bundle()
            when(comFrom){
                "Notify" ->{
                    viewModel.notifyTypes.value?.filter { it.checked }?.map {
                        bundle.putSerializable("SelectNotify",it)
                    }
                }

                else ->{
                    when(viewModel.clickAlone){
                        true ->{
                            viewModel.des.value?.filter { it.checked }?.map {
                                bundle.putSerializable("SelectOne",it)
                            }
                        }
                        else ->{
                            val arr = viewModel.des.value?.filter { it.checked }?.toTypedArray()
                            bundle.putSerializable("SelectArray",arr)
                        }
                    }
                }
            }

            intent.putExtras(bundle)
            setResult(DaggerAppCompatActivity.RESULT_OK, intent)
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (comFrom.equals("MedicalHistoryAnamnesis")) menuInflater.inflate(R.menu.menu_subject,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        return  when(item?.itemId){
            R.id.submit->{
                complit()
                true
            }
            else->super.onOptionsItemSelected(item)
        }
    }


}
