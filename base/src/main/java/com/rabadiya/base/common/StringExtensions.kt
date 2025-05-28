package com.rabadiya.base.common

import android.content.Context
import android.widget.EditText

fun Context.getStrings(resId: Int): String {
    return resources.getString(resId)
}

fun EditText.sensitizedString(): String {
    return this.text.toString().trim()
}