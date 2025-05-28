package com.rabadiya.base.utils

object CommonUtils {
    fun buildString(vararg values: Any?): String {
        return buildString {
            values.forEach { value ->
                append(value)
            }
        }
    }

    fun addSpace(): String {
        return  " "
    }
}