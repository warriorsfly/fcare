package com.wxsoft.fcare.ui.patient


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.utils.activityViewModelProvider
import com.wxsoft.fcare.databinding.FragmentTimeLineBinding
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class TimeLineFragment : DaggerFragment() {


    private lateinit var viewModel: TimeLineViewModel

    @Inject
    lateinit var factory: ViewModelFactory


    lateinit var binding: FragmentTimeLineBinding
    lateinit var adapter: TimeLineAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel=activityViewModelProvider(factory)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        adapter= TimeLineAdapter()
        binding= FragmentTimeLineBinding.inflate(inflater,container, false).apply {
            lifecycleOwner = this@TimeLineFragment

            list.adapter=this@TimeLineFragment.adapter
            viewModel=this@TimeLineFragment.viewModel
        }

        viewModel.timelines.observe(this, Observer {
            adapter.submitList(it)
        })
        return binding.root
    }

}
