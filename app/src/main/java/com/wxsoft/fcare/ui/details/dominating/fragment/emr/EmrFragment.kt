package com.wxsoft.fcare.ui.details.dominating.fragment.emr

import android.Manifest
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.app.AlertDialog
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Point
import android.graphics.Rect
import android.graphics.RectF
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.wxsoft.fcare.BuildConfig
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.data.entity.ElectroCardiogram
import com.wxsoft.fcare.core.di.GlideApp
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.result.EventObserver
import com.wxsoft.fcare.data.dictionary.ActionRes
import com.wxsoft.fcare.databinding.FragmentEmrBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.CommitEventAction
import com.wxsoft.fcare.ui.EventActions
import com.wxsoft.fcare.ui.PhotoEventAction
import com.wxsoft.fcare.ui.details.checkbody.CheckBodyActivity
import com.wxsoft.fcare.ui.details.diagnose.DiagnoseActivity
import com.wxsoft.fcare.ui.details.informedconsent.InformedConsentActivity
import com.wxsoft.fcare.ui.details.measures.MeasuresActivity
import com.wxsoft.fcare.ui.details.medicalhistory.MedicalHistoryActivity
import com.wxsoft.fcare.ui.details.pharmacy.PharmacyActivity
import com.wxsoft.fcare.ui.details.vitalsigns.VitalSignsActivity
import com.wxsoft.fcare.ui.patient.ProfileActivity
import com.wxsoft.fcare.ui.rating.RatingActivity
import com.wxsoft.fcare.utils.lazyFast
import com.wxsoft.fcare.utils.viewModelProvider
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_emr.*
import java.io.File
import java.lang.ref.WeakReference
import javax.inject.Inject

class EmrFragment : DaggerFragment() {

    companion object {

        private const val ARG_PATIENT = "arg.patient"
        private const val ARG_PREHOS = "arg.prehos"
        @JvmStatic
        fun newInstance( patientId:String,preHos:Boolean=true): EmrFragment {

            val args = Bundle().apply {
                putString(ARG_PATIENT,patientId)
                putBoolean(ARG_PREHOS,preHos)
            }
            return EmrFragment().apply { arguments = args }

        }

    }
    private val photoAction:PhotoAction by lazy {
        PhotoAction()
    }

    private var commitAction:CommitAction? =CommitAction()

    private val toast:Toast by lazy {
        Toast.makeText(context,"",Toast.LENGTH_SHORT)
    }


    @Inject
    lateinit var factory: ViewModelFactory

    lateinit var binding: FragmentEmrBinding

    private lateinit var viewModel: EmrViewModel

    private lateinit var adapter: EmrAdapter

    private var mCurrentAnimator: Animator? = null
    private var mShortAnimationDuration: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = viewModelProvider(factory)
        binding= FragmentEmrBinding.inflate(inflater, container, false).apply {
            setLifecycleOwner(this@EmrFragment)

            viewModel=this@EmrFragment.viewModel
        }
        adapter= EmrAdapter(this)
        adapter.setActionListener(EventAction(WeakReference(activity!!),patientId))
        adapter.setCommitEventActionListener(commitAction!!)
        adapter.pictureAdapter.setActionListener(photoAction)
        binding.list.adapter=adapter

        viewModel.patientId=patientId

        viewModel.preHos=preHos

        viewModel.emrs.observe(this, Observer {
            //    binding.list?.clearDecorations()
            adapter.items=it ?: emptyList()

        })

        viewModel.mesAction.observe(this,EventObserver{
            toast.apply {
                setText(it)
            }.show()
        })
        viewModel.emrItemLoaded.observe(this,EventObserver{
            adapter.notifyItemChanged(it.first)
            when(it.second){
                ActionRes.ActionType.心电图->{
                    viewModel.bitmaps.clear()
                    adapter.pictureAdapter.locals= emptyList()
                }
            }
        })
        return binding.root

    }

    class EventAction constructor(private val context: WeakReference<Context>,private val patientId:String):EventActions{
        override fun onOpen(id: String) {
            when(id){
                ActionRes.ActionType.患者信息录入->{
                    var intent = Intent(context.get(), ProfileActivity::class.java).apply {
                        putExtra(ProfileActivity.PATIENT_ID, patientId)
                    }
                    context.get()?.startActivity(intent)
                }

                ActionRes.ActionType.GRACE->{
                    var intent = Intent(context.get(), RatingActivity::class.java)
                        .apply {
                            putExtra(ProfileActivity.PATIENT_ID, patientId)
                        }
                    context.get()?.startActivity(intent)
                }
                ActionRes.ActionType.生命体征->{
                    var intent = Intent(context.get(), VitalSignsActivity::class.java).apply {
                        putExtra(VitalSignsActivity.PATIENT_ID, patientId)
                    }
                    context.get()?.startActivity(intent)
                }
                ActionRes.ActionType.PhysicalExamination->{
                    var intent = Intent(context.get(), CheckBodyActivity::class.java).apply {
                        putExtra(CheckBodyActivity.PATIENT_ID, patientId)
                    }
                    context.get()?.startActivity(intent)
                }
                ActionRes.ActionType.IllnessHistory->{
                    var intent = Intent(context.get(), MedicalHistoryActivity::class.java).apply {
                        putExtra(MedicalHistoryActivity.PATIENT_ID, patientId)
                    }
                    context.get()?.startActivity(intent)
                }
                ActionRes.ActionType.DispostionMeasures->{
                    var intent = Intent(context.get(), MeasuresActivity::class.java).apply {
                        putExtra(MeasuresActivity.PATIENT_ID, patientId)
                    }
                    context.get()?.startActivity(intent)
                }
                ActionRes.ActionType.给药 ->{
                    var intent = Intent(context.get(), PharmacyActivity::class.java).apply {
                        putExtra(PharmacyActivity.PATIENT_ID, patientId)
                    }
                    context.get()?.startActivity(intent)
                }
                ActionRes.ActionType.知情同意书 ->{
                    var intent = Intent(context.get(), InformedConsentActivity::class.java).apply {
                        putExtra(InformedConsentActivity.PATIENT_ID, patientId)
                    }
                    context.get()?.startActivity(intent)
                }
                ActionRes.ActionType.诊断 ->{
                    var intent = Intent(context.get(), DiagnoseActivity::class.java).apply {
                        putExtra(DiagnoseActivity.PATIENT_ID, patientId)
                    }
                    context.get()?.startActivity(intent)
                }

            }
        }

    }

    private val patientId: String by lazyFast {
        val args = arguments ?: throw IllegalStateException("Missing arguments!")
        args.getString(ARG_PATIENT)
    }

    private val preHos: Boolean by lazyFast {
        val args = arguments ?: throw IllegalStateException("Missing arguments!")
        args.getBoolean(ARG_PREHOS,true)
    }



    private fun checkPhotoTaking(){
        if (ContextCompat.checkSelfPermission(activity!!, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(activity!!, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(activity!!,
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                BaseActivity.CAMERA_PERMISSION_REQUEST
            )

        }else{
            dispatchTakePictureIntent(adapter.pictureAdapter.locals.map { it.first },2-adapter.pictureAdapter.remotes.size)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            BaseActivity.CAMERA_PERMISSION_REQUEST ->{
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                    dispatchTakePictureIntent(adapter.pictureAdapter  .locals.map { it.first },2-adapter.pictureAdapter.remotes.size)
                }
            }
        }
    }

    inner class PhotoAction() : PhotoEventAction {
        override fun localSelected() {
            checkPhotoTaking()
        }

        override fun enlargeRemote(root:View,url: String) {
            zoomImageFromThumb(root,enlarged,url)
        }
    }

    inner class CommitAction() : CommitEventAction {
        override fun commit(any: Any,type:Int) {

            when(any){
                is ElectroCardiogram->{
                    when(type){
                        0->viewModel.saveEcg()
                        1-> {
                            val editView=EditText(activity).apply {
                                layoutParams=LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
                            }
                            val dialog = AlertDialog.Builder(activity, R.style.Theme_FCare_Dialog_Text)
                                .setView(editView)
                                .setMessage("心电图判读")
                                .setPositiveButton("确定") { _, _ ->
                                    if(editView.text.toString().isNotEmpty())
                                        viewModel.diagnose(editView.text.toString())
                                }
                                .setNegativeButton("取消") { _, _ -> }

                            dialog.show()
                        }
                    }

                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()

        commitAction=null
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== Activity.RESULT_OK) {
            when (requestCode) {

                PictureConfig.CHOOSE_REQUEST->{
                    viewModel.bitmaps.clear()
                    adapter.pictureAdapter.locals= PictureSelector.obtainMultipleResult(data)?.map { localmedia->

                        viewModel.bitmaps.add(localmedia.path)

                        return@map Pair(localmedia, FileProvider.getUriForFile(
                            activity!!,
                            BuildConfig.APPLICATION_ID + ".fileProvider",
                            File(localmedia.path)
                        ))
                    }?: emptyList()
                    (viewModel.emrs.value?.first {
                            emr->emr.code==ActionRes.ActionType.心电图
                    }?.result as? ElectroCardiogram)?.savable=adapter.pictureAdapter.locals.isNotEmpty()
                }
            }
        }

    }

    private fun zoomImageFromThumb(thumbView: View, imageView: ImageView, imageResId: String) {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        mCurrentAnimator?.cancel()

        GlideApp.with(this).load(imageResId).error(R.mipmap.img_electrocardiogram).into(imageView)//enlarged.setImageResource(imageResId)

        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        val startBoundsInt = Rect()
        val finalBoundsInt = Rect()
        val globalOffset = Point()

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        thumbView.getGlobalVisibleRect(startBoundsInt)
        list
            .getGlobalVisibleRect(finalBoundsInt, globalOffset)
        startBoundsInt.offset(-globalOffset.x, -globalOffset.y)
        finalBoundsInt.offset(-globalOffset.x, -globalOffset.y)

        val startBounds = RectF(startBoundsInt)
        val finalBounds = RectF(finalBoundsInt)

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        val startScale: Float
        if ((finalBounds.width() / finalBounds.height() > startBounds.width() / startBounds.height())) {
            // Extend start bounds horizontally
            startScale = startBounds.height() / finalBounds.height()
            val startWidth: Float = startScale * finalBounds.width()
            val deltaWidth: Float = (startWidth - startBounds.width()) / 2
            startBounds.left -= deltaWidth.toInt()
            startBounds.right += deltaWidth.toInt()
        } else {
            // Extend start bounds vertically
            startScale = startBounds.width() / finalBounds.width()
            val startHeight: Float = startScale * finalBounds.height()
            val deltaHeight: Float = (startHeight - startBounds.height()) / 2f
            startBounds.top -= deltaHeight.toInt()
            startBounds.bottom += deltaHeight.toInt()
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        thumbView.alpha = 0f
        imageView.visibility = View.VISIBLE

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        imageView.pivotX = 0f
        imageView.pivotY = 0f

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        mCurrentAnimator = AnimatorSet().apply {
            play(
                ObjectAnimator.ofFloat(
                    imageView,
                    View.X,
                    startBounds.left,
                    finalBounds.left)
            ).apply {
                with(ObjectAnimator.ofFloat(imageView, View.Y, startBounds.top, finalBounds.top))
                with(ObjectAnimator.ofFloat(imageView, View.SCALE_X, startScale, 1f))
                with(ObjectAnimator.ofFloat(imageView, View.SCALE_Y, startScale, 1f))
            }
            duration = mShortAnimationDuration.toLong()
            interpolator = DecelerateInterpolator()
            addListener(object : AnimatorListenerAdapter() {

                override fun onAnimationEnd(animation: Animator) {
                    mCurrentAnimator = null
                }

                override fun onAnimationCancel(animation: Animator) {
                    mCurrentAnimator = null
                }
            })
            start()
        }

        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        imageView.setOnClickListener {
            mCurrentAnimator?.cancel()

            // Animate the four positioning/sizing properties in parallel,
            // back to their original values.
            mCurrentAnimator = AnimatorSet().apply {
                play(ObjectAnimator.ofFloat(imageView, View.X, startBounds.left)).apply {
                    with(ObjectAnimator.ofFloat(imageView, View.Y, startBounds.top))
                    with(ObjectAnimator.ofFloat(imageView, View.SCALE_X, startScale))
                    with(ObjectAnimator.ofFloat(imageView, View.SCALE_Y, startScale))
                }
                duration = mShortAnimationDuration.toLong()
                interpolator = DecelerateInterpolator()
                addListener(object : AnimatorListenerAdapter() {

                    override fun onAnimationEnd(animation: Animator) {
                        thumbView.alpha = 1f
                        imageView.visibility = View.GONE
                        mCurrentAnimator = null
                    }

                    override fun onAnimationCancel(animation: Animator) {
                        thumbView.alpha = 1f
                        imageView.visibility = View.GONE
                        mCurrentAnimator = null
                    }
                })
                start()
            }
        }
    }


    fun dispatchTakePictureIntent(list:List<LocalMedia>, max:Int) {


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
            .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }

}
