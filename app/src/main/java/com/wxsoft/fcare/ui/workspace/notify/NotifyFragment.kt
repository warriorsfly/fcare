package com.wxsoft.fcare.ui.workspace.notify

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.utils.activityViewModelProvider
import com.wxsoft.fcare.databinding.FragmentNotifyBinding
import com.wxsoft.fcare.ui.workspace.WorkingViewModel
import com.wxsoft.fcare.widget.WxDimDialogFragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class NotifyFragment : WxDimDialogFragment() , HasSupportFragmentInjector {

    @Inject
    lateinit var factory: ViewModelFactory

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    private lateinit var binding: FragmentNotifyBinding
    private lateinit var viewModel: WorkingViewModel
    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return fragmentInjector
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel=activityViewModelProvider(factory)
        binding=FragmentNotifyBinding.inflate(inflater,container,false).apply {

            viewModel=this@NotifyFragment.viewModel
            lifecycleOwner = this@NotifyFragment
        }

        return binding.root
    }


    companion object {
        const val TAG = "notifi_fragment_dialog"
    }

}