package com.jmnetwork.e_jartas.utils

import android.content.Context
import android.content.SharedPreferences

class MySharedPreferences(context: Context) {

    companion object {
        const val USER_PREF = "USER_PREF"
    }

    private val mSharedPreferences = context.getSharedPreferences(USER_PREF, Context.MODE_PRIVATE)

    fun clear() {
        mSharedPreferences.edit().clear().apply()
    }

    fun setValue(key: String, value: String) {
        mSharedPreferences.edit().putString(key, value).apply()
    }

    fun setValueInteger(key: String, value: Int) {
        mSharedPreferences.edit().putInt(key, value).apply()
    }

    fun getValue(key: String): String? {
        return mSharedPreferences.getString(key, "")
    }

    fun getValueInteger(key: String): Int {
        return mSharedPreferences.getInt(key, 0)
    }
}