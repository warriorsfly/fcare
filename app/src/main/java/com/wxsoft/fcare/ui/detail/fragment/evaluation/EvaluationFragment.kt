package com.wxsoft.fcare.ui.detail.fragment.evaluation

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import android.databinding.BindingAdapter
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wxsoft.fcare.data.entity.Dictionary
import com.wxsoft.fcare.databinding.FragmentDictionaryBinding
import com.wxsoft.fcare.di.ViewModelFactory
import com.wxsoft.fcare.ui.detail.PatientDetailViewModel
import com.wxsoft.fcare.utils.activityViewModelProvider
import com.wxsoft.fcare.widget.WxDimDialogFragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.DaggerFragment
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class EvaluationFragment : WxDimDialogFragment(), HasSupportFragmentInjector {

    companion object {
        val TAG= "fragment_evaluation"
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var binding: FragmentDictionaryBinding

    private lateinit var viewModel: PatientDetailViewModel
    private lateinit var adapter: EvaluationAdapter

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return fragmentInjector
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding= FragmentDictionaryBinding.inflate(inflater, container, false).apply {
            setLifecycleOwner (this@EvaluationFragment)
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = activityViewModelProvider(viewModelFactory)

        binding.viewModel=viewModel

        adapter = EvaluationAdapter(viewModel)

        binding.list.adapter = adapter

        viewModel.dictionary.observe(this@EvaluationFragment, Observer { t->

            if(t==null)return@Observer
            adapter.dictionarys=t
            adapter.notifyDataSetChanged()

        })

    }
}
