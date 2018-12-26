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

import android.app.TimePickerDialog
import android.databinding.BindingAdapter
import android.databinding.InverseMethod
import android.support.design.widget.FloatingActionButton
import android.support.v4.view.ViewPager
import android.view.View
import android.view.View.*
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TimePicker
import com.wxsoft.fcare.data.dictionary.ActionRes
import java.util.*

@BindingAdapter("invisibleUnless")
fun invisibleUnless(view: View, visible: Boolean) {
    view.visibility = if (visible) VISIBLE else INVISIBLE
}

@BindingAdapter("goneUnless")
fun goneUnless(view: View, visible: Boolean) {
    view.visibility = if (visible) VISIBLE else GONE
}

@BindingAdapter("fabVisibility")
fun fabVisibility(fab: FloatingActionButton, visible: Boolean) {
    if (visible) fab.show() else fab.hide()
}

@BindingAdapter("pageMargin")
fun pageMargin(viewPager: ViewPager, pageMargin: Float) {
    viewPager.pageMargin = pageMargin.toInt()
}


@BindingAdapter("actionCode")
fun loadImage(view: ImageView, code: String) {
    if(ActionRes.ActionIcons.keys.contains(code)) {
        view.setImageResource(ActionRes.ActionIcons[code]!!)
    }
}


@BindingAdapter("timeOnClick")
fun textTime(button: Button, listener: View.OnClickListener){

    var ca = Calendar.getInstance()
    var mYear = ca.get(Calendar.YEAR)
    var mMonth = ca.get(Calendar.MONTH)
    var mDay = ca.get(Calendar.DAY_OF_MONTH)
    var mHour = ca.get(Calendar.HOUR_OF_DAY)
    var mMinute = ca.get(Calendar.MINUTE)

    val timeDialog= TimePickerDialog(button.context, TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
        val time=StringBuffer().append(String.format("%02d", hourOfDay)).append(":").append(String.format("%02d", minute)).toString()
        button.setText(time)
    },mHour,mMinute,true)
    timeDialog.show()
}

object Converter{
    @InverseMethod("stringToInt")
    @JvmStatic fun intToString(value: Int): String {
        return if(value==0) ""  else value.toString()
    }

    @JvmStatic fun stringToInt(value:String): Int {
        return if(value.isEmpty()) 0 else value.toInt()
    }
}


