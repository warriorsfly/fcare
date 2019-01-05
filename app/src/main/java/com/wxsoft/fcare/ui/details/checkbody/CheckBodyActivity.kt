package com.wxsoft.fcare.ui.details.checkbody

import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.MenuItem
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.databinding.ActivityCheckBodyBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.utils.viewModelProvider
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

    lateinit var adapter: CheckBodyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setTitle("查体")

        viewModel = viewModelProvider(factory)
        binding = DataBindingUtil.setContentView<ActivityCheckBodyBinding>(this, R.layout.activity_check_body)
            .apply {
                setLifecycleOwner(this@CheckBodyActivity)
            }
        patientId=intent.getStringExtra(CheckBodyActivity.PATIENT_ID)?:""
        viewModel.patientId = patientId
        binding.viewModel = viewModel
        viewModel.loadItems()
        viewModel.loadCheckBody()

        adapter = CheckBodyAdapter(this,viewModel,this)
        binding.checkList.adapter = adapter
        viewModel.checkBody.observe(this, Observer {  })

    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle presses on the action bar items
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }


}
