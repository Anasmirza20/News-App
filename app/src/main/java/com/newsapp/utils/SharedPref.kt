package com.newsapp.utils

import android.content.Context
import android.content.SharedPreferences
import com.newsapp.utils.Constants.SHARED_PREF_KEY

class SharedPref(context: Context?) {
    private val sharedPreferences: SharedPreferences? =
        context?.getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE)

    private val editor = sharedPreferences?.edit()

    fun putString(key: String, value: String) {
        editor?.putString(key, value)
        editor?.apply()
    }

    fun putInt(key: String, value: Int) {
        editor?.putInt(key, value)
        editor?.apply()
    }

    fun putBoolean(key: String, value: Boolean) {
        editor?.putBoolean(key, value)
        editor?.apply()
    }

    fun getString(key: String) = sharedPreferences?.getString(key, null)
    fun getInt(key: String) = sharedPreferences?.getInt(key, 0)
    fun getBoolean(key: String) = sharedPreferences?.getBoolean(key, false)
}