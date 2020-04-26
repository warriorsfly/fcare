package com.wxsoft.fcare.ui.details.assistant


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.utils.activityViewModelProvider
import com.wxsoft.fcare.core.utils.lazyFast
import com.wxsoft.fcare.databinding.FragmentLisJcBinding
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_lis_jc.*
import kotlinx.android.synthetic.main.item_stub_emr_vital_list.view.*
import javax.inject.Inject

class LisJCFragment : DaggerFragment() {

    private lateinit var viewModel: AssistantExaminationViewModel

    @Inject
    lateinit var factory: ViewModelFactory

    lateinit var binding: FragmentLisJcBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel=activityViewModelProvider(factory)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= FragmentLisJcBinding.inflate(inflater,container, false).apply {
            lifecycleOwner = this@LisJCFragment
            viewModel=this@LisJCFragment.viewModel
        }

        viewModel.lisJCRecords.observe(viewLifecycleOwner, Observer { list ->
            if (list.isNotEmpty()){
                viewPager.adapter = LisJCAdapter(childFragmentManager,list.size,list.map { it.jylbmc })
//                tabLayout.getTabAt(0).setCustomView()
            }
        })



        return binding.root
    }

}

class LisJCAdapter(fm: FragmentManager, count:Int, val arr:List<String>) :
    FragmentPagerAdapter(fm) {

    private val statusFragments:List<Fragment> by lazyFast {
        (0..(count-1)).map {
            LisJCItemFragment(it)
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


