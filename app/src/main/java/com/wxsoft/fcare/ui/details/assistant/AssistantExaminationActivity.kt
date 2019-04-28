package com.wxsoft.fcare.ui.details.assistant

import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
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
        const val JY = 0
        const val JC = 1
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
        title=""

        viewModel.mesAction.observe(this, EventObserver {
            Toast.makeText(this,it, Toast.LENGTH_SHORT).show()
        })

        viewPager.adapter = AssistantAdapter(supportFragmentManager)
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }
            override fun onPageSelected(position: Int) {
                viewModel.loadJcjyShow.value = (position==0)
            }
        })


        viewModel.jcjyShow.observe(this, Observer {
            if (it)viewPager.setCurrentItem(0,true) else viewPager.setCurrentItem(1,true)
        })

    }

}


class AssistantAdapter(fm: FragmentManager) :
    FragmentPagerAdapter(fm) {

    private val statusFragments:List<Fragment> by lazyFast {
        (0..1).map {
            when(it){
                AssistantExaminationActivity.JY -> LisJYFragment()
                AssistantExaminationActivity.JC -> LisJCFragment()
                else ->  throw IllegalStateException("Unknown index $it")
            }
        }
    }

    override fun getItem(position: Int): androidx.fragment.app.Fragment {

        return statusFragments[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return null
    }

    override fun getCount(): Int {
        return statusFragments.size
    }

}
