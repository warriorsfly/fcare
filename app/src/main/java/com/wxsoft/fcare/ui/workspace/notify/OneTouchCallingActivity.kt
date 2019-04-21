package com.wxsoft.fcare.ui.workspace.notify

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.databinding.ActivityOneTouchCallingBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.details.diagnose.DiagnoseActivity
import javax.inject.Inject
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import android.provider.Settings
import android.widget.Toast


class OneTouchCallingActivity : BaseActivity() {

    private lateinit var patientId:String
    var phoneNuber = ""
    companion object {
        const val PATIENT_ID = "PATIENT_ID"
        const val MY_PERMISSIONS_REQUEST_CALL_PHONE = 10
    }
    private lateinit var viewModel: OneTouchCallingViewModel
    @Inject
    lateinit var factory: ViewModelFactory

    lateinit var binding: ActivityOneTouchCallingBinding
    private lateinit var adapter: OneTouchCallingListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = this.viewModelProvider(factory)
        binding = DataBindingUtil.setContentView<ActivityOneTouchCallingBinding>(this, R.layout.activity_one_touch_calling)
            .apply {
                adapter = OneTouchCallingListAdapter(this@OneTouchCallingActivity,this@OneTouchCallingActivity.viewModel)
                list.adapter = this@OneTouchCallingActivity.adapter
                lifecycleOwner = this@OneTouchCallingActivity
            }
        patientId = intent.getStringExtra(DiagnoseActivity.PATIENT_ID) ?: ""
        viewModel.patientId = patientId
        viewModel.getCalls()
        viewModel.calls.observe(this, Observer { adapter.submitList(it) })

        viewModel.callNumber.observe(this, Observer { startCallPhone(it) })

        viewModel.callResult.observe(this, Observer {
            Intent().let { intent->
                setResult(RESULT_OK, intent)
                finish()
            }
        })

    }


    fun call(numer:String){
        val intent = Intent(Intent.ACTION_CALL)
        val data = Uri.parse("tel:${numer}")
        intent.data = data
        startActivity(intent)
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
                    val uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                } else {
                    // 不需要解释为何需要该权限，直接请求授权
                    ActivityCompat.requestPermissions(this,
                        arrayOf<String>(Manifest.permission.CALL_PHONE),
                        MY_PERMISSIONS_REQUEST_CALL_PHONE);
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
    // 处理权限申请的回调
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
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


}