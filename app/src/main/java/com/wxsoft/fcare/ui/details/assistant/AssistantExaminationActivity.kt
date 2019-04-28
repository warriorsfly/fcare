package com.wxsoft.fcare.ui.details.assistant

import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.result.EventObserver
import com.wxsoft.fcare.core.utils.lazyFast
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.databinding.ActivityAssistantExaminationBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.details.vitalsigns.VitalSignsActivity
import kotlinx.android.synthetic.main.activity_assistant_examination.*
import kotlinx.android.synthetic.main.layout_new_title.*
import javax.inject.Inject

class AssistantExaminationActivity : BaseActivity(){


    private lateinit var patientId:String

    companion object {
        const val PATIENT_ID = "PATIENT_ID"
    }

    private lateinit var viewModel: AssistantExaminationViewModel
    @Inject
    lateinit var factory: ViewModelFactory

    lateinit var binding: ActivityAssistantExaminationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(factory)
        binding = DataBindingUtil.setContentView<ActivityAssistantExaminationBinding>(this, R.layout.activity_assistant_examination)
            .apply {
                viewModel = this@AssistantExaminationActivity.viewModel
                lifecycleOwner = this@AssistantExaminationActivity
            }
        patientId=intent.getStringExtra(VitalSignsActivity.PATIENT_ID)?:""
        viewModel.patientId = patientId

        setSupportActionBar(toolbar)
        title="检查检验"
        viewModel.mesAction.observe(this, EventObserver {
            Toast.makeText(this,it, Toast.LENGTH_SHORT).show()
        })

        viewModel.lisRecords.observe(this, Observer {
            if (it.isNotEmpty()){
                viewPager.adapter = AssistantAdapter(supportFragmentManager,it.size,it.map { it.jylbmc })
            }
        })
    }



}


class AssistantAdapter(fm: FragmentManager,count:Int,val arr:List<String>) :
    FragmentPagerAdapter(fm) {

    private val statusFragments:List<Fragment> by lazyFast {
        (0..(count-1)).map {
            LisFragment(it)
        }
    }

    override fun getItem(position: Int): androidx.fragment.app.Fragment {

        return statusFragments[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return arr.get(position)
    }

    override fun getCount(): Int {
        return statusFragments.size
    }

}
