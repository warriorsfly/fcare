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
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
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
import com.wxsoft.fcare.core.data.entity.Dictionary
import com.wxsoft.fcare.core.data.entity.Patient
import com.wxsoft.fcare.core.data.entity.Tag
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.result.Event
import com.wxsoft.fcare.core.result.EventObserver
import com.wxsoft.fcare.core.result.Resource
import com.wxsoft.fcare.core.utils.DateTimeUtils
import com.wxsoft.fcare.core.utils.lazyFast
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.databinding.ActivityPatientProfileBinding
import com.wxsoft.fcare.di.GlideApp
import com.wxsoft.fcare.ui.BaseTimeShareDeleteActivity
import com.wxsoft.fcare.ui.PhotoEventAction
import com.wxsoft.fcare.ui.common.PictureAdapter
import com.wxsoft.fcare.ui.details.fast.FastActivity
import com.wxsoft.fcare.ui.details.vitalsigns.records.VitalSignsRecordActivity
import com.wxsoft.fcare.ui.patient.choice.ChoicePatientActivity
import com.wxsoft.fcare.ui.patient.choice.WristbandActivity
import com.wxsoft.fcare.ui.selecter.SelecterOfOneModelActivity
import com.wxsoft.fcare.ui.share.ShareActivity
import com.wxsoft.fcare.utils.ActionCode.Companion.FAST
import id.zelory.compressor.Compressor
import kotlinx.android.synthetic.main.activity_patient_profile.*
import kotlinx.android.synthetic.main.layout_new_title.*
import java.io.File
import javax.inject.Inject
import javax.inject.Named


class ProfileActivity : BaseTimeShareDeleteActivity(), View.OnClickListener,PhotoEventAction ,AMapLocationListener{
    override fun selectTime(millseconds: Long) {

        dialog?.onDestroy()
        dialog=null
        (findViewById<TextView>(selectedId))?.text= DateTimeUtils.formatter.format(millseconds)
    }

    var phoneNuber = ""

    override fun onLocationChanged(p0: AMapLocation?) {

        p0?.let {
            locat.setText(it.address)
        }
    }


    override fun doError(throwable: Throwable) {

    }

    override fun delete(id: String) {
        viewModel.delete(id)
    }

    override fun localSelected() {
        checkPhotoTaking()
    }

    override fun enlargeRemote(imageView: View, url: String) {
        zoomImageFromThumb(imageView,enlarged,url)
    }

    override fun deleteRemote(url: String) {
        showImageDialog(url)
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

    private val toast:Toast by  lazy {
        Toast.makeText(this,"",Toast.LENGTH_SHORT)
    }

    companion object {
        const val TASK_ID = "TASK_ID"
        const val PATIENT_ID = "PATIENT_ID"
        const val IS_PRE = "IS_PRE"
        const val MY_PERMISSIONS_REQUEST_CALL_PHONE = 100
        const val SELECT_ADDRESS = 101
        const val SELECT_PATIENT = 102
        const val SELECT_Wristband= 103
        const val SELECT_CardType= 104
    }

    private var mCurrentAnimator: Animator? = null
    private var mShortAnimationDuration: Int = 0

    private val patientId: String by lazyFast {
        intent?.getStringExtra(PATIENT_ID)?:""
    }

    private val taskId: String by lazyFast {
         intent ?.getStringExtra(TASK_ID)?:""
    }

    private val handOvered: Boolean by lazyFast {
        intent ?.getBooleanExtra("HandOver",false)?:false

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

        adapter= PictureAdapter(this,4,this,this)

        show_big_outpatientId.visibility=View.GONE
        show_big_inpatientId.visibility=View.GONE


        adapter.locals= emptyList()
        attachments.adapter=adapter
        viewModel.patient.observe(this, Observer {
            it ?: return@Observer
            adapter.locals= emptyList()
            adapter.remotes=it.attachments.map { attachment -> attachment.httpUrl }
        })

        binding.mzh.setOnFocusChangeListener{ view, b ->
            if (b){
                show_big_outpatientId.visibility=View.VISIBLE
            }else{
                viewModel.getPatientInfos(binding.mzh.text.toString(),0)
                show_big_outpatientId.visibility=View.GONE
            }
        }
        binding.zyh.setOnFocusChangeListener{ view, b ->
            if (b){
                show_big_inpatientId.visibility=View.VISIBLE
            }else{
                if (viewModel.patient.value?.outpatientId.isNullOrEmpty()){
                    viewModel.getPatientInfos(binding.zyh.text.toString(),1)
                }
                show_big_inpatientId.visibility=View.GONE
            }
        }
        binding.call.setOnClickListener {
            startCallPhone(binding.callText.text.toString())
        }

        viewModel.startFasting.observe(this, Observer {
            if(viewModel.patient.value?.diagnosisCode=="215-2") {
                val intent = Intent(this@ProfileActivity, FastActivity::class.java).apply {
                    putExtra("id",viewModel.patient.value?.stroke120Id)
                }
                startActivityForResult(intent, FAST)
            }
        })

        viewModel.savePatientResult.observe(this, Observer {

            when{
                (it is Resource.Success && it.data.success)->{
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
                if(handOvered)
                checkGpsPermission()
            }
        })

        loc.apply {
            if(handOvered) {
                setOnClickListener {
                    checkGpsPermission()
                }
            }else{
                visibility=View.GONE
            }
        }

        select_loc.apply {
            if(handOvered) {
                visibility=View.GONE
            }else{
                setOnClickListener {
                    toSelectAdress()
                }
            }
        }
        select_patient.apply {
            if(handOvered) {
                visibility=View.GONE
            }else{
                setOnClickListener {
                    selectPatient()
                }
            }
        }
        select_wristband.apply {
            setOnClickListener {
                toSelectWridsban()
            }
        }
        wristband_id.apply {
            setOnClickListener {
                if (!viewModel.patient.value?.wristband.isNullOrEmpty()){
                    toSelectWridsban()
                }
            }
        }



    }

    fun selectPatient(){
        val intent = Intent(this, ChoicePatientActivity::class.java).apply {
            putExtra(ChoicePatientActivity.PATIENT_ID, patientId)
            putExtra(ChoicePatientActivity.FOR_TASK_CHOOSE, false)
        }
        startActivityForResult(intent, SELECT_PATIENT)
    }

    fun toSelectAdress(){
        val intent = Intent(this, SelecterOfOneModelActivity::class.java).apply {
            putExtra(SelecterOfOneModelActivity.PATIENT_ID, patientId)
            putExtra(SelecterOfOneModelActivity.COME_FROM, "Adress")
        }
        startActivityForResult(intent, SELECT_ADDRESS)
    }

    fun toSelectCardType(){
        val intent = Intent(this, SelecterOfOneModelActivity::class.java).apply {
            putExtra(SelecterOfOneModelActivity.PATIENT_ID, patientId)
            putExtra(SelecterOfOneModelActivity.COME_FROM, "CardType")
        }
        startActivityForResult(intent, SELECT_CardType)
    }

    fun toSelectWridsban(){
        val intent = Intent(this, WristbandActivity::class.java).apply {
            putExtra(WristbandActivity.PATIENT_ID, patientId)
            putExtra(WristbandActivity.ID, viewModel.patient.value?.wristband)
        }
        startActivityForResult(intent, SELECT_Wristband)
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
           MY_PERMISSIONS_REQUEST_CALL_PHONE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 授权成功，继续打电话
                    callPhone(this.phoneNuber)
                } else {
                    // 授权失败！
                    Toast.makeText(this,"授权失败",Toast.LENGTH_SHORT).show()
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

                        return@map kotlin.Pair(localmedia,FileProvider.getUriForFile(
                                this,
                                BuildConfig.APPLICATION_ID + ".fileProvider",
                                File(localmedia.path)))
                    }?: emptyList()

                    val files=viewModel.bitmaps.map {
                        File(it).let {
                            file->
                        Compressor(this@ProfileActivity)
                            .setMaxWidth(1280)
                            .setMaxHeight(1280)
                            .setQuality(75).compressToFile(file)
                    } }
                    viewModel.savePic(files)
                }
                SELECT_ADDRESS ->{
                    val address = data?.getSerializableExtra("SelectOne") as Dictionary
                    viewModel.patient.value?.attackPosition = address.itemName
                }
                SELECT_CardType ->{
                    val cardtype = data?.getSerializableExtra("SelectOne") as Dictionary
                    viewModel.patient.value?.cardType = cardtype.id
                    viewModel.patient.value?.cardTypeName = cardtype.itemName
                }
                SELECT_PATIENT ->{
                    val item = data?.getSerializableExtra("SelectPatient") as Patient
                    viewModel.patient.value?.apply {
                        idcard = item.idcard
                        name = item.name
                        gender = item.gender
                        age = item.age
                        phone = item.phone
                        outpatientId = item.outpatientId
                        if(item.registerDate.isNullOrEmpty())return@apply
                        registerDate = item.registerDate
                    }

                }
                SELECT_Wristband ->{
                    val item = data?.getSerializableExtra("SelectTag") as Tag
                    if (!item.id.isNullOrEmpty()){
                        viewModel.patient.value?.apply {
                            wristband = item.id
                        }
                        Toast.makeText(this,"腕带绑定成功",Toast.LENGTH_SHORT).show()
                    }else{
                        viewModel.patient.value?.apply {
                            wristband = ""
                        }
                    }
                }

                FAST->{
                    val id=data?.getStringExtra("id")
                    viewModel.patient.value?.let{
                        it.stroke120Id=id?:""
                    }
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
                viewModel.save()
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


    fun  startCallPhone(phoneNumber:String) {
        phoneNuber = phoneNumber
        //判断Android版本是否大于23
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val checkCallPhonePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)

            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this, arrayOf<String>(Manifest.permission.CALL_PHONE),
                    MY_PERMISSIONS_REQUEST_CALL_PHONE
                )
                return
            } else {
                callPhone(phoneNumber)
            }
        }else {
            callPhone(phoneNumber)
            // 检查是否获得了权限（Android6.0运行时权限）
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // 没有获得授权，申请授权
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.CALL_PHONE)) {
                    // 返回值：
//                          如果app之前请求过该权限,被用户拒绝, 这个方法就会返回true.
//                          如果用户之前拒绝权限的时候勾选了对话框中”Don’t ask again”的选项,那么这个方法会返回false.
//                          如果设备策略禁止应用拥有这条权限, 这个方法也返回false.
                    // 弹窗需要解释为何需要该权限，再次请求授权
//                    TastyToastUtils.newInstance(mContext).ERROR("请授权");

                    // 帮跳转到该应用的设置界面，让用户手动授权
                    val intent =  Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    val uri = Uri.fromParts("package", packageName, null);
                    intent.data = uri;
                    startActivity(intent);
                } else {
                    // 不需要解释为何需要该权限，直接请求授权
                    ActivityCompat.requestPermissions(this,
                        arrayOf<String>(Manifest.permission.CALL_PHONE),
                        MY_PERMISSIONS_REQUEST_CALL_PHONE
                    );
                }
            } else {
                // 已经获得授权，可以打电话
                callPhone(phoneNumber);
            }
        }

    }


    private fun callPhone(phoneNumber: String) {
        // 拨号：激活系统的拨号组件 -- 直接拨打电话
//        val intent = Intent(Intent.ACTION_CALL)
//        val data = Uri.parse("tel:${phoneNumber}")
//        intent.data = data
//        startActivity(intent)

        //打开拨号界面，填充输入手机号码，让用户自主的选择
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }



}
