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

import android.graphics.Color
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseMethod
import android.net.Uri
import androidx.viewpager.widget.ViewPager
import android.view.View
import android.widget.ImageView
import android.widget.TextView
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
    view.setTextColor(if (visible) view.context.resources.getColor(R.color.task_done) else view.context.resources.getColor(R.color.task_undo))
}

@BindingAdapter("overflowAt")
fun overflowAt(view: TextView, visible: Boolean) {
    view.setTextColor(if (visible) Color.parseColor("#FE5F55") else Color.WHITE)
}

/**
 * task complated time only show hour and minute
 */
@BindingAdapter("doneAt")
fun doneAt(view: TextView, doneAt:String?) {
    view.text=doneAt?.substring(11,16)
}

@BindingAdapter("subject","option","selectedIndex","rating")
fun selectedOptionAt(view: View, subject:Subject,option:Option,selectedIndex:Int?,rating: Rating?) {
    if(selectedIndex==null || selectedIndex==-1){
        view.setBackgroundResource(R.color.white)
    }else{
        view.setBackgroundResource(if(subject.options[selectedIndex]==option) R.color.rating_red else R.color.white)
    }
    rating?.changeScore()
}


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
fun diagnosisCode(textView: TextView, diagnosisCode: String) {
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

@BindingAdapter(value = ["imageUri"], requireAll = false)
fun setImageUrl(imageView: ImageView, url: Uri?) {

    GlideApp.with(imageView).load(url).placeholder(R.mipmap.img_electrocardiogram).into(imageView)
}

@BindingAdapter(value = ["imageUrl"], requireAll = false)
fun setImageUrl(imageView: ImageView, url: String?) {

    GlideApp.with(imageView).load(url).placeholder(R.mipmap.img_electrocardiogram).into(imageView)
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
        return if(value.isEmpty()) 0 else value.toInt()
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