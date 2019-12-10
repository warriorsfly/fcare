package com.wxsoft.fcare.ui.discharge

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
import androidx.lifecycle.Observer
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.databinding.ActivityDisChargeSonBinding
import com.wxsoft.fcare.ui.BaseActivity
import kotlinx.android.synthetic.main.layout_new_title.*
import javax.inject.Inject

class DisChargeSonActivity : BaseActivity() {


    private lateinit var patientId:String
    private lateinit var titleName:String
    private lateinit var dicType:String
    private lateinit var xt:String
    companion object {
        const val PATIENT_ID = "PATIENT_ID"
        const val TITLE_NAME = "TITLE_NAME"
        const val Dic_Type = "Dic_Type"
        const val IS_XT = "IS_XT"
    }

    private lateinit var viewModel: DisChargeSonViewModel
    @Inject
    lateinit var factory: ViewModelFactory
    lateinit var listAdapter: DisChargeSonAdapter

    lateinit var binding: ActivityDisChargeSonBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(factory)
        binding = DataBindingUtil.setContentView<ActivityDisChargeSonBinding>(this, R.layout.activity_dis_charge_son)
            .apply {
                viewModel = this@DisChargeSonActivity. viewModel
                lifecycleOwner = this@DisChargeSonActivity
            }
        patientId=intent.getStringExtra(PATIENT_ID)?:""
        titleName=intent.getStringExtra(TITLE_NAME)?:""
        dicType=intent.getStringExtra(Dic_Type)?:""
        xt=intent.getStringExtra(IS_XT)?:""
        viewModel.patientId = patientId
        viewModel.dicId = dicType
        setSupportActionBar(toolbar)
        title=titleName

        listAdapter = DisChargeSonAdapter(this,viewModel)
        binding.list.adapter = listAdapter


        viewModel.des.observe(this, Observer {
            listAdapter.items = it
        })

        viewModel.saveResult.observe(this, Observer {
            if (it){
                Intent().let { intent ->
                    setResult(RESULT_OK, intent)
                    finish()
                }
            }
        })

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
