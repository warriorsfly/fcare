package com.wxsoft.fcare.ui.details.informedconsent.addinformed

import android.Manifest
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Point
import android.graphics.Rect
import android.graphics.RectF
import android.os.Bundle
import android.os.SystemClock
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.jzxiang.pickerview.TimePickerDialog
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.wxsoft.fcare.BuildConfig
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.utils.DateTimeUtils
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.databinding.ActivityAddInformedBinding
import com.wxsoft.fcare.databinding.ItemDialogImageBinding
import com.wxsoft.fcare.di.GlideApp
import com.wxsoft.fcare.ui.BaseTimingActivity
import com.wxsoft.fcare.ui.PhotoEventAction
import com.wxsoft.fcare.ui.common.PictureAdapter
import kotlinx.android.synthetic.main.activity_add_informed.*
import kotlinx.android.synthetic.main.activity_patient_profile.*
import kotlinx.android.synthetic.main.layout_new_title.*
import java.io.File
import java.util.*
import javax.inject.Inject

class AddInformedActivity : BaseTimingActivity() ,PhotoEventAction {
    override fun localSelected() {
        checkPhotoTaking()
    }

    override fun enlargeRemote(imageView: View, url: String) {
        zoomImageFromThumb(imageView,enlarged,url)
    }

    override fun deleteRemote(url: String) {
        val binding= ItemDialogImageBinding.inflate(layoutInflater).apply {
            lifecycleOwner=this@AddInformedActivity
            imageUrl=url
        }
        AlertDialog.Builder(this,R.style.Theme_FCare_Dialog)
            .setView(binding.root)
            .setMessage("确定删除吗？")
            .setPositiveButton("是") { _, _ ->
                //                viewModel.deleteImage(url)
            }
            .setNegativeButton("否") { _, _ ->
            }.show()

    }


//    private var recorder:? =null

    override fun onDateSet(timePickerView: TimePickerDialog?, millseconds: Long) {
        (findViewById<TextView>(selectedId))?.text= DateTimeUtils.formatter.format(millseconds)
        when(selectedId){
            R.id.start_informed_time -> viewModel.talk.value?.startTime = DateTimeUtils.formatter.format(millseconds)
            R.id.end_informed_time -> viewModel.talk.value?.endTime = DateTimeUtils.formatter.format(millseconds)
        }
        viewModel.talk.value?.judgeTime()
    }

    private fun showDatePicker(v: View?){
        (v as? TextView)?.let {
            selectedId=it.id
            val currentTime= it.text.toString().let { txt->
                if(txt.isEmpty()) 0L else DateTimeUtils.formatter.parse(txt).time
            }

            dialog = createDialog(currentTime)
            dialog?.show(supportFragmentManager, "all")
        }
    }

    private var selectedId=0



    //*************************************************************************************
    private lateinit var patientId:String
    private lateinit var titleName:String
    private lateinit var informedConten:String
    private lateinit var informedContenId:String
    private lateinit var comeFrom:String
    private lateinit var talkId:String

    companion object {
        const val PATIENT_ID = "PATIENT_ID"
        const val TITLE_NAME = "TITLE_NAME"
        const val TITLE_CONTENT = "TITLE_CONTENT"
        const val INFORMED_ID = "INFORMED_ID"
        const val COME_FROM= "COME_FROM"
        const val TALK_ID= "TALK_ID"
    }

    private var mCurrentAnimator: Animator? = null
    private var mShortAnimationDuration: Int = 0

    private val photoAction: EventAction by lazy {
        EventAction()
    }

    private lateinit var viewModel: AddInformedConsentViewModel
    @Inject
    lateinit var factory: ViewModelFactory

    lateinit var binding: ActivityAddInformedBinding

    private lateinit var adapter:PictureAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(factory)
        binding = DataBindingUtil.setContentView<ActivityAddInformedBinding>(this, R.layout.activity_add_informed)
            .apply {
                startInformedTime.setOnClickListener{
                    showDatePicker(findViewById(R.id.start_informed_time))
                }
                endInformedTime.setOnClickListener{
                    showDatePicker(findViewById(R.id.end_informed_time))
                }

                lifecycleOwner = this@AddInformedActivity
            }
        setSupportActionBar(toolbar)

        patientId=intent.getStringExtra(AddInformedActivity.PATIENT_ID)?:""
        titleName=intent.getStringExtra(AddInformedActivity.TITLE_NAME)?:""
        title = titleName
        informedConten=intent.getStringExtra(AddInformedActivity.TITLE_CONTENT)?:""
        informedContenId=intent.getStringExtra(AddInformedActivity.INFORMED_ID)?:""
        comeFrom=intent.getStringExtra(AddInformedActivity.COME_FROM)?:""
        talkId=intent.getStringExtra(AddInformedActivity.TALK_ID)?:""


        viewModel.informedContenId = informedContenId
        viewModel.informedname = titleName

        viewModel.patientsId = patientId
        viewModel.talkId = talkId
        binding.viewModel = viewModel

        binding.informedContent.text = informedConten




//        back.setOnClickListener { onBackPressed() }



        viewModel.talkResultId.observe(this, Observer {})

        viewModel.loadTalk()

        viewModel.backToLast.observe(this, Observer {
            if (comeFrom == "THROMBOLYSIS"){
                Intent().let { intent->
                    intent.putExtra("informedConsentId",viewModel.talkResultId.value)
                    intent.putExtra("startTime",viewModel.talk.value?.startTime)
                    intent.putExtra("endTime",viewModel.talk.value?.endTime)
                    intent.putExtra("allTime",viewModel.talk.value?.allTime)
                    intent.putExtra("typename",viewModel.informedConsent.value?.informedTypeName)
                    setResult(RESULT_OK, intent)
                    finish()
                }
            }else{
                Intent().let { intent->
                    setResult(RESULT_OK, intent)
                    finish()
                }
            }

        })

        viewModel.showVoiceTime.observe(this, Observer {  })


        adapter= PictureAdapter(this,10,this)
        adapter.locals= emptyList()
        informed_attachments.adapter=adapter

        viewModel.talk.observe(this, Observer {
            if (it != null){
                adapter.remotes = it.attachments.map { it.httpUrl }
                if (!it.informedConsentName.isNullOrEmpty()) title = it.informedConsentName
            }
        })

        viewModel.clickStr.observe(this, Observer {
            when(it){
                "startVoice" ->{
                    binding.voiceTime.setBase(SystemClock.elapsedRealtime())
                    binding.voiceTime.start()
                    if (viewModel.talk.value?.startTime.isNullOrEmpty())
                    viewModel.talk.value?.startTime = getCurrentTime()
                }
                "endVoice" ->{
                    binding.voiceTime.stop()
                    viewModel.talk.value?.endTime = getCurrentTime()
                    viewModel.talk.value?.judgeTime()
                }
            }
        })


    }


    private fun checkPhotoTaking(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                CAMERA_PERMISSION_REQUEST
            )

        }else{
            dispatchTakePictureIntent(adapter.locals.map { it.first },10-adapter.remotes.size)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            CAMERA_PERMISSION_REQUEST->{
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                    dispatchTakePictureIntent(adapter.locals.map { it.first },10-adapter.remotes.size)
                }
            }

            AUDIO_RECRD_PERMISSION_REQUEST->{
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED){

                }
            }
        }
    }

    inner class EventAction : PhotoEventAction {
        override fun deleteRemote(url: String) {

        }

        override fun localSelected() {
            checkPhotoTaking()
        }

        override fun enlargeRemote(imageView: View, url: String) {
            zoomImageFromThumb(imageView,enlarged,url)
        }


    }

    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if(resultCode== Activity.RESULT_OK) {
            when (requestCode) {

                PictureConfig.CHOOSE_REQUEST->{
                    viewModel.bitmaps.clear()
                    adapter.locals= PictureSelector.obtainMultipleResult(data)?.map { localmedia->

                        viewModel.bitmaps.add(localmedia.path)

                        return@map Pair(localmedia, FileProvider.getUriForFile(
                            this,
                            BuildConfig.APPLICATION_ID + ".fileProvider",
                            File(localmedia.path)
                        ))
                    }?: emptyList()

                }
            }
        }

    }

    private fun zoomImageFromThumb(thumbView: View, imageView: ImageView, imageResId: String) {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        mCurrentAnimator?.cancel()

        GlideApp.with(this).load(imageResId).error(R.drawable.img_electrocardiogram).into(imageView)//enlarged.setImageResource(imageResId)

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
        findViewById<View>(R.id.container)
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


    private fun getCurrentTime(): String {
        val calendar = Calendar.getInstance()
        //年
        val year = calendar.get(Calendar.YEAR)
        //月
        val month = frontCompWithZore(calendar.get(Calendar.MONTH) + 1, 2)
        //日
        val day = frontCompWithZore(calendar.get(Calendar.DAY_OF_MONTH), 2)
        //获取系统时间
        //小时
        val hour = frontCompWithZore(calendar.get(Calendar.HOUR_OF_DAY), 2)
        //分钟
        val minute = frontCompWithZore(calendar.get(Calendar.MINUTE), 2)
        //秒
        val second = frontCompWithZore(calendar.get(Calendar.SECOND), 2)

        return "$year-$month-$day $hour:$minute:$second"
    }

    /**
    　　* 将元数据前补零，补后的总长度为指定的长度，以字符串的形式返回
    　　* @param sourceDate
    　　* @param formatLength
    　　* @return 重组后的数据
    　　*/
    private fun frontCompWithZore(sourceDate: Int, formatLength: Int): String {
        /*
    　　      * 0 指前面补充零
    　　      * formatLength 字符总长度为 formatLength
    　　      * d 代表为正数。
    　　      */
        return String.format("%0" + formatLength + "d", sourceDate)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_subject,menu)
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



