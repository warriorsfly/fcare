package com.wxsoft.fcare.ui.launch

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.utils.activityViewModelProvider
import com.wxsoft.fcare.databinding.FragmentUpgradeAppBinding
import com.wxsoft.fcare.widget.TransparentDimDialogFragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject


class UpgradeDialog : TransparentDimDialogFragment(), HasSupportFragmentInjector {



    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var factory: ViewModelFactory

    private lateinit var viewModel: LauncherViewModel
    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return fragmentInjector
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        viewModel=activityViewModelProvider(factory)
        viewModel.update.observe(this, Observer {
            dismiss()
        })
        return FragmentUpgradeAppBinding.inflate(inflater,container, false)
            .apply {

                update.setOnClickListener { viewModel?.version?.value?.let{
                    viewModel?.startUpdate()
                } }
                lifecycleOwner = this@UpgradeDialog
                viewModel=this@UpgradeDialog.viewModel
            }.root


    }


    companion object {
        const val DIALOG_UPGRADE = "DIALOG_UPGRADE"
    }




}

