package com.wxsoft.fcare.core.data.prefs

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.WorkerThread
import javax.inject.Inject
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

interface PreferenceStorage {
    var onboardingCompleted: Boolean
    var endPointChanged: Boolean
    var loginedName: String?
    var loginedPassword: String?
    var userInfo:String?
    var registrationId:String?
    var endPointIndex:Int
}

/**
 * [PreferenceStorage] impl backed by [android.content.SharedPreferences].
 */
class SharedPreferenceStorage @Inject constructor(context: Context) :
        PreferenceStorage {


    companion object {
        const val PREFS_NAME = "fcare"
        const val PREFS_JPUSH_REGISTRATION_ID = "pref_jpush_registration_id"
        const val PREF_ENDPOINT_INDEX = "pref_endpoint_index"
        const val PREF_ENDPOINT_CHANGED = "pref_endpoint_changed"
        const val PREF_ONBOARDING = "pref_onboarding"
        const val PREF_USERNAME = "pref_user_name"
        const val PREF_PASSWORD = "pref_password"
        const val PREF_USER_INFO = "pref_user_info"
    }
    private val prefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

//    init {
//        prefs.registerOnSharedPreferenceChangeListener { sharedPreferences, key ->
//            if(key==PREF_ENDPOINT_INDEX){
//                endPointChanged=true
//            }
//        }
//    }
//    fun registerOnPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
//        prefs.registerOnSharedPreferenceChangeListener(listener)
//    }

    override var onboardingCompleted by BooleanPreference(prefs, PREF_ONBOARDING, false)
    override var endPointChanged   by BooleanPreference(prefs, PREF_ENDPOINT_CHANGED, false)

    override var loginedName by StringPreference(prefs, PREF_USERNAME,"")

    override var loginedPassword by StringPreference(prefs, PREF_PASSWORD,"")

    override var userInfo by StringPreference(prefs, PREF_USER_INFO,"")
    override var registrationId by StringPreference(prefs, PREFS_JPUSH_REGISTRATION_ID,"")
    override var endPointIndex  by IntPreference(prefs, PREF_ENDPOINT_INDEX,-1)

}


class BooleanPreference(
        private val preferences: SharedPreferences,
        private val name: String,
        private val defaultValue: Boolean
) : ReadWriteProperty<Any, Boolean> {

    @WorkerThread
    override fun getValue(thisRef: Any, property: KProperty<*>): Boolean {
        return preferences.getBoolean(name, defaultValue)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Boolean) {
        preferences.edit().apply { putBoolean(name, value).apply()}
    }
}

class StringPreference(
        private val preferences: SharedPreferences,
        private val name: String,
        private val defaultValue: String?
) : ReadWriteProperty<Any, String?> {

    @WorkerThread
    override fun getValue(thisRef: Any, property: KProperty<*>): String? {
        return preferences.getString(name, defaultValue)
    }
    override fun setValue(thisRef: Any, property: KProperty<*>, value: String?) {
        preferences.edit().apply {  putString(name, value).apply()}
    }
}


class IntPreference(
    private val preferences: SharedPreferences,
    private val name: String,
    private val defaultValue: Int
) : ReadWriteProperty<Any, Int> {

    @WorkerThread
    override fun getValue(thisRef: Any, property: KProperty<*>): Int {
        return preferences.getInt(name, defaultValue)
    }
    override fun setValue(thisRef: Any, property: KProperty<*>, value: Int) {
        preferences.edit().apply {  putInt(name, value).apply()}

    }
}