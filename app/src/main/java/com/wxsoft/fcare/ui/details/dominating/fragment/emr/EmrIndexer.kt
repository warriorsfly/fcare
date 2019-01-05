package com.wxsoft.fcare.ui.details.dominating.fragment.emr

import com.wxsoft.fcare.core.data.entity.EmrItem

/**
 * Find the first block of each day (rounded down to nearest day) and return pairs of
 * index to start time. Assumes that [items] are sorted by ascending start time.
 */
//fun indexHeader(items: List<EmrItem>): List<Pair<Int, String>> {
//    return items.mapIndexed { index,t->  Pair(index,t.userCase) }
//            .distinctBy { it.second }
//}

fun indexEmr(items: List<EmrItem>): List<Triple<Int, Boolean,String?>> {


    return items.mapIndexed { index,e->

        return@mapIndexed Triple(index,e.done,e.complatedAt?.substring(11,16))
    }
}