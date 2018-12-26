package com.wxsoft.fcare.service

import android.app.KeyguardManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import cn.jpush.android.api.JPushInterface
import com.wxsoft.fcare.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.ui.calling.CallingActivity
import org.json.JSONException
import org.json.JSONObject
import javax.inject.Inject

/**
 * 自定义接收器
 *
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
class JPushReceiver  : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        try {
            val bundle = intent.extras
            when (intent.action) {
                JPushInterface.ACTION_REGISTRATION_ID -> {
                    val regId = bundle!!.getString(JPushInterface.EXTRA_REGISTRATION_ID)

                    processCustomMessage(context,RegistrationId,regId)
                }
                JPushInterface.ACTION_MESSAGE_RECEIVED -> {
                    val notifactionId = bundle!!.getInt(JPushInterface.EXTRA_NOTIFICATION_ID)
                    val message = bundle.getString(JPushInterface.EXTRA_MESSAGE)
                    val title= bundle.getString(JPushInterface.EXTRA_TITLE)
                    processCustomMessage(context,title,message)
                }
                JPushInterface.ACTION_NOTIFICATION_RECEIVED -> {//Logger.d(TAG, "[MyReceiver] 接收到推送下来的通知");

                }
                JPushInterface.ACTION_NOTIFICATION_OPENED -> {

                    val notifactionId = bundle!!.getInt(JPushInterface.EXTRA_NOTIFICATION_ID)

                    val title= bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE)
                    if(title=="急救通知") {
                        bundle.getString(JPushInterface.EXTRA_ALERT)

                    }
                }
                JPushInterface.ACTION_RICHPUSH_CALLBACK -> {
                    bundle!!.getInt(JPushInterface.EXTRA_NOTIFICATION_ID)
                }
                JPushInterface.ACTION_CONNECTION_CHANGE -> {
                    val connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false)
                    Log.i("极光连接",if(connected)"成功" else "失败")
//                    val regId = bundle!!.getString(JPushInterface.EXTRA_REGISTRATION_ID)
//                    sharedPreferenceStorage.jpushRegId=regId
                }
                else -> {
                    //Logger.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
                }
            }
        } catch (e: Exception) {
            Log.i("错误",e.message)
        }

    }

    //send msg to MainActivity
    private fun processCustomMessage(context: Context,title:String,message:String) {
        when(title) {
            "SendMessage" -> {

                if (message.isNotEmpty()) {

                    val km = context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
                    if (km.isKeyguardLocked) {
                        val alarmIntent = Intent(context, CallingActivity::class.java).apply {
                            putExtra("notify", message)
                        }
                        alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        context.startActivity(alarmIntent)
                    }
                }
            }
            RegistrationId->{
                val timeLineRefresh=Intent(RegistrationId).apply {
                    putExtra(RegistrationId,message)
                }

                context.sendBroadcast(timeLineRefresh)
            }
            "RefreshTimeLine"->{

                val timeLineRefresh=Intent(LineRefresh).apply {
                    putExtra("pid",message)
                }

                context.sendBroadcast(timeLineRefresh)
            }
        }
    }

    companion object {

        const val LineRefresh="LineRefresh"
        const val RegistrationId="RegistrationId"
        // 打印所有的 intent extra 数据
        private fun printBundle(bundle: Bundle): String {
            val sb = StringBuilder()
            for (key in bundle.keySet()) {
                if (key == JPushInterface.EXTRA_NOTIFICATION_ID) {
                    sb.append("\nkey:" + key + ", value:" + bundle.getInt(key))
                } else if (key == JPushInterface.EXTRA_CONNECTION_CHANGE) {
                    sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key))
                } else if (key == JPushInterface.EXTRA_EXTRA) {
                    if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                        //Logger.i(TAG, "This message has no Extra liveDiagnosis");
                        continue
                    }

                    try {
                        val json = JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA))
                        val it = json.keys()

                        while (it.hasNext()) {
                            val myKey = it.next()
                            sb.append(
                                "\nkey:" + key + ", value: [" +
                                        myKey + " - " + json.optString(myKey) + "]"
                            )
                        }
                    } catch (e: JSONException) {
                        //Logger.e(TAG, "Get message extra JSON error!");
                    }

                } else {
                    sb.append("\nkey:" + key + ", value:" + bundle.get(key))
                }
            }
            return sb.toString()
        }
    }
}
