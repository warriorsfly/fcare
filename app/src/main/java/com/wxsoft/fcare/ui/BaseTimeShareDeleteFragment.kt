package com.wxsoft.fcare.ui

import android.app.AlertDialog
import androidx.fragment.app.DialogFragment
import cn.jiguang.share.android.api.JShareInterface
import cn.jiguang.share.android.api.PlatActionListener
import cn.jiguang.share.android.api.Platform
import cn.jiguang.share.android.api.ShareParams
import cn.jiguang.share.wechat.Wechat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.jzxiang.pickerview.TimePickerDialog
import com.jzxiang.pickerview.data.Type
import com.jzxiang.pickerview.listener.OnDateSetListener
import com.wxsoft.fcare.R
import com.wxsoft.fcare.databinding.ItemDialogImageBinding
import com.wxsoft.fcare.di.GlideApp
import com.wxsoft.fcare.utils.IShareOrDelete
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File

abstract class BaseTimeShareDeleteFragment: BaseFragment(), IShareOrDelete , OnDateSetListener {

    protected var dialog: TimePickerDialog?=null

    protected fun createDialog(time:Long): TimePickerDialog {

        return TimePickerDialog.Builder()
            .setThemeColor(resources.getColor(R.color.colorPrimary))
            .setCallBack(this)
            .setCancelStringId("取消")
            .setSureStringId("确定")
            .setTitleStringId("选择时间")
            .setYearText("")
            .setMonthText("")
            .setDayText("")
            .setHourText("")
            .setMinuteText("")
            .setCyclic(true)
            .setCurrentMillseconds(if(time==0L)System.currentTimeMillis() else time)
            .setType(Type.ALL)
            .setWheelItemTextSize(12)
            .build().apply {  setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_FCare_Dialog)}
    }

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

    override fun showImageDialog(url:String,id:String) {

        val binding= ItemDialogImageBinding.inflate(layoutInflater).apply {
            lifecycleOwner=this@BaseTimeShareDeleteFragment
            imageUrl=url
        }
        AlertDialog.Builder(context, R.style.Theme_FCare_Dialog)
            .setView(binding.root)
            .setNeutralButton("分享"){_,_->

                share(url)
            }
            .setPositiveButton("删除") { _, _ ->
                delete(
                    if(id.isNullOrEmpty()) url else id
                )
            }
            .setNegativeButton("取消") { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    override fun onDestroy() {
        disposable.clear()
        super.onDestroy()
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


}