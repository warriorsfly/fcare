package com.wxsoft.fcare.ui.details.assistant.troponin

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
import android.os.Bundle
import android.provider.MediaStore
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
import com.jzxiang.pickerview.data.Type
import com.jzxiang.pickerview.listener.OnDateSetListener
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.wxsoft.fcare.BuildConfig
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.utils.DateTimeUtils
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.databinding.ActivityJgdbBinding
import com.wxsoft.fcare.di.GlideApp
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.PhotoEventAction
import com.wxsoft.fcare.ui.common.PictureAdapter
import com.wxsoft.fcare.ui.hardwaredata.HardwareDataActivity
import com.wxsoft.fcare.ui.patient.ProfileActivity
import com.wxsoft.fcare.utils.ActionCode
import kotlinx.android.synthetic.main.activity_jgdb.*
import kotlinx.android.synthetic.main.layout_new_title.*
import java.io.File
import javax.inject.Inject

class JGDBActivity : BaseActivity() , OnDateSetListener {

    private var dialog: TimePickerDialog?=null
    private val selectedIndex= mutableListOf<Int>()


    override fun onDateSet(timePickerView: TimePickerDialog?, millseconds: Long) {
        dialog?.onDestroy()
        dialog=null
        (findViewById<TextView>(selectedId))?.text= DateTimeUtils.formatter.format(millseconds)
        when(selectedId){
            R.id.draw_blood_time -> viewModel.lisCr.value?.samplingTime = DateTimeUtils.formatter.format(millseconds)
            R.id.draw_blood_report_time -> viewModel.lisCr.value?.reportTime = DateTimeUtils.formatter.format(millseconds)
        }
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



    private lateinit var patientId:String
    private lateinit var recordId:String

    companion object {
        const val PATIENT_ID = "PATIENT_ID"
        const val RECORD_ID = "RECORD_ID"
        const val HARDWARE_DATA = 10
    }

    private lateinit var viewModel: TroponinViewModel
    @Inject
    lateinit var factory: ViewModelFactory

    lateinit var binding: ActivityJgdbBinding

    private var mCurrentAnimator: Animator? = null
    private var mShortAnimationDuration: Int = 0
    private lateinit var adapter: PictureAdapter

    private var photoAction: EventAction? =EventAction()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = viewModelProvider(factory)
        binding = DataBindingUtil.setContentView<ActivityJgdbBinding>(this, R.layout.activity_jgdb)
            .apply {
                viewModel = this@JGDBActivity.viewModel
                line0.setOnClickListener {
                    toGetHardwareData()
                }
                lifecycleOwner = this@JGDBActivity
            }
        patientId=intent.getStringExtra(PATIENT_ID)?:""
        recordId=intent.getStringExtra(RECORD_ID)?:""
        viewModel.patientId = patientId
        viewModel.getCrById(patientId)
        viewModel.lisCr.observe(this, Observer {
            if (it != null){
                adapter.remotes = it.attachments.map { it.httpUrl }
            }
        })
        setSupportActionBar(toolbar)
        title="肌钙蛋白"
//        viewModel.loadTroponin()

        viewModel.clickEdit.observe(this, Observer {
            when(it){
                "1"-> showDatePicker(binding.drawBloodTime)
                "2"-> showDatePicker(binding.drawBloodReportTime)
                "success" -> {
                    Intent().let { intent->
                        setResult(RESULT_OK, intent)
                        finish()
                    }
                }
            }
        })

        adapter= PictureAdapter(this,10)
        adapter.setActionListener(photoAction!!)
        adapter.locals= emptyList()
        jgdb_photo_items_rv.adapter = adapter

    }

    fun toGetHardwareData(){

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_subject,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        return  when(item?.itemId){
            R.id.submit->{
                viewModel.submit()
                true
            }
            else->super.onOptionsItemSelected(item)
        }
    }

    private fun createDialog(time:Long): TimePickerDialog {

        return TimePickerDialog.Builder()
            .setCallBack(this)
            .setCancelStringId("取消")
            .setSureStringId("确定")
            .setTitleStringId("选择时间")
            .setYearText("")
            .setMonthText("")
            .setDayText("")
            .setHourText("")
            .setMinuteText("")
            .setCyclic(false)
            .setCurrentMillseconds(if(time==0L)System.currentTimeMillis() else time)
            .setType(Type.ALL)
            .setWheelItemTextSize(16)
            .build()
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
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {

                PictureConfig.CHOOSE_REQUEST -> {
                    viewModel.bitmaps.clear()
                    adapter.locals = PictureSelector.obtainMultipleResult(data)?.map { localmedia ->
                        viewModel.bitmaps.add(localmedia.path)
                        return@map Pair(
                            localmedia, FileProvider.getUriForFile(
                                this,
                                BuildConfig.APPLICATION_ID + ".fileProvider",
                                File(localmedia.path)
                            )
                        )
                    } ?: emptyList()

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


    override fun onDestroy() {
        super.onDestroy()
        photoAction=null
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


    private fun showPhotoTaking(){
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST)
    }



}
