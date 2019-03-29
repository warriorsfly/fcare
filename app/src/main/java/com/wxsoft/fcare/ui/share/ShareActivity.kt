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
    private var path: String = ""

//    val url = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1553600258449&di=872164468d27fcd35b284f27fd0e3d05&imgtype=0&src=http%3A%2F%2Fpic1.16pic.com%2F00%2F06%2F41%2F16pic_641310_b.jpg"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(factory)

        binding = DataBindingUtil.setContentView<ActivityShareBinding>(this, R.layout.activity_share)
            .apply {
//                uri = url.toString()
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

        val handle = object : Handler() {
            override fun handleMessage(msg: Message?) {
                msg.let {
                    path =  msg?.obj as String
                }
            }
        }

        viewModel.url.observe(this, Observer {
            if (it.isNotEmpty()){
                binding.uri = it.first()
            }
        })


        viewModel.clickShare.observe(this, Observer {
            share()
        })







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
