package com.wxsoft.fcare.ui.photo

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.luck.picture.lib.photoview.OnPhotoTapListener
import com.wxsoft.fcare.R
import kotlinx.android.synthetic.main.item_pic_show.view.*

class PopAdapter(var look_imgs: Array<String>) : PagerAdapter(){
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return `object` === view
    }
    override fun getCount(): Int = look_imgs.size

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        ( container as ViewPager).removeView(`object` as View)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view= View.inflate(container.context, R.layout.item_pic_show,null)
        Glide.with(container.context).load(look_imgs[position]).into(view.pic_photoview)
        view.pic_photoview.setOnPhotoTapListener(object : OnPhotoTapListener {
            override fun onPhotoTap(p0: ImageView?, p1: Float, p2: Float) {
                (container.context as PhotoActivity).supportFinishAfterTransition()
            }
        })
        (container as ViewPager).addView(view)
        return view
    }


}