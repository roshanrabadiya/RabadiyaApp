package com.rabadiya.base.common

import android.content.Context
import android.widget.EditText

fun Context.getStrings(resId: Int, vararg formatArgs: Any): String {
    return resources.getString(resId, *formatArgs)
}

fun EditText.sensitizedString(): String {
    return this.text.toString().trim()
}