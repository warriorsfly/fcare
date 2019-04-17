package com.wxsoft.fcare.ui.details.ecg

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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.wxsoft.fcare.BuildConfig
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.utils.inTransaction
import com.wxsoft.fcare.core.utils.lazyFast
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.databinding.ActivityEcgBinding
import com.wxsoft.fcare.databinding.ItemDialogImageBinding
import com.wxsoft.fcare.databinding.ItemImageBinding
import com.wxsoft.fcare.di.GlideApp
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.PhotoEventAction
import com.wxsoft.fcare.ui.common.EcgAdapter
import com.wxsoft.fcare.ui.details.ecg.fragment.EcgEditFragment
import com.wxsoft.fcare.ui.rating.RatingSubjectActivity
import com.wxsoft.fcare.ui.share.ShareActivity
import kotlinx.android.synthetic.main.activity_ecg.*
import java.io.File
import javax.inject.Inject

class EcgActivity : BaseActivity(),PhotoEventAction {
    override fun deleteRemote(url: String) {
        val binding=ItemDialogImageBinding.inflate(layoutInflater).apply {
            lifecycleOwner=this@EcgActivity
            imageUrl=url
        }
        AlertDialog.Builder(this,R.style.Theme_FCare_Dialog)
            .setView(binding.root)
            .setMessage("确定删除吗？")
            .setPositiveButton("是") { _, _ ->
                viewModel.deleteImage(url)
            }
            .setNegativeButton("否") { _, _ ->
            }.show()

    }


    private val  fragment by lazy{
        EcgEditFragment()
    }
    companion object {
        const val PHOTO_COUNT=9
    }
    override fun localSelected() {
        checkPhotoTaking()
    }

    override fun enlargeRemote(imageView: View, url: String) {
        zoomImageFromThumb(imageView,enlarged,url)
    }

    private var mCurrentAnimator: Animator? = null
    private var mShortAnimationDuration: Int = 0

    private lateinit var viewModel: EcgViewModel
    @Inject
    lateinit var factory: ViewModelFactory

    private lateinit var adapter:EcgAdapter

    private val patientId: String by lazyFast {
        intent?.getStringExtra(RatingSubjectActivity.PATIENT_ID)?:""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel=viewModelProvider(factory)
        viewModel.patientId=patientId
        adapter= EcgAdapter(this,PHOTO_COUNT,this)
        DataBindingUtil.setContentView<ActivityEcgBinding>(
            this,
            R.layout.activity_ecg
        ).apply{
            viewModel=this@EcgActivity.viewModel
            list.adapter=this@EcgActivity.adapter
            lifecycleOwner = this@EcgActivity
        }
        viewModel.ecg.observe(this, Observer {
            adapter.locals= emptyList()
            adapter.remotes=it.attachments.map { it.httpUrl }
        })
        setSupportActionBar(toolbar)

        start.setOnClickListener {
            if(fragment.isAdded)return@setOnClickListener
            supportFragmentManager.inTransaction {

                setCustomAnimations(
                    R.animator.left_enter,
                    R.animator.left_exit,
                    R.animator.right_enter,
                    R.animator.right_exit)
                addToBackStack(null)
                add(R.id.fragment_container, fragment)
            }
        }

        share_tv.setOnClickListener {
            toShareEcg()
        }


        viewModel.saved.observe(this, Observer {

            setResult(Activity.RESULT_OK)
        })

        see_reactive.setOnClickListener {
            val intent=Intent(this,ReactiveEcgActivity::class.java)
            startActivity(intent)
        }
    }

    private fun checkPhotoTaking(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                BaseActivity.CAMERA_PERMISSION_REQUEST
            )

        }else{
            dispatchTakePictureIntent(adapter.locals.map { it.first },PHOTO_COUNT-adapter.remotes.size)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            BaseActivity.CAMERA_PERMISSION_REQUEST ->{
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                    dispatchTakePictureIntent(adapter  .locals.map { it.first }, PHOTO_COUNT-adapter.remotes.size)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if(resultCode== Activity.RESULT_OK) {
            when (requestCode) {

                PictureConfig.CHOOSE_REQUEST->{
                    viewModel.bitmaps.clear()
                    adapter.locals= PictureSelector.obtainMultipleResult(data)?.map { media->

                        viewModel.bitmaps.add(media.path)

                        return@map Pair(media, FileProvider.getUriForFile(
                            this,
                            BuildConfig.APPLICATION_ID + ".fileProvider",
                            File(media.path)
                        ))
                    }?: emptyList()

                }
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_subject,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

       return when(item?.itemId){
            R.id.submit->{
                viewModel.saveEcg()
                true
            }
            else->super.onOptionsItemSelected(item)
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

    fun toShareEcg(){
        val intent = Intent(this, ShareActivity::class.java).apply {
            putExtra(ShareActivity.PATIENT_ID, patientId)
            putExtra(ShareActivity.TYPE_ID, "230-1")
        }
        startActivityForResult(intent,100 )
    }

    override fun onBackPressed() {

        if(fragment.isAdded) {
            fragment.childFragmentManager.also {
                if (it.popBackStackImmediate()) {
                    it.popBackStack()
                } else {
                    if (supportFragmentManager.popBackStackImmediate()) {
                        supportFragmentManager.popBackStack()
                    } else {
                        super.onBackPressed()
                    }
                }
            }
        }else{
            super.onBackPressed()
        }
    }
}
