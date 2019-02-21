package com.wxsoft.fcare.ui.details.informedconsent.addinformed

import android.Manifest
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Point
import android.graphics.Rect
import android.graphics.RectF
import android.media.AudioFormat
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.SystemClock
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import cafe.adriel.androidaudioconverter.AndroidAudioConverter
import cafe.adriel.androidaudioconverter.callback.IConvertCallback
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.wxsoft.fcare.BuildConfig
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.databinding.ActivityAddInformedBinding
import com.wxsoft.fcare.di.GlideApp
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.PhotoEventAction
import com.wxsoft.fcare.ui.common.PictureAdapter
import kotlinx.android.synthetic.main.activity_add_informed.*
import kotlinx.android.synthetic.main.activity_patient_profile.*
import kotlinx.android.synthetic.main.layout_common_title.*
import omrecorder.AudioRecordConfig
import omrecorder.PullTransport
import omrecorder.PullableSource
import java.io.File
import java.util.*
import javax.inject.Inject

class AddInformedActivity : BaseActivity() , View.OnClickListener,IConvertCallback {
    override fun onSuccess(p0: File?) {
        viewModel.voicePath = p0!!.absolutePath
//                    onRecordFinishListener?.invoke(p0!!)
        dismiss()

    }
    override fun onFailure(p0: Exception?) {
        toast("录音组件异常")
        dismiss()
    }

    private val STATE_RECORD_INIT = 1           // 初始状态
    private val STATE_RECORD_RECORDING = 2      // 正在录音
    private val STATE_RECORD_PLAY = 3           // 正在播放录音
    private val STATE_RECORD_PAUSE = 4          // 暂停录音

    private val MAX_RECORD_TIME = 1800000

    private var recorder : CustomRecorder? = null
    private val mediaPlayer by lazy { MediaPlayer() }
    private var recordState = STATE_RECORD_INIT
    private var recordTime = 0L  //录制时间，单位为ms

    //录音计时器
    private var recordTimer: CountDownTimer? = null

    //录音播放计时器
    private var recordPlayTimer: CountDownTimer? = null

//*************************************************************************************
    private lateinit var patientId:String
    private lateinit var titleName:String
    private lateinit var informedConten:String
    private lateinit var informedContenId:String
    private lateinit var comeFrom:String

    companion object {
        const val PATIENT_ID = "PATIENT_ID"
        const val TITLE_NAME = "TITLE_NAME"
        const val TITLE_CONTENT = "TITLE_CONTENT"
        const val INFORMED_ID = "INFORMED_ID"
        const val COME_FROM= "COME_FROM"
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
                lifecycleOwner = this@AddInformedActivity
            }

//        initConvertor()

        patientId=intent.getStringExtra(AddInformedActivity.PATIENT_ID)?:""
        titleName=intent.getStringExtra(AddInformedActivity.TITLE_NAME)?:""
        informedConten=intent.getStringExtra(AddInformedActivity.TITLE_CONTENT)?:""
        informedContenId=intent.getStringExtra(AddInformedActivity.INFORMED_ID)?:""
        comeFrom=intent.getStringExtra(AddInformedActivity.COME_FROM)?:""


        viewModel.informedContenId = informedContenId
        viewModel.informedname = titleName
        viewModel.titleName = titleName
        viewModel.patientsId = patientId
        binding.viewModel = viewModel

        binding.informedContent.text = informedConten


        back.setOnClickListener { onBackPressed() }


        viewModel.talk.observe(this, Observer {})
        viewModel.talkResultId.observe(this, Observer {})

        viewModel.backToLast.observe(this, Observer {
            if (comeFrom == "THROMBOLYSIS"){
                Intent().let { intent->
                    intent.putExtra("informedConsentId",viewModel.talkResultId.value)
                    intent.putExtra("startTime",viewModel.talk.value?.startTime)
                    intent.putExtra("endTime",viewModel.talk.value?.endTime)
                    setResult(RESULT_OK, intent)
                    finish()
                }
            }else{
                finish()
            }

        })

        viewModel.showVoiceTime.observe(this, Observer {  })


        adapter= PictureAdapter(this,10)
        adapter.setActionListener(photoAction)
        adapter.locals= emptyList()
        informed_attachments.adapter=adapter


        viewModel.voiceStart.observe(this,Observer {
            if (it == true){
                checkAudioRecoder()

            }else{
                pauseRecording()
                binding.voiceTime.stop()
                viewModel.talk.value?.endTime = getCurrentTime()
                viewModel.initShowVoiceTime.value = true
                binding.timeVoice.text = binding.voiceTime.text
            }
        })




    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.voice_record -> {
                playRecoding()
            }

        }
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
                    startRecording()
                }
            }
        }
    }

    inner class EventAction : PhotoEventAction {
        override fun localSelected() {
            checkPhotoTaking()
        }

        override fun enlargeRemote(imageView: View, url: String) {
            zoomImageFromThumb(imageView,enlarged,url)
        }


    }

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


    private fun pauseRecording() {
        changeState(STATE_RECORD_PAUSE)
        recordTimer?.cancel()
        recorder!!.pauseRecording()

        finishRecording()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun startRecording() {
        if (recordTime >= MAX_RECORD_TIME){
            toast("回答不能超过${MAX_RECORD_TIME / 1000}s")
            return
        }

        if (recordState == STATE_RECORD_INIT) {
            recorder = createRecorder()
            recorder!!.startRecording()
            viewModel.talk.value?.startTime = getCurrentTime()
            binding.voiceTime.base = SystemClock.elapsedRealtime()
            binding.voiceTime.isCountDown = false
            binding.voiceTime.start()
            startRecordTimer()


        }else{
            recorder!!.resumeRecording()
        }

        changeState(STATE_RECORD_RECORDING)
    }

    private fun deleteRecording(){

        changeState(STATE_RECORD_INIT)
        recordTime = 0
        recorder!!.stopRecording()
    }

    private fun playRecoding(){
        if (recordTime >= MAX_RECORD_TIME) {toast("录制时间到达上限"); return}

        if (recordState == STATE_RECORD_PAUSE) {
            mediaPlayer.setOnCompletionListener { playRecoding() }
            mediaPlayer.reset()
            mediaPlayer.setDataSource(this, Uri.fromFile(recorder!!.tempFile))
            mediaPlayer.prepare()
            mediaPlayer.start()
            startPlayRecordTimer()
            changeState(STATE_RECORD_PLAY)

//            progress_view.maxProgress = 100f
        }else{
            recordPlayTimer?.cancel()
//            progressAnim!!.cancel()
//            progress_view.currentProgress = 0f
            mediaPlayer.stop()
            changeState(STATE_RECORD_PAUSE)
        }
    }

    private fun finishRecording() {
        recorder!!.stopRecording()

        //将wav转化为mp3
        AndroidAudioConverter.with(this).setFile(recorder!!.recordFile)
            .setFormat(cafe.adriel.androidaudioconverter.model.AudioFormat.MP3)
            .setCallback(this).convert()

    }

    /**
     * 根据状态改变控件显示属性
     */
    private fun changeState(recordState: Int){
        when(recordState){
            STATE_RECORD_INIT -> {

            }
            STATE_RECORD_RECORDING -> {

            }
            STATE_RECORD_PAUSE -> {

            }
            STATE_RECORD_PLAY -> {

            }
        }
        this.recordState = recordState
    }

    private fun startRecordTimer(){
        recordTimer = object : CountDownTimer(MAX_RECORD_TIME - recordTime,100){
            override fun onTick(millisUntilFinished: Long) {
                recordTime += 100
//                tv_time.text = "${recordTime / 60000}:${formatSecondLongToString((recordTime % 60000) / 1000)}"
            }
            override fun onFinish() {
                pauseRecording()
                toast("录制时间到达上限")
            }
        }.start()
    }

    private fun startPlayRecordTimer(){
        var countDownTime = recordTime.toFloat()
        recordPlayTimer = object : CountDownTimer(recordTime,100){
            init {
//                progress_view.maxProgress = countDownTime
            }

            override fun onTick(millisUntilFinished: Long) {
                val timeLeft = millisUntilFinished / 1000
//                tv_time.text = "${timeLeft/60}:${formatSecondLongToString(timeLeft % 60)}"
//                progress_view.currentProgress = countDownTime - millisUntilFinished
            }
            override fun onFinish() {}
        }.start()
    }

    private fun createRecorder() = CustomRecorder(
        PullTransport.Default(mic(),
            PullTransport.OnAudioChunkPulledListener {}),file(),tempFile())

    private fun mic() = PullableSource.Default(
        AudioRecordConfig.Default(
            MediaRecorder.AudioSource.MIC, AudioFormat.ENCODING_PCM_16BIT,
            AudioFormat.CHANNEL_IN_MONO, 44100))

    private fun file() = File(this.externalCacheDir, "record.wav")
    private fun tempFile() = File(this.externalCacheDir, "temp.wav")

    fun toast(msg: String){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show()
    }

    private fun dismiss() {
        // 如果reocrder再daialog关闭前已经停止，则调用stopRecording会抛出IllegalStateException
        try {
            recorder?.stopRecording()
        }catch (e: IllegalStateException){}
    }

    private fun checkAudioRecoder(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)!= PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                AUDIO_RECRD_PERMISSION_REQUEST
            )

        }else{
            startRecording()
        }
    }
}



