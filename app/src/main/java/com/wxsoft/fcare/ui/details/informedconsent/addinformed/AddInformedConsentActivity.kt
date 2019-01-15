package com.wxsoft.fcare.ui.details.informedconsent.addinformed

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.databinding.ActivityAddInformedConsentBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.common.PictureAdapter
import com.wxsoft.fcare.utils.viewModelProvider
import kotlinx.android.synthetic.main.activity_patient_profile.*
import kotlinx.android.synthetic.main.layout_common_title.*
import javax.inject.Inject

class AddInformedConsentActivity : BaseActivity()  {

    private lateinit var patientId:String
    companion object {
        const val PATIENT_ID = "PATIENT_ID"
    }
    private lateinit var viewModel: AddInformedConsentViewModel
    @Inject
    lateinit var factory: ViewModelFactory

    lateinit var binding: ActivityAddInformedConsentBinding

    private lateinit var adapter:PictureAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(factory)
        binding = DataBindingUtil.setContentView<ActivityAddInformedConsentBinding>(this, R.layout.activity_add_informed_consent)
            .apply {
                setLifecycleOwner(this@AddInformedConsentActivity)
            }
        patientId=intent.getStringExtra(AddInformedConsentActivity.PATIENT_ID)?:""
        viewModel.patientId = patientId
        binding.viewModel = viewModel

        back.setOnClickListener { onBackPressed() }


        adapter= PictureAdapter(this,4)
        adapter.locals= emptyList()
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
            CAMERA_PERMISSION_REQUEST ->{
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

            viewModel.photos.add(photo)
//            adapter.photoAdapter.locals= viewModel.photos.toList()
        }
    }

    private fun showPhotoTaking(){
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST)
    }


}
