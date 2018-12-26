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

package com.wxsoft.fcare.ui.detail.fragment.timeline

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.State
import android.text.Layout
import android.text.SpannableStringBuilder
import android.text.StaticLayout
import android.text.TextPaint
import android.view.View
import com.wxsoft.fcare.R
import com.wxsoft.fcare.data.entity.EmrLog
import com.wxsoft.fcare.utils.withTranslation

/**
 * A [RecyclerView.ItemDecoration] which draws sticky headers marking the days in a given list of
 * [Block]s. It also inserts gaps between days.
 */
class ItemDecoration(
        context: Context,
        items:List<EmrLog>
) : RecyclerView.ItemDecoration() {

    private val drawableEnd: Drawable = context.resources.getDrawable(R.drawable.bg_vertical_line_end,context.theme)
    private val drawable: Drawable = context.resources.getDrawable(R.drawable.bg_vertical_line,context.theme)
    private val paddingTop: Int
    private val paddingLeft: Int
    private val width: Int
    private val paint: TextPaint
    init {
        val attrs = context.obtainStyledAttributes(
                R.style.Widget_Emergency_ProcessHeaders,
                R.styleable.EmrItemHeader
        )

        paddingTop = attrs.getDimensionPixelSize(R.styleable.TimeHeader_android_paddingTop,0)
        paddingLeft = attrs.getDimensionPixelSize(R.styleable.TimeHeader_android_paddingTop,0)
        width = attrs.getDimensionPixelSize(R.styleable.TimeHeader_android_width,0)
        paint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = attrs.getColor(R.styleable.TimeHeader_android_textColor,0)
            textSize = attrs.getDimension(R.styleable.EmrItemHeader_timeTextSize,10f)
            try {
                typeface = ResourcesCompat.getFont(
                    context,
                    attrs.getResourceId(R.styleable.TimeHeader_android_fontFamily,0)
                )
            } catch (_: Exception) {
                // ignore
            }
        }
    }

    // Get the emr index:usercase and create header layouts for each
    private val drawableSlots: Map<Int, DrawableLayout> =
            indexLineTimeDrawable(items).map {
                it.first to createHeader(it.second)
            }.toMap()

    /**
     *  Add gaps between days, split over the last and first block of a day.
     */
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: State) {
        val position = parent.getChildAdapterPosition(view)
        if (position <= 0) return

        if (drawableSlots.containsKey(position)) {
            // first block of day, pad top
            outRect.top =  paddingTop

            outRect.right=paddingTop
            outRect.bottom = paddingTop
        } else if (drawableSlots.containsKey(position + 1)) {
            // last block of day, pad bottom
            outRect.right=paddingTop
            outRect.bottom = paddingTop
        }

    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: State) {
        if (drawableSlots.isEmpty() || parent.childCount==0) return
        var prevHeaderTop = Int.MAX_VALUE
        for (i in parent.childCount - 1 downTo 0) {
            val view = parent.getChildAt(i)
            val viewTop = view.top + view.translationY.toInt()
            val position = parent.getChildAdapterPosition(view)
            drawableSlots[position]?.let { layout ->

                var rect =  Rect(0, 0, 0, 0);

                rect.top = view.top-paddingTop;
                rect.bottom = view.bottom+paddingTop;
                rect.left = width-layout.drawable.intrinsicWidth;
                rect.right = width;
                layout.drawable.bounds=rect
                layout.drawable.draw(c)


                val top = (viewTop)
                    .coerceAtMost(prevHeaderTop - layout.layout.height)

                c.withTranslation(y = top.toFloat()+((view.height-layout.layout.height)/2).toFloat(),x= paddingLeft.toFloat()) {
                    layout.layout.draw(c)
                }

                prevHeaderTop = viewTop
            }
        }

    }


    private fun createHeader(lineDrawable: TimeLineDrawable):DrawableLayout{
        val day=lineDrawable.time.substring(0,10)
        var time =lineDrawable.time.substring(11,19)
        val text = SpannableStringBuilder(day).apply {
            append(System.lineSeparator())
            append(time)
        }
        val line=if(lineDrawable.end)drawableEnd else drawable
        val layout = StaticLayout(text, paint, width-line.intrinsicWidth-paddingLeft, Layout.Alignment.ALIGN_CENTER, 1f, 0f, true)
        return DrawableLayout(layout,if(lineDrawable.end)drawableEnd else drawable)
    }
}
