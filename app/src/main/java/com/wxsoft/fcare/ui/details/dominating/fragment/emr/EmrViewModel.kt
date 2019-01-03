package com.wxsoft.fcare.ui.details.dominating.fragment.emr

import com.google.gson.Gson
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.ui.BaseViewModel
import javax.inject.Inject

class EmrViewModel @Inject constructor(override val sharedPreferenceStorage: SharedPreferenceStorage,
                                       gson: Gson
) : BaseViewModel(sharedPreferenceStorage,gson) {

}
