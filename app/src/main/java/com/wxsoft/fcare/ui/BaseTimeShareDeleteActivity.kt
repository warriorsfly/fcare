package com.wxsoft.fcare.ui

import android.app.AlertDialog
import android.view.MenuItem
import cn.jiguang.share.android.api.JShareInterface
import cn.jiguang.share.android.api.PlatActionListener
import cn.jiguang.share.android.api.Platform
import cn.jiguang.share.android.api.ShareParams
import cn.jiguang.share.wechat.Wechat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.wxsoft.fcare.R
import com.wxsoft.fcare.databinding.ItemDialogImageBinding
import com.wxsoft.fcare.di.GlideApp
import com.wxsoft.fcare.ui.common.DingLikeTimePicker
import com.wxsoft.fcare.ui.common.ITimeSelected
import com.wxsoft.fcare.utils.IShareOrDelete
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File

abstract class BaseTimeShareDeleteActivity: BaseActivity(), IShareOrDelete, ITimeSelected {

    protected val listener = object : PlatActionListener {
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

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        return when(item?.itemId){
            android.R.id.home->{
                onBackPressed()
                return true
            }

            else->super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        disposable.clear()
        super.onDestroy()
    }

    override fun showImageDialog(url:String,id:String,fix:Boolean) {

        val binding= ItemDialogImageBinding.inflate(layoutInflater).apply {
            lifecycleOwner=this@BaseTimeShareDeleteActivity
            imageUrl=url
        }
        AlertDialog.Builder(this, R.style.Theme_FCare_Dialog)
            .setView(binding.root)
            .setNegativeButton("分享"){_,_->

                share(url)
            }
            .setPositiveButton("删除") { _, _ ->
                delete(
                    (if(id.isNullOrEmpty()) url else id),false
                )
            }
            .setNeutralButton(if (fix)"修复" else "取消") { dialog, _ ->
                if (fix){
                    delete(
                        (if(id.isNullOrEmpty()) url else id),true
                    )
                }
                dialog.dismiss()
            }.show()
    }

    private fun doImage(file: File){
        try {
            if(JShareInterface.isSupportAuthorize(Wechat.Name) ){
                val params = ShareParams().apply {
                    imagePath= file.path
                    shareType = Platform.SHARE_IMAGE
                }
                JShareInterface.share(Wechat.Name, params,listener )

            }

        }catch (e:Exception){
            error(e)
        }
    }

    protected abstract fun doError(throwable: Throwable)

    override fun share(url:String) {

        disposable.add(
            Single.fromCallable {
                GlideApp.with(this)
                    .asFile().load(url)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .submit().get()}
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(::doImage, ::doError))

    }


    protected var dialog: DingLikeTimePicker?=null

    protected fun createDialog(time:Long): DingLikeTimePicker {

        return DingLikeTimePicker(time,::selectTime,::clearTime)

    }

}