package com.wxsoft.fcare.utils

import androidx.annotation.IntDef

@Retention(AnnotationRetention.SOURCE)
@IntDef( ActionCode.ARG_NEW_ITEM_CODE, ActionCode.MEDICAL_HISTORY_CODE, ActionCode.VITAL_SIGNS, ActionCode.CHECK_BODY,
    ActionCode.DIAGNOSE, ActionCode.MEASURES, ActionCode.INV, ActionCode.Catheter, ActionCode.CT,
    ActionCode.DISCHARGE, ActionCode.OUTCOME, ActionCode.INFORMEDCONSENT, ActionCode.THROMBOLYSIS, ActionCode.DRUGRECORD,
    ActionCode.OTDIAGNOSE, ActionCode.CT_OPERATION, ActionCode.RATING, ActionCode.CABG, ActionCode.BASE_INFO, ActionCode.COMPLAINTS,
    ActionCode.STRATEGY, ActionCode.NOTIFICATION, ActionCode.RIS_LIS,ActionCode.ECG
)

annotation class ActionCode {
    companion object {
        const val ARG_NEW_ITEM_CODE = 20
        /**
         * 药史
         */
        const val MEDICAL_HISTORY_CODE = 21
        /**
         * 生命体征
         */
        const val VITAL_SIGNS = 22
        /**
         * 查体
         */
        const val CHECK_BODY = 23
        /**
         * 诊断
         */
        const val DIAGNOSE = 24
        /**
         * 处置
         */
        const val MEASURES = 25
        /**
         *
         */
        const val INV = 26
        /**
         *
         */
        const val Catheter = 27
        /**
         *
         */
        const val CT = 28
        /**
         *
         */
        const val DISCHARGE = 29
        /**
         * 出院
         */
        const val OUTCOME = 30
        /**
         *
         */
        const val INFORMEDCONSENT = 31
        /**
         *
         */
        const val THROMBOLYSIS = 32
        /**
         *
         */
        const val DRUGRECORD = 33
        /**
         *
         */
        const val OTDIAGNOSE = 34
        /**
         *
         */
        const val CT_OPERATION = 35
        /**
         *
         */
        const val RATING = 36
        /**
         *
         */
        const val CABG = 37
        /**
         *
         */
        const val BASE_INFO = 38
        /**
         *
         */
        const val COMPLAINTS = 39
        /**
         *
         */
        const val STRATEGY = 40
        /**
         *
         */
        const val NOTIFICATION = 41
        /**
         *
         */
        const val RIS_LIS = 42
        const val ECG = 43
        const val EMR = 44
    }
}
