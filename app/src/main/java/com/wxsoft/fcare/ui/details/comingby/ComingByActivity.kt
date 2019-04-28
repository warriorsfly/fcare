package com.wxsoft.fcare.ui.details.comingby

import android.app.Activity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.jzxiang.pickerview.TimePickerDialog
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.result.EventObserver
import com.wxsoft.fcare.core.utils.DateTimeUtils
import com.wxsoft.fcare.core.utils.inTransaction
import com.wxsoft.fcare.core.utils.lazyFast
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.databinding.ActivityComingByBinding
import com.wxsoft.fcare.ui.BaseTimingActivity
import com.wxsoft.fcare.ui.details.comingby.fragments.ComingByItemListFragment
import kotlinx.android.synthetic.main.layout_new_title.*
import javax.inject.Inject

class ComingByActivity : BaseTimingActivity() {
    override fun onDateSet(timePickerView: TimePickerDialog?, millseconds: Long) {

        val newTime=DateTimeUtils.formatter.format(millseconds)
        viewModel.changedTiming(Pair(timingType,newTime))
        timingType=""
    }

    companion object {
        const val PATIENT_ID = "PATIENT_ID"
    }
    private var timingType:String=""
    private lateinit var viewModel: ComingByViewModel
    @Inject
    lateinit var factory: ViewModelFactory

    lateinit var binding: ActivityComingByBinding

    private val patientId: String by lazyFast {
        intent?.getStringExtra(PATIENT_ID)?:""
    }

    private val  fragment by lazy{
        ComingByItemListFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        viewModel = viewModelProvider(factory)
        binding = DataBindingUtil.setContentView<ActivityComingByBinding>(this, R.layout.activity_coming_by)
            .apply {
                viewModel=this@ComingByActivity.viewModel
                lifecycleOwner = this@ComingByActivity
            }
        setSupportActionBar(toolbar)
        title="来院方式"
        viewModel.patientId=patientId

        viewModel.timeLiveData.observe(this, Observer {
            val currentTime= it.second.let { txt->
                if(txt.isNullOrEmpty()) 0L else DateTimeUtils.formatter.parse(txt).time
            }

            timingType=it.first

            dialog = createDialog(currentTime)
            dialog?.show(supportFragmentManager, "all")
        })

        viewModel.comingType.observe(this, Observer {  })
        viewModel.comingFrom.observe(this, Observer {  })
        viewModel.passingKs.observe(this, Observer {  })

        viewModel.selectType.observe(this, Observer {

            supportFragmentManager.inTransaction {

                setCustomAnimations(
                        R.animator.left_enter,
                        R.animator.left_exit,
                        R.animator.right_enter,
                        R.animator.right_exit)
                addToBackStack(null)
                add(R.id.fragment_container, fragment.apply {

                    type = it
                })
            }
        })
        viewModel.messageAction.observe(this, EventObserver {
            Toast.makeText(this,it,Toast.LENGTH_SHORT).show()
        })
        viewModel.saved.observe(this, Observer {
            if(it){
                setResult(Activity.RESULT_OK)
                finish()
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
