package com.wxsoft.fcare.ui.share

import android.os.Bundle
import android.os.Handler
import android.os.Message
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
import com.wxsoft.fcare.ui.outcome.OutComeActivity
import kotlinx.android.synthetic.main.layout_common_title.*
import javax.inject.Inject
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.layout_new_title.*

import java.util.concurrent.ExecutionException


class ShareActivity : BaseActivity() {

    private lateinit var patientId:String
    private lateinit var typeId:String
    companion object {
        const val PATIENT_ID = "PATIENT_ID"
        const val TYPE_ID = "TYPE_ID"
    }
    private lateinit var viewModel: ShareViewModel
    @Inject
    lateinit var factory: ViewModelFactory

    private lateinit var shareListener: PlatActionListener
    private lateinit var binding: ActivityShareBinding
    private lateinit var adapter: ShareAdapter
    private var path: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(factory)

        binding = DataBindingUtil.setContentView<ActivityShareBinding>(this, R.layout.activity_share)
            .apply {
//                uri = url.toString()
                adapter = ShareAdapter(this@ShareActivity,this@ShareActivity.viewModel)
                shareList.adapter = adapter
                viewModel = this@ShareActivity. viewModel
                lifecycleOwner = this@ShareActivity
            }
        patientId=intent.getStringExtra(ShareActivity.PATIENT_ID)?:""
        typeId=intent.getStringExtra(ShareActivity.TYPE_ID)?:""

        viewModel.patientId = patientId
        viewModel.typeId = typeId

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

        viewModel.urls.observe(this, Observer {
            adapter.submitList(it)

        })


        viewModel.clickShare.observe(this, Observer {
            share()
        })

        viewModel.selectUrl.observe(this, Observer {
            loadImage(it)
        })




    }

    fun loadImage(url:String){
        val handle = object : Handler() {
            override fun handleMessage(msg: Message?) {
                msg.let {
                    path =  msg?.obj as String
                }
            }
        }

        /**
        - 异步线程
         */
        Thread(object : Runnable{
            override fun run() {
                val msg = Message.obtain()

                val future = Glide.with(this@ShareActivity)
                    .load(url)
                    .downloadOnly(500, 500)
                try {
                    val cacheFile = future.get()
                    msg.obj = cacheFile.getAbsolutePath()
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                } catch (e: ExecutionException) {
                    e.printStackTrace()
                }
                //返回主线程
                handle.sendMessage(msg)
            }
        }).start()

    }


    fun share(){
        if(JShareInterface.isSupportAuthorize(Wechat.Name) ){ // &&(showVitalImage.get()||showDiagnosisImage.get()||showEvaluationImage.get())

            val params = ShareParams()
            params.imagePath = path
//            params.setImagePath("/storage/emulated/0/PictureSelector/CameraImage/PictureSelector_20190226_085108.JPEG")
            params.shareType = Platform.SHARE_IMAGE
            JShareInterface.share(Wechat.Name, params,shareListener )

        }
    }




    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_share,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        return  when(item?.itemId){
            R.id.submit->{
                viewModel.click()
                true
            }
            else->super.onOptionsItemSelected(item)
        }
    }


}
