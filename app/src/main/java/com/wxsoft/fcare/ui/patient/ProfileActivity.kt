package com.wxsoft.fcare.ui.patient

import android.Manifest
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Point
import android.graphics.Rect
import android.graphics.RectF
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationListener
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.wxsoft.fcare.BuildConfig
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.result.EventObserver
import com.wxsoft.fcare.core.result.Resource
import com.wxsoft.fcare.core.utils.DateTimeUtils
import com.wxsoft.fcare.core.utils.lazyFast
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.databinding.ActivityPatientProfileBinding
import com.wxsoft.fcare.databinding.ItemDialogImageBinding
import com.wxsoft.fcare.di.GlideApp
import com.wxsoft.fcare.ui.BaseTimingActivity
import com.wxsoft.fcare.ui.PhotoEventAction
import com.wxsoft.fcare.ui.common.PictureAdapter
import com.wxsoft.fcare.ui.details.vitalsigns.records.VitalSignsRecordActivity
import com.wxsoft.fcare.ui.share.ShareActivity
import kotlinx.android.synthetic.main.activity_patient_profile.*
import java.io.File
import javax.inject.Inject
import javax.inject.Named


class ProfileActivity : BaseTimingActivity(), View.OnClickListener,PhotoEventAction ,AMapLocationListener{
    override fun localSelected() {
        checkPhotoTaking()
    }

    override fun onLocationChanged(p0: AMapLocation?) {

        p0?.let {
            locat.setText(it.address)
        }
    }

    override fun enlargeRemote(imageView: View, url: String) {
        zoomImageFromThumb(imageView,enlarged,url)
    }

    override fun deleteRemote(url: String) {
        val binding= ItemDialogImageBinding.inflate(layoutInflater).apply {
            lifecycleOwner=this@ProfileActivity
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

    @field:[Inject Named("single")]
    lateinit var client: AMapLocationClient
    private var selectedId=0
    override fun onClick(v: View?) {
        (v as? TextView)?.let {
            selectedId = it.id
            val currentTime = it.text.toString().let { text ->
                if (text.isEmpty()) 0L else DateTimeUtils.formatter.parse("$text:00").time
            }

            dialog = createDialog(currentTime)
            dialog?.show(supportFragmentManager, "all")
        }

    }

    override fun onDateSet(timePickerView: com.jzxiang.pickerview.TimePickerDialog?, millseconds: Long) {

        dialog?.onDestroy()
        dialog=null
        (findViewById<TextView>(selectedId))?.text= DateTimeUtils.formatter.format(millseconds)
    }

    private val toast:Toast by  lazy {
        Toast.makeText(this,"",Toast.LENGTH_SHORT)
    }

    companion object {
        const val TASK_ID = "TASK_ID"
        const val PATIENT_ID = "PATIENT_ID"
        const val IS_PRE = "IS_PRE"
    }

    private var mCurrentAnimator: Animator? = null
    private var mShortAnimationDuration: Int = 0

    private val patientId: String by lazyFast {
        intent?.getStringExtra(PATIENT_ID)?:""
    }

    private val taskId: String by lazyFast {
         intent ?.getStringExtra(TASK_ID)?:""

    }

    private var photoAction:EventAction?=EventAction()

    @Inject
    lateinit var factory: ViewModelFactory

    private lateinit var adapter:PictureAdapter
    private lateinit var viewModel:ProfileViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityPatientProfileBinding>(
            this,
            R.layout.activity_patient_profile
        ).apply{

            lifecycleOwner = this@ProfileActivity

        }
        setSupportActionBar(toolbar)

        viewModel = viewModelProvider(factory)

        binding.viewModel=viewModel

        viewModel.taskId=taskId
        viewModel.patientId=patientId

        viewModel.mesAction.observe(this, EventObserver {
            toast.setText(it)
            toast.show()
        })


        adapter= PictureAdapter(this,4)

        adapter.setActionListener(photoAction!!)
        adapter.locals= emptyList()
        attachments.adapter=adapter
        viewModel.patient.observe(this, Observer {
            it ?: return@Observer
            adapter.remotes=it.attachments.map { attachment -> attachment.httpUrl }
        })


        viewModel.savePatientResult.observe(this, Observer {

            when(it){
                is Resource.Success->{


                    Intent().let {intent->
                        intent.putExtra(NEW_PATIENT_ID,it.data.result)
                        setResult(RESULT_OK, intent)
                        finish()
                    }
                }
            }

        })

        attack_button.setOnClickListener(this)

        viewModel.shareClick.observe(this, Observer {
            when(it){
                "share" ->toShareVital()
                "saveSuccess" ->{
                    Intent().let { intent->
                        setResult(RESULT_OK, intent)
                        finish()
                    }
                }
            }
        })

        mShortAnimationDuration = resources.getInteger(android.R.integer.config_shortAnimTime)

        title=if(viewModel.preHos)"基本信息" else "病人信息"
        client.setLocationListener(this)
        viewModel.patient.observe(this, Observer {
            if(it.attackPosition.isNullOrEmpty()){
                checkGpsPermission()
            }
        })
        loc.setOnClickListener {
            checkGpsPermission()
        }
    }

    fun toShareVital(){
        if(viewModel.patientSavable){
            val intent = Intent(this, ShareActivity::class.java).apply {
                putExtra(ShareActivity.PATIENT_ID, patientId)
                putExtra(ShareActivity.URL, "230-3")
            }
            startActivityForResult(intent, VitalSignsRecordActivity.SHARE)
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
            dispatchTakePictureIntent(adapter.locals.map { it.first },4-adapter.remotes.size)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            CAMERA_PERMISSION_REQUEST->{
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                  dispatchTakePictureIntent(adapter.locals.map { it.first },4-adapter.remotes.size)
                }
            }
            GIS_PERMISSION_REQUEST -> {
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    client.stopLocation()
                    client.startLocation()
                }
            }
        }
    }

    inner class EventAction :PhotoEventAction{
        override fun deleteRemote(url: String) {

        }

        override fun localSelected() {
            checkPhotoTaking()
        }

        override fun enlargeRemote(imageView:View, url: String) {
            zoomImageFromThumb(imageView,enlarged,url)
        }
    }

    override fun onDestroy() {
        photoAction=null
        client.let {
            it.stopLocation()
            it.onDestroy()
        }

        super.onDestroy()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== Activity.RESULT_OK) {
            when (requestCode) {

                PictureConfig.CHOOSE_REQUEST->{
                    viewModel.bitmaps.clear()
                    adapter.locals= PictureSelector.obtainMultipleResult(data)?.map {localmedia->

                        viewModel.bitmaps.add(localmedia.path)

                        return@map Pair(localmedia,FileProvider.getUriForFile(
                                this,
                                BuildConfig.APPLICATION_ID + ".fileProvider",
                                File(localmedia.path)))
                    }?: emptyList()

                }
            }
        }

    }

    private fun zoomImageFromThumb(thumbView: View, imageView:ImageView, imageResId: String) {
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
            play(ObjectAnimator.ofFloat(
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
    private fun checkGpsPermission() {
        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
            ||
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
                GIS_PERMISSION_REQUEST
            )

        } else {

            client.stopLocation()
            client.startLocation()
        }
    }

}
