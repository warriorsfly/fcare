/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wxsoft.fcare.ui.detail.fragment.timeline

import com.wxsoft.fcare.data.entity.EmrLog

/**
 * Find the first block of each day (rounded down to nearest day) and return pairs of
 * index to start time. Assumes that [items] are sorted by ascending start time.
 */
//fun indexHeader(items: List<EmrItem>): List<Pair<Int, String>> {
//    return items.mapIndexed { index,t->  Pair(index,t.userCase) }
//            .distinctBy { it.second }
//}

fun indexLineTimeDrawable(items: List<EmrLog>): List<Pair<Int, TimeLineDrawable>> {


    return items.mapIndexed { index,e->

        return@mapIndexed Pair(index,TimeLineDrawable(e.createdDate,index==items.size-1))
    }
}