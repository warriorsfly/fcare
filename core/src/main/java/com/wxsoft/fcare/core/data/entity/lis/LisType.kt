package com.wxsoft.fcare.core.data.entity.lis

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.wxsoft.fcare.core.BR

data class LisType(val reportNo:String?,
                   val jylbmc:String,
                   val cyrq:String?,
                   val  fbrq:String?,
                   val lisReoprtaRecordDates:List<LisRecord>?)