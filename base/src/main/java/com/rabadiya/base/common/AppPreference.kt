package com.rabadiya.base.common

import android.content.Context
import android.content.SharedPreferences
import com.rabadiya.base.utils.APP_PREFERENCE_NAME

class AppPreference(
    private val context: Context
) {

    private val sharedPreferences : SharedPreferences by lazy {
        context.getSharedPreferences(APP_PREFERENCE_NAME, Context.MODE_PRIVATE)
    }

    // Save String Data
    fun saveData(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun getStringData(key: String): String {
        return sharedPreferences.getString(key, "") ?: ""
    }

    // Save Integer Data
    fun saveData(key: String, value: Int) {
        sharedPreferences.edit().putInt(key, value).apply()
    }

    fun getIntData(key: String, defaultValue: Int): Int {
        return sharedPreferences.getInt(key, defaultValue)
    }

    // Save Boolean Data

    fun saveData(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    fun getBooleanData(key: String, defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    // Save Float Data

    fun saveData(key: String, value: Float) {
        sharedPreferences.edit().putFloat(key, value).apply()
    }

    fun getFloatData(key: String, defaultValue: Float): Float {
        return sharedPreferences.getFloat(key, defaultValue)
    }

    // Save Long Data
    fun saveData(key: String, value: Long) {
        sharedPreferences.edit().putLong(key, value).apply()
    }

    fun getLongData(key: String, defaultValue: Long): Long {
        return sharedPreferences.getLong(key, defaultValue)
    }

    // Remove Data
    fun removeData(key: String) {
        sharedPreferences.edit().remove(key).apply()
    }

    // Clear All Data
    fun clearAllData() {
        sharedPreferences.edit().clear().apply()
    }
}