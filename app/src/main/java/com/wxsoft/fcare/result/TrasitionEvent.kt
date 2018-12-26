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

package com.wxsoft.fcare.result

import android.arch.lifecycle.Observer
import android.view.View

/**
 * Used as a wrapper for liveDiagnosis that is exposed via a LiveData that represents an event.
 */
open class TrasitionEvent<out T>(private val view: View, private val content: T) {

    var hasBeenHandled = false
        private set // Allow external read but not write

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotHandled(): Pair<View,T>? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            Pair(view,content)
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T = content
}

/**
 * An [Observer] for [Event]s, simplifying the pattern of checking if the [Event]'s content has
 * already been handled.
 *
 * [onEventUnhandledContent] is *only* called if the [Event]'s contents has not been handled.
 */
class TransitionEventObserver<T>(private val onEventUnhandledContent: (View,T) -> Unit) : Observer<TrasitionEvent<T>> {
    override fun onChanged(event: TrasitionEvent<T>?) {
        event?.getContentIfNotHandled()?.let {
            onEventUnhandledContent(it.first,it.second)
        }
    }
}
