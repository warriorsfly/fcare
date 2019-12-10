package com.wxsoft.fcare.ui.main.fragment.profile

import android.app.AlertDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import cn.jiguang.share.android.api.JShareInterface
import cn.jiguang.share.android.api.PlatActionListener
import cn.jiguang.share.android.api.Platform
import cn.jiguang.share.android.api.ShareParams
import cn.jiguang.share.wechat.Wechat
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.result.EventObserver
import com.wxsoft.fcare.core.utils.DateTimeUtils
import com.wxsoft.fcare.core.utils.activityViewModelProvider
import com.wxsoft.fcare.databinding.FragmentUserProfileBinding
import com.wxsoft.fcare.databinding.ItemDialogQrImageBinding
import com.wxsoft.fcare.ui.login.LoginActivity
import com.wxsoft.fcare.ui.sign.SignInActivity
import dagger.android.support.DaggerFragment
import java.util.*
import java.util.prefs.PreferenceChangeListener
import javax.inject.Inject
import kotlin.collections.HashMap


class UserProfileFragment : DaggerFragment() {

    private val listener = object : PlatActionListener {
        /**
         * 上传成功
         */
        override fun onComplete(p0: Platform?, p1: Int, p2: HashMap<String, Any>?) {

        }

        /**
         * 取消
         */
        override fun onCancel(p0: Platform?, p1: Int) {

        }

        /**
         * 分享错误
         */
        override fun onError(p0: Platform?, p1: Int, p2: Int, p3: Throwable?) {

        }
    }

    @Inject
    lateinit var factory: ViewModelFactory

    private lateinit var viewModel: UserProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = activityViewModelProvider(factory)
        val binding = FragmentUserProfileBinding.inflate(inflater, container, false).apply {

            viewModel=this@UserProfileFragment.viewModel
            this@UserProfileFragment.viewModel.getAllHospital()
            logout.setOnClickListener {
                viewModel?.loginOut()
                val intent = Intent(activity!!, LoginActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                }
                startActivity(intent)
                activity?.finish()
            }
            changePass.setOnClickListener {
                val dialog=PasswordFragment()
                dialog.show(childFragmentManager,"all")
            }
            sign.setOnClickListener {
                val intent = Intent(this@UserProfileFragment.activity, SignInActivity::class.java)
                this@UserProfileFragment.activity?.startActivity(intent)
            }
            changeHospital.setOnClickListener {
                if (this@UserProfileFragment.viewModel.hospitals.value!=null) showDiag()
            }
            qrCode.setOnClickListener {
                showImageDialog()
            }
            viewModel
            lifecycleOwner = this@UserProfileFragment
        }

        viewModel.hospitals.observe(this@UserProfileFragment, Observer {})
        viewModel.mesAction.observe(this, EventObserver{
            Toast.makeText(this.context,it, Toast.LENGTH_LONG).show()
        })

//        viewModel.signedDate.observe(this, Observer {
//            if(it.isNullOrEmpty() || DateTimeUtils.date_formatter.format(Calendar.getInstance().time) > it) {
//                //TODO 获取服务端签到时间，如果不是当天，弹出窗口，如果是当天，更新本地时间
//            }
//        })


        return binding.root
    }

    fun showDiag(){
        val list = viewModel.hospitals.value!!.map { it.name }.toTypedArray()
        AlertDialog.Builder(this.context!!).setItems(list) { _, i ->
            val hospital = viewModel.hospitals.value!![i]
            viewModel.selectHospital(hospital)
        }.create().show()
    }

    fun showImageDialog() {
        val binding= ItemDialogQrImageBinding.inflate(layoutInflater).apply {
            lifecycleOwner=this@UserProfileFragment
        }
        AlertDialog.Builder(this.context, R.style.Theme_FCare_Dialog)
            .setView(binding.root)
            .setNeutralButton("分享"){_,_->
                doImage()
            }
            .setNegativeButton("取消") { dialog, _ ->
                dialog.dismiss()
            }.show()
    }
    private fun doImage(){
        try {
            if(JShareInterface.isSupportAuthorize(Wechat.Name) ){
                val params = ShareParams().apply {

                   val bitmap = BitmapFactory.decodeResource(this@UserProfileFragment.context!!.resources, R.drawable.ic_qr_image)
                    imageData = bitmap
                    shareType = Platform.SHARE_IMAGE
                }
                JShareInterface.share(Wechat.Name, params,listener )

            }

        }catch (e:Exception){
            error(e)
        }
    }



}
