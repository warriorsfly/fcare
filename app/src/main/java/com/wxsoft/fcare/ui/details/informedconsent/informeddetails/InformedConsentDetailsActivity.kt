package com.wxsoft.fcare.ui.details.informedconsent.informeddetails

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.graphics.Point
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.LevelListDrawable
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.GlideApp
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.databinding.ActivityInformedConsentDetailsBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.ui.PhotoEventAction
import com.wxsoft.fcare.ui.PlayVoiceEventAction
import kotlinx.android.synthetic.main.activity_informed_consent_details.*
import kotlinx.android.synthetic.main.layout_common_title.*
import javax.inject.Inject

class InformedConsentDetailsActivity : BaseActivity() {

    private lateinit var patientId:String
    private lateinit var talkId:String
    private lateinit var talkName:String
    private lateinit var informedId:String
    companion object {
        const val PATIENT_ID = "PATIENT_ID"
        const val TALK_ID = "TALK_ID"
        const val TALK_NAME = "TALK_NAME"
        const val INFORMED_ID = "INFORMED_ID"
    }

    private val STATE_RECORD_PLAY = 3           // 正在播放录音
    private val STATE_RECORD_PAUSE = 4          // 暂停录音

    private lateinit var viewModel: InformedConsentDetailsViewModel
    @Inject
    lateinit var factory: ViewModelFactory

    lateinit var binding: ActivityInformedConsentDetailsBinding

    private lateinit var adapter: InformedDetailsAdapter

    private var mCurrentAnimator: Animator? = null
    private var mShortAnimationDuration: Int = 0

    private var photoAction:EventAction? = EventAction()

    private var voiceAction:VocieAction? =VocieAction()


    private val mediaPlayer by lazy { MediaPlayer() }

    private var recordState = STATE_RECORD_PAUSE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(factory)
        binding = DataBindingUtil.setContentView<ActivityInformedConsentDetailsBinding>(this, R.layout.activity_informed_consent_details)
            .apply {
                lifecycleOwner = this@InformedConsentDetailsActivity
            }
        patientId=intent.getStringExtra(InformedConsentDetailsActivity.PATIENT_ID)?:""
        talkId=intent.getStringExtra(InformedConsentDetailsActivity.TALK_ID)?:""
        talkName=intent.getStringExtra(InformedConsentDetailsActivity.TALK_NAME)?:""
        informedId=intent.getStringExtra(InformedConsentDetailsActivity.INFORMED_ID)?:""

        viewModel.patientId = patientId
        binding.viewModel = viewModel

        back.setOnClickListener { onBackPressed() }

        viewModel.getTalkById(talkId)


        adapter = InformedDetailsAdapter(this)
        adapter.setActionListener(photoAction!!)
        adapter.setVoiceActionListener(voiceAction!!)
        binding.attachments.adapter = adapter
//
        viewModel.talk.observe(this, Observer {
            if (it != null){
                adapter.remotes = it.attachments
                viewModel.getInformedConsentById(it.informedConsentId)
                viewModel.title = it.informedConsentName
            }
        })

    }

    override fun onDestroy() {
        super.onDestroy()
        photoAction=null
        voiceAction=null
    }


    inner class EventAction : PhotoEventAction {
        override fun localSelected() {

        }
        override fun enlargeRemote(imageView: View, url: String) {
            zoomImageFromThumb(imageView,enlarged,url)
        }
    }

    inner class VocieAction : PlayVoiceEventAction {
        override fun play(imageView:ImageView,url: String) {
//            mediaPlayer.setDataSource(url)
//            mediaPlayer.prepare()
//            mediaPlayer.start()
//            changeState(STATE_RECORD_PLAY)
//                (image.drawable as? LevelListDrawable)?.level = if (isPlaying) 1 else 0

            if (recordState == STATE_RECORD_PAUSE) {
                (imageView.drawable as? LevelListDrawable)?.level = 1
                mediaPlayer.setOnCompletionListener { play(imageView,url) }
                mediaPlayer.reset()
                mediaPlayer.setDataSource(url)
                mediaPlayer.prepare()
                mediaPlayer.start()
                changeState(STATE_RECORD_PLAY)
            }else{
                (imageView.drawable as? LevelListDrawable)?.level = 0
                mediaPlayer.stop()
                changeState(STATE_RECORD_PAUSE)
            }

        }

    }

    /**
     * 根据状态改变控件显示属性
     */
    private fun changeState(recordState: Int){
        when(recordState){
            STATE_RECORD_PAUSE -> {

            }
            STATE_RECORD_PLAY -> {

            }
        }
        this.recordState = recordState
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

}
