package com.rabadiya.base.utils

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.Q)
fun isQAndAbove() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.O)
fun isOAndAbove() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O