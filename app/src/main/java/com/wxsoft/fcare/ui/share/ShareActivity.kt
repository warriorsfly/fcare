package com.wxsoft.fcare.ui.share

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import cn.jiguang.share.android.api.JShareInterface
import cn.jiguang.share.android.api.PlatActionListener
import cn.jiguang.share.android.api.Platform
import cn.jiguang.share.android.api.ShareParams
import cn.jiguang.share.wechat.Wechat
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.databinding.ActivityShareBinding
import com.wxsoft.fcare.ui.BaseActivity
import javax.inject.Inject
import com.bumptech.glide.Glide
import com.wxsoft.fcare.core.result.Event
import com.wxsoft.fcare.di.GlideApp
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_share.*
import kotlinx.android.synthetic.main.layout_new_title.*
import java.io.File

import java.util.concurrent.Future


class ShareActivity : BaseActivity() {

    private lateinit var patientId:String
    private lateinit var url:ArrayList<String>
    companion object {
        const val PATIENT_ID = "PATIENT_ID"
        const val URL = "URL"
    }
    private lateinit var viewModel: ShareViewModel
    @Inject
    lateinit var factory: ViewModelFactory
    private lateinit var shareListener: PlatActionListener
    private lateinit var binding: ActivityShareBinding
    private var path: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(factory)

        binding = DataBindingUtil.setContentView<ActivityShareBinding>(this, R.layout.activity_share)
            .apply {
                viewModel = this@ShareActivity. viewModel
                lifecycleOwner = this@ShareActivity
            }
        patientId=intent.getStringExtra(PATIENT_ID)?:""
        url=intent.getStringArrayListExtra(URL)

        viewModel.getImageUrl( patientId,url)
        viewModel.url.observe(this, Observer {
            if(it.isNotEmpty())
                loadImage(it)
        })

        shareListener = object : PlatActionListener {
            override fun onComplete(p0: Platform?, p1: Int, p2: HashMap<String, Any>?) {

            }

            override fun onCancel(p0: Platform?, p1: Int) {

            }

            override fun onError(p0: Platform?, p1: Int, p2: Int, p3: Throwable?) {

            }
        }
        setSupportActionBar(toolbar)
        title="分享预览"

    }

    private fun doError(throwable: Throwable){
        viewModel.messageAction.value = Event(throwable.message ?: "错误")
    }

    private fun doImage(file: File){
        try {
            path = file.absolutePath
            GlideApp.with(this).load(file).into(container)
            viewModel.loadUploading.value = false
        }catch (e:Exception){
            error(e)
        }
    }

    private fun loadImage(url:String) {
        disposable.add(
            Single.fromCallable {
                GlideApp.with(this)
                    .asFile().load(url)
                    .submit().get()}
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(::doImage, ::doError)
        )
    }


    fun share(){
        if(JShareInterface.isSupportAuthorize(Wechat.Name) ){
            val params = ShareParams().apply {
                title = "心电图"
                this.comment="心电图"
                imagePath= path
                shareType = Platform.SHARE_IMAGE
            }
            JShareInterface.share(Wechat.Name, params,shareListener )

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_share,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        return  when(item?.itemId){
            R.id.submit->{
                share()
//                viewModel.click()
                true
            }
            else->super.onOptionsItemSelected(item)
        }
    }


}
