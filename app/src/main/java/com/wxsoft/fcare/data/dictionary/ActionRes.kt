package com.wxsoft.fcare.data.dictionary

import android.support.annotation.StringDef
import com.wxsoft.fcare.R


class ActionRes {

    //为了容易修改，添加item的时候按a-z排序
    companion object {
        //a-g 可以 分多行，但是不要和h以及之后的放在一起

        val ActionIcons = mapOf(
            ActionType.救护车 to R.drawable.ic_menu_120_car,
            ActionType.绑定腕带 to R.drawable.ic_menu_bdwd,
            ActionType.病情评估 to R.drawable.ic_menu_bqpg,
            ActionType.辅助检查 to R.drawable.ic_menu_fzjc,
            ActionType.GRACE to R.drawable.ic_menu_grace,
            ActionType.给药 to R.drawable.ic_menu_hzgy,
            ActionType.溶栓处置 to R.drawable.ic_menu_rsss,
            ActionType.绕行导管室 to R.drawable.ic_menu_lstd,
            ActionType.生命体征 to R.drawable.ic_menu_smtz,
            ActionType.消息通知 to R.drawable.ic_menu_xxtz,
            ActionType.心电图 to R.drawable.ic_menu_xdt,
            ActionType.交接单 to R.drawable.ic_menu_jjxx,
            ActionType.PCI to R.drawable.ic_menu_pci,
            ActionType.初步诊断 to R.drawable.ic_menu_cbzd,
            ActionType.知情同意书 to R.drawable.ic_menu_zqtys,
            ActionType.出院诊断 to R.drawable.ic_menu_cyzd,
            ActionType.患者转归 to R.drawable.ic_menu_hzzg,
            ActionType.到达导管室 to R.drawable.ic_menu_jhdgs,
            ActionType.启动导管室 to R.drawable.ic_menu_jhdgs,
            ActionType.来院方式 to R.drawable.ic_directions_car_black_24dp,
            ActionType.激活导管室 to R.drawable.ic_menu_jhdgs
        )
    }

    @Retention(AnnotationRetention.SOURCE)
    @StringDef(
        ActionType.救护车, ActionType.绑定腕带, ActionType.病情评估, ActionType.辅助检查, ActionType.GRACE,
        ActionType.给药, ActionType.溶栓处置, ActionType.绕行导管室, ActionType.心电图, ActionType.生命体征,
        ActionType.导管室完成准备, ActionType.CABG, ActionType.初步诊断, ActionType.消息通知, ActionType.知情同意书,
        ActionType.CT, ActionType.出院诊断, ActionType.启动导管室, ActionType.辅助检查, ActionType.GRACE, ActionType.患者,
        ActionType.交接单, ActionType.患者列表, ActionType.患者信息录入, ActionType.患者转归,
        ActionType.接受通知, ActionType.PCI,ActionType.StartVehicle,ActionType.PhysicalExamination,ActionType.DispostionMeasures,ActionType.IllnessHistory,ActionType.到达导管室, ActionType.激活导管室, ActionType.来院方式
    )
    annotation class ActionType {
        companion object {

            @Deprecated(message = "replace  with StartVehicle")
            const val 救护车 = "120"
            const val 绑定腕带 = "bdwd"
            const val 病情评估 = "bqpg"
            const val CABG = "cabg"
            const val 初步诊断 = "cbzd"
            const val CT = "ct"
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
            //发车
            const val StartVehicle = "start_vehicle"
            //体格检查
            const val PhysicalExamination = "physical_examination"
            //病史
            const val IllnessHistory = "illness_history"
            //处置措施
            const val DispostionMeasures = "disposition_measures"
        }
    }
}
