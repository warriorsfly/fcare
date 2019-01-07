package com.wxsoft.fcare.ui.details.medicalhistory

import android.Manifest
import android.arch.lifecycle.Observer
import android.content.Intent
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.MenuItem
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.databinding.ActivityMedicalHistoryBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.patient.ProfileActivity
import com.wxsoft.fcare.utils.viewModelProvider
import javax.inject.Inject

class MedicalHistoryActivity : BaseActivity() {

    private lateinit var patientId:String
    companion object {
        const val PATIENT_ID = "PATIENT_ID"
    }

    private lateinit var viewModel: MedicalHistoryViewModel
    @Inject
    lateinit var factory: ViewModelFactory

    lateinit var binding: ActivityMedicalHistoryBinding

    lateinit var adapter: MedicalHistoryAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setTitle("病史")

        viewModel = viewModelProvider(factory)
        binding = DataBindingUtil.setContentView<ActivityMedicalHistoryBinding>(this, R.layout.activity_medical_history)
            .apply {
                setLifecycleOwner(this@MedicalHistoryActivity)
            }
        patientId=intent.getStringExtra(MedicalHistoryActivity.PATIENT_ID)?:""
        viewModel.patientId = patientId
        binding.viewModel = viewModel

        adapter = MedicalHistoryAdapter(this,viewModel,this)
        binding.medicalHistoryList.adapter = adapter

        viewModel.loadMedicalHistory()

        viewModel.historyPhoto.observe(this, Observer {
            checkPhotoTaking()
        })


    }


    private fun checkPhotoTaking(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {


            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                ProfileActivity.CAMERA_PERMISSION_REQUEST
            )

        }else{
            showPhotoTaking()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            ProfileActivity.CAMERA_PERMISSION_REQUEST ->{
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                    showPhotoTaking()
                }
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (ProfileActivity.CAMERA_PIC_REQUEST == requestCode) {
            val photo = data?.extras?.get("data") as Bitmap

            viewModel.photos.add(photo)
            adapter.photoAdapter.attachs= viewModel.photos.toList()
        }
    }

    private fun showPhotoTaking(){
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, ProfileActivity.CAMERA_PIC_REQUEST)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle presses on the action bar items
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

}
