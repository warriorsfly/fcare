package com.wxsoft.fcare.ui.workspace.notify

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.databinding.ActivityOneTouchCallingBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.details.diagnose.DiagnoseActivity
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class OneTouchCallingActivity : BaseActivity() {

    private lateinit var patientId:String
    companion object {
        const val PATIENT_ID = "PATIENT_ID"
    }
    private lateinit var viewModel: OneTouchCallingViewModel
    @Inject
    lateinit var factory: ViewModelFactory

    lateinit var binding: ActivityOneTouchCallingBinding
    private lateinit var adapter: OneTouchCallingListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(factory)
        binding = DataBindingUtil.setContentView<ActivityOneTouchCallingBinding>(this, R.layout.activity_one_touch_calling)
            .apply {
                adapter = OneTouchCallingListAdapter(this@OneTouchCallingActivity,this@OneTouchCallingActivity.viewModel)
                list.adapter = this@OneTouchCallingActivity.adapter
                lifecycleOwner = this@OneTouchCallingActivity
            }
        patientId = intent.getStringExtra(DiagnoseActivity.PATIENT_ID) ?: ""
        viewModel.patientId = patientId
        viewModel.getCalls()
        viewModel.calls.observe(this, Observer { adapter.submitList(it) })

        viewModel.callNumber.observe(this, Observer { call(it) })

        viewModel.callResult.observe(this, Observer {
            Intent().let { intent->
                setResult(RESULT_OK, intent)
                finish()
            }
        })

    }


    fun call(numer:String){
        val intent = Intent(Intent.ACTION_CALL)
        val data = Uri.parse("tel:${numer}")
        intent.data = data
        startActivity(intent)
    }

}