package com.wxsoft.fcare.ui.patient

import android.Manifest
import android.arch.lifecycle.Observer
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.result.EventObserver
import com.wxsoft.fcare.databinding.ActivityLoginBinding
import com.wxsoft.fcare.databinding.ActivityPatientProfileBinding
import com.wxsoft.fcare.generated.callback.OnClickListener
import com.wxsoft.fcare.service.JPushReceiver.Companion.RegistrationId
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.common.AttachmentAdapter
import com.wxsoft.fcare.ui.common.ForNewItem
import com.wxsoft.fcare.ui.main.MainActivity
import com.wxsoft.fcare.utils.lazyFast
import com.wxsoft.fcare.utils.viewModelProvider
import kotlinx.android.synthetic.main.activity_patient_profile.*
import kotlinx.android.synthetic.main.layout_common_title.*
import javax.inject.Inject

/**
 * A login screen that offers login via email/password.
 */
class ProfileActivity : BaseActivity() {

    private val toast:Toast by  lazy {
        Toast.makeText(this,"",Toast.LENGTH_SHORT)
    }

    companion object {
        const val TASK_ID = "TASK_ID"
        const val PATIENT_ID = "PATIENT_ID"
        const val CAMERA_PIC_REQUEST=10
        const val CAMERA_PERMISSION_REQUEST=11
    }

    private val photos= ArrayList<Bitmap>()

    private val patientId: String by lazyFast {
        val args = intent ?: throw IllegalStateException("Missing arguments!")
        args.getStringExtra(PATIENT_ID)
    }

    private val taskId: String by lazyFast {
        val args = intent ?: throw IllegalStateException("Missing arguments!")
        args.getStringExtra(PATIENT_ID)
    }


    @Inject
    lateinit var factory: ViewModelFactory

    private lateinit var adapter:AttachmentAdapter
    private lateinit var viewModel:ProfileViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding = DataBindingUtil.setContentView<ActivityPatientProfileBinding>(
            this,
            R.layout.activity_patient_profile
        ).apply{

            setLifecycleOwner(this@ProfileActivity)

        }

        back.setOnClickListener { onBackPressed() }
        viewModel = viewModelProvider(factory)

        binding.viewModel=viewModel

        viewModel.taskId=taskId
        viewModel.patientId=patientId

        viewModel.mesAction.observe(this, EventObserver {
            toast.setText(it)
            toast.show()
        })


        adapter= AttachmentAdapter(this, OnClickListener(OnClickListener.Listener{ _, view ->
            when(view.tag){
                ForNewItem->{
                    checkPhotoTaking()
                }
            }
        },1))

        adapter.attachs= emptyList()
        attachments.adapter=adapter


    }


    private fun checkPhotoTaking(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {


            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                CAMERA_PERMISSION_REQUEST
            )

        }else{
            showPhotoTaking()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            CAMERA_PERMISSION_REQUEST->{
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                    showPhotoTaking()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (CAMERA_PIC_REQUEST == requestCode) {
            val photo = data?.extras?.get("data") as Bitmap

            viewModel.bitmaps.add(photo)
            adapter.attachs= viewModel.bitmaps.toList()
        }
    }

    private fun showPhotoTaking(){
        val cameraIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST)
    }

}
