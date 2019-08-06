package com.wxsoft.fcare.ui.main.fragment.profile

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.utils.activityViewModelProvider
import com.wxsoft.fcare.databinding.FragmentHospitalChangeBinding
import com.wxsoft.fcare.widget.TransparentDimDialogFragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject


class HospitalChangeFragment : TransparentDimDialogFragment(), HasSupportFragmentInjector {


    @Inject
    lateinit var factory: ViewModelFactory

    private lateinit var viewModel: UserProfileViewModel
    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>
    private lateinit var adapter: HospitalsAdapter

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return fragmentInjector
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activityViewModelProvider(factory)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        viewModel = activityViewModelProvider(factory)
        viewModel?.getAllHospital()
        adapter = HospitalsAdapter(this,viewModel)
        val binding = FragmentHospitalChangeBinding.inflate(inflater, container, false).apply {
            list.adapter = adapter
            lifecycleOwner = this@HospitalChangeFragment
        }
        viewModel.hospitals.observe(this, Observer {
            adapter.submitList(it?: emptyList())
        })

        viewModel.hospitalChanged.observe(this, Observer {

        })

        return binding.root
    }

}