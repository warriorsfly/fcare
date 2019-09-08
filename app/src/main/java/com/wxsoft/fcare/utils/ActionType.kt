package com.wxsoft.fcare.utils

import androidx.annotation.StringDef

@Retention(AnnotationRetention.SOURCE)
@StringDef( ActionType.绑定腕带, ActionType.病情评估, ActionType.辅助检查, ActionType.GRACE,
    ActionType.给药,ActionType.ACS给药, ActionType.溶栓处置, ActionType.绕行导管室, ActionType.心电图, ActionType.生命体征,
    ActionType.导管室完成准备, ActionType.CABG, ActionType.院前诊断, ActionType.消息通知, ActionType.知情同意书,
    ActionType.CT, ActionType.诊断, ActionType.启动导管室, ActionType.辅助检查, ActionType.GRACE, ActionType.患者,
    ActionType.交接单, ActionType.患者列表, ActionType.患者信息录入, ActionType.患者转归,ActionType.治疗方案,
    ActionType.接受通知, ActionType.PCI,ActionType.StartVehicle,ActionType.PhysicalExamination,ActionType.DispostionMeasures,
    ActionType.IllnessHistory,ActionType.到达导管室, ActionType.激活导管室,ActionType.主诉及症状,ActionType.治疗策略,ActionType.治疗操作,
    ActionType.通知启动导管室,ActionType.通知启动CT室, ActionType.来院方式,ActionType.Catheter,
    ActionType.CT_OPERATION,ActionType.出院诊断,ActionType.一键通知,ActionType.肌钙蛋白,ActionType.BLOOD
    ,ActionType.BLOODPRESSURE,ActionType.PGB,ActionType.胸痛诊断
)

annotation class ActionType {
    companion object {
        const val 绑定腕带 = "bdwd"
        const val 病情评估 = "bqpg"
        const val CABG = "cabg"
        const val 院前诊断 = "zd"
        const val CT = "ct"
        const val 诊断 = "zd"
        const val 出院诊断 = "cyzd"
        const val 启动导管室 = "qddgs"
        const val 辅助检查 = "fzjc"
        const val GRACE = "gracepf"
        const val 患者 = "hz"
        const val 患者转归 = "hzzg"
        const val 交接单 = "hzjjd"
        const val 患者列表 = "hzlb"
        const val 患者信息录入 = "hzxxlr"
        const val 激活导管室 = "jhdgs"
        const val 到达导管室 = "dddgs"
        const val 接受通知 = "jstz"
        const val PCI = "pci"
        const val 溶栓处置 = "rscz"
        const val 绕行导管室 = "rx"
        const val 生命体征 = "smtz"
        const val 导管室完成准备 = "wczb"
        const val 心电图 = "xdt"
        const val 消息通知 = "xxtz"
        const val 来院方式 = "lyfs"
        const val 知情同意书 = "informed_consent_statement"
        const val 给药 = "hzgy"
        const val ACS给药 = "acsgy"
        //发车
        const val StartVehicle = "start_vehicle"
        //体格检查
        const val PhysicalExamination = "physical_examination"
        //病史
        const val IllnessHistory = "illness_history"
        //处置措施
        const val DispostionMeasures = "disposition_measures"
        //导管室操作
        const val Catheter = "dgscz"
        const val CT_OPERATION = "ctscz"
        const val 治疗方案 = "zlfa"
        const val 通知启动CT室 = "tzqdcts"
        const val 通知启动导管室 = "tzqddgs"
        const val 主诉及症状= "cc"
        const val 治疗策略= "zlcl"
        const val 治疗操作= "zlcz"
        const val 一键通知= "yjtz"
        const val 肌钙蛋白= "jgdb"
        const val BLOOD= "cx"
        const val BLOODPRESSURE= "xyjc"
        const val PGB= "pgb"
        const val 胸痛诊断= "xtzd"
        const val 介入详情= "jrxq"

    }
}
