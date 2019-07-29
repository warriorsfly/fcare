package com.wxsoft.fcare.utils

import androidx.annotation.StringDef

@Retention(AnnotationRetention.SOURCE)
@StringDef(
    TimingType.HelpAt,
    TimingType.ArriveEmergency,
    TimingType.ArriveCcu,
    TimingType.OutHospitalVisit,
    TimingType.Transfer,
    TimingType.AmbulanceArrived,
    TimingType.LeaveOutHospital,
    TimingType.ArriveHospital,
    TimingType.InHospitalAdmission,
    TimingType.ArriveScene,
    TimingType.Consultation,
    TimingType.LeaveDepartment
)

annotation class TimingType {
    companion object {
        /**
         * 呼叫120时间
         */
        const val HelpAt = "HelpAt"
        /**
         * 急诊医生到达时间
         */
        const val ArriveEmergency = "ArriveEmergency"
        /**
         * 到达CCU时间
         */
        const val ArriveCcu = "ArriveCcu"
        /**
         * 到达转院医院时间
         */
        const val OutHospitalVisit = "OutHospitalVisit"
        /**
         * 决定转院时间
         */
        const val Transfer = "Transfer"
        /**
         * 救护车到达时间
         */
        const val AmbulanceArrived = "AmbulanceArrived"
        /**
         * 离开转院时间
         */
        const val LeaveOutHospital = "LeaveOutHospital"

        const val ArriveScene = "ArriveScene"
        /**
         * 到达本院大门时间
         */
        const val ArriveHospital = "ArriveHospital"
        /**
         *院内首诊
         */
        const val InHospitalAdmission = "InHospitalAdmission"
        /**
         *
         */
        const val Consultation = "Consultation"
        /**
         * 离开科室时间
         */
        const val LeaveDepartment = "LeaveDepartment"
        /**
         * 绕行急诊到达场所时间
         */
        const val PassingArriveEmergency= "PassingArriveEmergency"
        /**
         * 离开时间
         */
        const val PassingLeaveEmergency= "PassingLeaveEmergency"
        /**
         * 急诊绕行CCU到达导管室时间
         */
        const val PassingArriveCCU = "PassingArriveCCU"

    }
}
