package com.wxsoft.fcare.ui

import android.view.MenuItem
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.disposables.CompositeDisposable


abstract class BaseActivity : DaggerAppCompatActivity() {

    protected val disposable= CompositeDisposable()
    companion object {
        const val CAMERA_PERMISSION_REQUEST=10
        const val UPGRADE_PERMISSION_REQUEST=15
        const val GIS_PERMISSION_REQUEST=17
        const val UPGRADE_PERMISSION_SETTING=16
        const val AUDIO_RECRD_PERMISSION_REQUEST=14
        const val CAMERA_PIC_REQUEST=11
        const val NEW_PATIENT_REQUEST=13
        const val COMING_WAY_DOCTOR=18
        const val COMING_WAY_TYPES=19
        const val NEW_PATIENT_ID="new_patient_id"
    }

    fun dispatchTakePictureIntent(list:List<LocalMedia>,max:Int) {


        PictureSelector.create(this)
            .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
            .maxSelectNum(max)// 最大图片选择数量 int
            .imageSpanCount(4)// 每行显示个数 int
            .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
            .previewImage(true)// 是否可预览图片 true or false
            .isCamera(true)// 是否显示拍照按钮 true or false
//            .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
            .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
            .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效

            .selectionMedia(list)// 是否传入已选图片 List<LocalMedia> list
            .previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
            .minimumCompressSize(100)// 小于100kb的图片不压缩
            .synOrAsy(true)//同步true或异步false 压缩 默认同步
            .forResult(PictureConfig.CHOOSE_REQUEST)//结果回调onActivityResult code
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

}