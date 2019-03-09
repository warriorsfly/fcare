package com.wxsoft.fcare.core.data.entity.rating

import com.google.gson.annotations.SerializedName

/**
 * 评分题
 */
data class Subject(val id:String,
                   @SerializedName("scoreSheetId")val ratingId:String,
                   val name:String,
                   @SerializedName("sortNum")
                   val index:Int,
                   val subjectType:String){
    @SerializedName("subjectOptions")
    var options:List<Option> =  emptyList()

    fun check(option: Option){

        //单选，先更新旧的选中的选项为未选中
        if(subjectType=="220-1" ) {
            if(options.size==1){
                option.checked = !option.checked
            }else {
                if (!option.checked) {
                    options.firstOrNull { it.checked }?.checked=false
                    option.checked = !option.checked
                }
            }

        }else {
            option.checked = !option.checked
        }
    }
}