package com.wxsoft.fcare.ui.details.assistant


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer

import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.utils.activityViewModelProvider
import com.wxsoft.fcare.core.utils.lazyFast
import com.wxsoft.fcare.databinding.FragmentLisJyBinding
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.activity_assistant_examination.*
import javax.inject.Inject


class LisJYFragment : DaggerFragment() {

    private lateinit var viewModel: AssistantExaminationViewModel

    @Inject
    lateinit var factory: ViewModelFactory

    lateinit var binding: FragmentLisJyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel=activityViewModelProvider(factory)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding=FragmentLisJyBinding.inflate(inflater,container, false).apply {
            lifecycleOwner = this@LisJYFragment
            viewModel=this@LisJYFragment.viewModel
        }

        viewModel.lisRecords.observe(this, Observer {
            if (it.isNotEmpty()){
                viewPager.adapter = LisJYAdapter(childFragmentManager,it.size,it.map { it.jylbmc })
            }
        })

        return binding.root
    }

}


class LisJYAdapter(fm: FragmentManager, count:Int, val arr:List<String>) :
    FragmentPagerAdapter(fm) {

    private val statusFragments:List<Fragment> by lazyFast {
        (0 until count).map {
            LisFragment(it)
        }
    }

    override fun getItem(position: Int): Fragment {

        return statusFragments[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return arr.get(position)
    }

    override fun getCount(): Int {
        return statusFragments.size
    }

}
