package com.wxsoft.fcare.ui.launch

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.DOWNLOAD_SERVICE
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.wxsoft.fcare.core.BuildConfig
import com.wxsoft.fcare.core.data.entity.version.Version
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.utils.activityViewModelProvider
import com.wxsoft.fcare.databinding.FragmentUpgradeAppBinding
import com.wxsoft.fcare.widget.CustomDimDialogFragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import java.lang.ref.WeakReference
import javax.inject.Inject
import android.os.Environment.DIRECTORY_DOWNLOADS
import androidx.lifecycle.Observer
import com.wxsoft.fcare.widget.TransparentDimDialog
import com.wxsoft.fcare.widget.TransparentDimDialogFragment
import java.io.File


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

