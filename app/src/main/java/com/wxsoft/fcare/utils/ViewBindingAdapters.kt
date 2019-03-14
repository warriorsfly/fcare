/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wxsoft.fcare.utils

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseMethod
import android.net.Uri
import android.os.Build
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.data.entity.rating.Option
import com.wxsoft.fcare.core.data.entity.rating.Rating
import com.wxsoft.fcare.core.data.entity.rating.Subject
import com.wxsoft.fcare.di.GlideApp

@BindingAdapter("taskSelectUser")
fun taskSelectUser(view: View, visible: Boolean) {
    view.setBackgroundResource(if (visible) R.drawable.bg_select_user_selected else R.drawable.bg_select_user_normal)
}

@BindingAdapter("taskSelectCar")
fun taskSelectCar(view: View, visible: Boolean) {
    view.setBackgroundResource(if (visible) R.drawable.bg_redconer_whiteblank else R.drawable.bg_redconer_redblank)
}
@BindingAdapter("taskSelectCarTextColor")
fun taskSelectCarTextColor(view: TextView, visible: Boolean) {
    view.setTextColor(if (visible) view.context.resources.getColor(R.color.task_select_car_able_text) else view.context.resources.getColor(R.color.task_select_car_unable_text))
}

@BindingAdapter("taskAt")
fun taskAt(view: TextView, visible: Boolean) {
    view.setBackgroundResource(if (visible) R.drawable.bg_task_process_normal else R.drawable.bg_task_process_pressed)
    view.elevation=if (visible) 0.5f else 6f
}

@RequiresApi(Build.VERSION_CODES.M)
@BindingAdapter("taskedAt")
fun taskedAt(view: TextView, visible: Boolean) {
    view.setTextColor(
        if (visible)
            view.context.resources.getColor( R.color.colorPrimary,view.context.theme)
        else
            view.context.resources.getColor( R.color.tint_rating_option_text_unselected,view.context.theme))
}

@BindingAdapter("overflowAt")
fun overflowAt(view: TextView, visible: Boolean) {
    view.setTextColor(if (visible) Color.parseColor("#FE5F55") else Color.parseColor("#525252"))
}

/**
 * task complated time only show hour and minute
 */
@BindingAdapter("doneAt")
fun doneAt(view: TextView, doneAt:String?) {
    view.text=doneAt?.substring(11,16)
}

//@BindingAdapter("android:onClick","rating","subject","option")
//fun selectOption(view: View,rating: Rating?, subject:Subject?,option:Option?) {
//
//    option?.let {
//        subject?.check(it)
//        rating?.refreshScore()
//    }
//}


@BindingAdapter("invisibleUnless")
fun invisibleUnless(view: View, visible: Boolean) {
    view.visibility = if (visible) View.VISIBLE else View.INVISIBLE
}

@BindingAdapter("goneUnless")
fun goneUnless(view: View, visible: Boolean) {
    view.visibility = if (visible) View.VISIBLE else View.GONE
}

@BindingAdapter("pageMargin")
fun pageMargin(viewPager: androidx.viewpager.widget.ViewPager, pageMargin: Float) {
    viewPager.pageMargin = pageMargin.toInt()
}

@BindingAdapter("diagnosisCode")
fun diagnosisCode(textView: TextView, diagnosisCode: String?) {
    when(diagnosisCode){
        "215-1"->textView.setBackgroundResource(R.drawable.ic_diagnosis1)
        "215-2"->textView.setBackgroundResource(R.drawable.ic_diagnosis5)
        "215-3"->textView.setBackgroundResource(R.drawable.ic_diagnosis1)
        "215-4"->textView.setBackgroundResource(R.drawable.ic_diagnosis4)
        "215-5"->textView.setBackgroundResource(R.drawable.ic_diagnosis5)
        //不在上述代码中则清空textView的背景
        else->textView.setBackgroundResource(0)
    }
}

@SuppressLint("ResourceAsColor")
@BindingAdapter("patientStatus")
fun patientStatus(textView: TextView, patientStatus: String?) {
    when(patientStatus){
        "223-1"->{
            textView.setBackgroundResource(R.drawable.bg_conor_status_1)//院前
            textView.setTextColor(Color.parseColor("#796EF4"))
        }
        "223-2"->{
            textView.setBackgroundResource(R.drawable.bg_conor_status_2)//初诊
            textView.setTextColor(Color.parseColor("#FAB131"))
        }
        "223-3"->{
            textView.setBackgroundResource(R.drawable.bg_conor_status_3)//治疗前检查
            textView.setTextColor(Color.parseColor("#FF885D"))
        }
        "223-4"->{
            textView.setBackgroundResource(R.drawable.bg_conor_status_4)//治疗
            textView.setTextColor(textView.context.resources.getColor(R.color.colorPrimary))
        }
        "223-5"->{
            textView.setBackgroundResource(R.drawable.bg_conor_status_5)//转归
            textView.setTextColor(Color.parseColor("#0FCEC6"))
        }
        //不在上述代码中则清空textView的背景
        else->textView.setBackgroundResource(0)
    }
}

@SuppressLint("ResourceAsColor")
@BindingAdapter("taskStatus")
fun taskStatus(textView: TextView, taskOverallStatu: Int?) {
    when(taskOverallStatu){
        1->{
            textView.setBackgroundResource(R.drawable.bg_conor_status_4)//进行中
            textView.setText("进行中")
            textView.setTextColor(Color.parseColor("#63B0F8"))
        }
        2->{
            textView.setBackgroundResource(R.drawable.bg_conor_status_5)//已完成
            textView.setText("已完成")
            textView.setTextColor(Color.parseColor("#30C890"))
        }
        3->{
            textView.setBackgroundResource(R.drawable.bg_conor_status_3)//已取消
            textView.setText("已取消")
            textView.setTextColor(Color.parseColor("#FF8182"))
        }
        //不在上述代码中则清空textView的背景
        else->textView.setBackgroundResource(0)
    }
}


@BindingAdapter(value = ["imageUri"], requireAll = false)
fun setImageUrl(imageView: ImageView, url: Uri?) {

    GlideApp.with(imageView.context).load(url).placeholder(R.mipmap.img_electrocardiogram).into(imageView)
}

@BindingAdapter(value = ["opItem"], requireAll = false)
fun setOpItem(imageView: ImageView, ico:Int) {

    GlideApp.with(imageView.context).load(ico)
        .fitCenter().circleCrop().into(imageView)
}


@BindingAdapter(value = ["imageUrl"], requireAll = false)
fun setImageUrl(imageView: ImageView, url: String?) {

    GlideApp.with(imageView.context).load(url).placeholder(R.mipmap.img_electrocardiogram).into(imageView)
}

@BindingAdapter(value = ["eventStatus"], requireAll = false)
fun setEventStatus(imageView: ImageView, status: String?) {

    when(status) {

        "fail" -> {
            imageView.let {
                it.setImageResource(R.drawable.ic_time_point_warning)
                it.setPadding(0, 6, 0, 6)
            }
        }
        "success" -> {
            imageView.let {
                it.setImageResource(R.drawable.ic_work_space_operation_done)
                it.setPadding(0, 6, 0, 6)
            }
        }
        else -> {
            imageView.let {
                it.setImageResource(R.drawable.ic_black)
                it.setPadding(0, 0, 0, 0)
            }
        }
    }
}

@BindingAdapter(value = ["eventMemoStatus"], requireAll = false)
fun setEventStatus(textView: TextView, status: String?) {
    if(status==null){
        textView.layoutParams.height=0

    }else{
        textView.layoutParams.height=LinearLayout.LayoutParams.WRAP_CONTENT
    }
}

@BindingAdapter(value = ["ratingGradeColor"], requireAll = false)
fun setRatingGradeColor(textView: TextView, color: String?) {
    if(color==null){
        textView.setTextColor(Color.BLACK)

    }else{
        textView.setTextColor(Color.parseColor(color))
    }
}

object Converter{
    @InverseMethod("stringToInt")
    @JvmStatic fun intToString(value: Int): String {
        return if(value==0)"" else value.toString()
    }

    @JvmStatic fun stringToInt(value:String): Int {
        return if(value.isEmpty()) 0 else value.toInt()
    }
}

object QualityScoreConverter{
    @InverseMethod("stringToInt")
    @JvmStatic fun intToString(value: Int): String {
        return if(value==0)"-" else value.toString()
    }

    @JvmStatic fun stringToInt(value:String): Int {
        return if(value=="-") 0 else value.toInt()
    }
}

object LongConverter{
    @InverseMethod("stringToLong")
    @JvmStatic fun longToString(value: Long): String {
        return if(value==0L) ""  else value.toString()
    }

    @JvmStatic fun stringToLong(value:String): Long {
        return if(value.isEmpty()) 0L else value.toLong()
    }
}

object FloatConverter{
    @InverseMethod("stringToFloat")
    @JvmStatic fun floatToString(value: Float?):String  {
        return if(value==null) ""  else value.toString()

    }

    @JvmStatic fun stringToFloat(value:String): Float? {
        return if(value.isEmpty()) null else value.toFloat()
    }
}