package com.wxsoft.fcare.ui.workspace.addpoint

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.utils.lazyFast
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.databinding.ActivityAddTimeLinePointBinding
import com.wxsoft.fcare.ui.BaseActivity
import kotlinx.android.synthetic.main.layout_new_title.*
import javax.inject.Inject

class AddTimeLinePointActivity : BaseActivity() {


    companion object {
        const val PATIENT_ID = "PATIENT_ID"
    }

    private val patientId: String by lazyFast {
        intent?.getStringExtra(PATIENT_ID)?:""
    }

    private lateinit var viewModel: AddTimeLinePointViewModel
    private lateinit var adapter: AddTimeLinePointAdapter
    @Inject
    lateinit var factory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel=viewModelProvider(factory)
        adapter= AddTimeLinePointAdapter(this,viewModel)
        DataBindingUtil.setContentView<ActivityAddTimeLinePointBinding>(this,R.layout.activity_add_time_line_point)
            .apply {
                if (list.adapter == null) list.adapter = this@AddTimeLinePointActivity.adapter
                viewModel = this@AddTimeLinePointActivity.viewModel.apply { patientId = this@AddTimeLinePointActivity.patientId }
                lifecycleOwner = this@AddTimeLinePointActivity
            }

        setSupportActionBar(toolbar)
        title="新增时间节点"

        viewModel.timepoints.observe(this, Observer {
            adapter.submitList(it)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_subject,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        return  when(item?.itemId){
            R.id.submit->{
                Intent().let { intent ->
                    val bundle = Bundle()
                    val piont = viewModel.timepoints.value?.filter { it.checked }?.first()
                    bundle.putSerializable("selectedTimePoint",piont)
                    intent.putExtras(bundle)
                    setResult(RESULT_OK, intent)
                    finish()
                }
                true
            }
            else->super.onOptionsItemSelected(item)
        }
    }

}
