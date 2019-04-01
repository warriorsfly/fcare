package com.wxsoft.fcare.ui.launch

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.DOWNLOAD_SERVICE
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
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


class UpgradeDialog : CustomDimDialogFragment(), HasSupportFragmentInjector {

    private val downloadManager:DownloadManager by lazy {
        activity?.getSystemService(DOWNLOAD_SERVICE) as DownloadManager
    }

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var factory: ViewModelFactory

    private  var downloadId:Long=0
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
        return FragmentUpgradeAppBinding.inflate(inflater,container, false)
            .apply {

                update.setOnClickListener { viewModel?.version?.value?.let{
                    download(it)
                } }
                lifecycleOwner = this@UpgradeDialog
                viewModel=this@UpgradeDialog.viewModel
            }.root


    }


    private var completeReceiver: CompleteReceiver? = null


    override fun onDestroy() {
        super.onDestroy()
        completeReceiver?.let{activity?.unregisterReceiver(it)}
    }

    private fun download(version:Version){

        val request = DownloadManager.Request(Uri.parse(BuildConfig.API_ENDPOINT+version.url)).apply {
//        val request = DownloadManager.Request(Uri.parse("http://112.27.113.252:44398/Upload/ECG/3a13b60e265947b6806ad9fd65172597.JPEG")).apply {
            setTitle("更新")
            setDescription(version.description)
            setMimeType("application/vnd.android.package-archive")

        }
        downloadId=downloadManager.enqueue(request)
        completeReceiver = CompleteReceiver(downloadManager)
        activity?.registerReceiver(
            completeReceiver,
            IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        )
        dismiss()
    }

    companion object {
        const val DIALOG_UPGRADE = "DIALOG_UPGRADE"
    }


   class CompleteReceiver ( download:DownloadManager): BroadcastReceiver() {
       private val manager = WeakReference(download)

       override fun onReceive(context: Context, intent: Intent) {

           val completeDownloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
           install(context, completeDownloadId)
       }

       private fun install(context: Context, id: Long) {
           val intent = Intent(Intent.ACTION_VIEW)
           intent.setDataAndType(manager.get()?.getUriForDownloadedFile(id), "application/vnd.android.package-archive")
           intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)//4.0以上系统弹出安装成功打开界面
           context.startActivity(intent)
       }
   }

}

