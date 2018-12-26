package com.wxsoft.fcare.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Environment
import android.support.v4.content.res.ResourcesCompat
import android.text.Layout
import android.text.SpannableStringBuilder
import android.text.StaticLayout
import android.text.TextPaint
import android.util.Log
import com.wxsoft.fcare.R
import java.io.File
import java.io.FileOutputStream


class ShareShotUtils constructor(private val context: Context, private val filename: String, texts: Array<Pair<String,Array<Pair<String,String>>>>){

    private val padding: Int
    private val paddingTitle: Int
    private val widthContent: Int
    private val widthTitle: Int
    private val widthProperty: Int
    private val paintContentNormal: TextPaint
    private val paintPropertyName: TextPaint
    private val paintTitle: TextPaint

    init {
        val attrsHeader = context.obtainStyledAttributes(
            R.style.Widget_FCare_ShareHeaders,
            R.styleable.ShareInfo
        )

        val attrsContent = context.obtainStyledAttributes(
            R.style.Widget_FCare_ShareContent,
            R.styleable.ShareContent
        )



        padding = attrsHeader.getDimensionPixelSize(R.styleable.TimeHeader_android_paddingTop,0)
        paddingTitle = 2*padding
        widthTitle  = attrsHeader.getDimensionPixelSize(R.styleable.ShareInfo_android_width,0)
        widthProperty = attrsHeader.getDimensionPixelSize(R.styleable.ShareInfo_android_width,0)
        widthContent = attrsContent.getDimensionPixelSize(R.styleable.ShareContent_android_width,0)
        paintContentNormal = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = attrsHeader.getColor(R.styleable.TimeHeader_android_textColor,0)
            textSize = attrsHeader.getDimension(R.styleable.EmrItemHeader_userCaseTextSize,10f)
            try {
                typeface = ResourcesCompat.getFont(
                    context,
                    attrsHeader.getResourceId(R.styleable.TimeHeader_android_fontFamily,0)
                )
            } catch (_: Exception) {
                // ignore
            }
        }

        paintPropertyName = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = attrsHeader.getColor(R.styleable.TimeHeader_android_textColor,0)
            textSize = attrsHeader.getDimension(R.styleable.EmrItemHeader_userCaseTextSize,10f)
            try {
                typeface = ResourcesCompat.getFont(
                    context,
                    attrsHeader.getResourceId(R.styleable.TimeHeader_android_fontFamily,0)
                )
            } catch (_: Exception) {
                // ignore
            }
        }

        paintTitle = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = attrsHeader.getColor(R.styleable.TimeHeader_android_textColor,0)
            textSize = attrsHeader.getDimension(R.styleable.EmrItemHeader_userCaseTextSize,10f)
            try {
                typeface = ResourcesCompat.getFont(
                    context,
                    attrsHeader.getResourceId(R.styleable.TimeHeader_android_fontFamily,0)
                )
            } catch (_: Exception) {
                // ignore
            }
        }
    }

    private val shareLayouts= texts.map{
        Pair(createLayout(paintTitle,it.first,widthTitle),
            it.second.map { item-> Pair(createLayout(paintPropertyName,item.first,widthProperty),
                createLayout(paintContentNormal,item.second,widthContent)) })
    }


    private fun  drawBitmap():Bitmap{

        val xPos=padding+0f
        var yPos=padding+0f

        val bitmap = Bitmap.createBitmap(widthTitle+widthContent+padding*4, widthTitle+widthContent+padding*4, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(bitmap)
        canvas.withTranslation(x=xPos,y=yPos) {

            shareLayouts.forEach {

                it.first.draw(canvas)

                canvas.withTranslation(x=xPos,y=it.first.height+xPos) {

                    it.second.forEach { pair ->

                        pair.first.draw(canvas)

                        canvas.translate( pair.first.width + xPos, 0f)
                        pair.second.draw(canvas)
                        canvas.translate(-1*(pair.first.width + xPos),pair.first.height + xPos)
                    }
                }
            }

        }


        return bitmap
    }

    private fun getDir(context: Context, filename: String): File? {

        // Get the directory for the app's private pictures directory.
        val dic = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "fcare")
        if (!dic?.mkdirs()) {
            Log.e("分享", "文件夹未创建")
        }
        return File(dic.path,filename)
    }

    fun save():Pair<String?,Bitmap>{

        val file=getDir(context,filename)

        val bitmap=drawBitmap()
        val fos=FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        fos.flush()
        return Pair(file?.absolutePath,bitmap)
    }

    private fun createLayout(paint: TextPaint, item: String, width:Int):StaticLayout{
        val text = SpannableStringBuilder(item)
        return StaticLayout(text, paint, width +2* padding, Layout.Alignment.ALIGN_CENTER, 1f, 0f, true)
    }



}