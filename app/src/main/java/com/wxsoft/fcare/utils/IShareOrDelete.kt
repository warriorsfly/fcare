package com.wxsoft.fcare.utils

interface IShareOrDelete{
    /**
     * 考虑实体接口的问题，这里可以传id，只是id可以为空
     */
    fun showImageDialog( url:String,id:String="",fix: Boolean=false)
    fun share(url:String)
    fun delete(id:String,fix:Boolean)
}