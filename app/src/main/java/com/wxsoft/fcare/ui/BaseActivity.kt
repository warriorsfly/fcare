package com.wxsoft.fcare.ui

import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import com.wxsoft.fcare.BuildConfig
import com.wxsoft.fcare.Manifest
import dagger.android.support.DaggerAppCompatActivity
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

abstract class BaseActivity : DaggerAppCompatActivity(){

    companion object {
        const val CAMERA_PERMISSION_REQUEST=10
        const val CAMERA_PIC_REQUEST=11

    }

    protected var mCurrentPhotoPath: Uri?=null
    /**
     * 创建图片文件
     */
    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        if(!storageDir.exists()){
            storageDir.createNewFile()
        }
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        )
//            .apply {
//            // Save a file: path for use with ACTION_VIEW intents
//            mCurrentPhotoPath = absolutePath
//        }
    }

    protected fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {

                    null
                }
                photoFile?.also {file->
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        BuildConfig.APPLICATION_ID+".fileProvider",
                        file
                    ).apply {
                        mCurrentPhotoPath=this
                    }
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, CAMERA_PIC_REQUEST)
                }
            }
        }
    }
}