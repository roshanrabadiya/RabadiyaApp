package com.rabadiya.base.utils

import android.text.InputFilter
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged

object CommonUtils {
    private var isFormatting = false

    fun buildString(vararg values: Any?): String {
        return buildString {
            values.forEach { value ->
                append(value)
            }
        }
    }

    fun addSpace(): String {
        return " "
    }

    fun formatMobileNumberView(view: View) {
        if (view is EditText) {
            view.doAfterTextChanged { s ->
                if (isFormatting) return@doAfterTextChanged

                isFormatting = true

                val digits = s.toString().replace(" ", "").take(10)
                val formatted = when {
                    digits.length > 5 -> "${digits.substring(0, 5)} ${digits.substring(5)}"
                    else -> digits
                }

                view.setText(formatted)
                view.setSelection(formatted.length)

                isFormatting = false
            }

            // Limit input length for EditText only
            view.filters = arrayOf(InputFilter.LengthFilter(11))

        } else if (view is TextView) {
            val digits = view.text.toString().replace(" ", "").take(10)
            val formatted = when {
                digits.length > 5 -> "${digits.substring(0, 5)} ${digits.substring(5)}"
                else -> digits
            }
            view.text = formatted
        }
    }

}