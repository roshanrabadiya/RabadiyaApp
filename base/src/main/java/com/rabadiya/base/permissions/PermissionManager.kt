package com.rabadiya.base.permissions

import android.content.Intent


interface PermissionManager {

    fun hasCameraPermission(): Boolean

    fun openPermissionSetting() : Intent

}