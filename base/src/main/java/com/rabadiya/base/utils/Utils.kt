package com.rabadiya.base.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Parcelable
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.rabadiya.base.customviews.customtextview.CommonEditText
import com.rabadiya.base.customviews.customtextview.CommonTextView
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

const val TAG = "Roshan"

var androidDeviceId : String? = null

fun <T> tryOrNull(logOnError: Boolean = true, body: () -> T?): T? {
    return try {
        body()
    } catch (e: Exception) {
        if (logOnError) {
            Log.w(TAG, e)
        }
        null
    }
}

fun Context.getColorCompat(colorRes: Int): Int {
    //return black as a default color, in case an invalid color ID was passed in
    return tryOrNull { ContextCompat.getColor(this, colorRes) } ?: Color.BLACK
}

@Suppress("DEPRECATION")
inline fun <reified P : Parcelable> Intent.getParcelableExtraCompat(key: String): P? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getParcelableExtra(key, P::class.java)
    } else {
        getParcelableExtra(key)
    }
}

fun Context.showGlideImage(data: Int, view: ImageView) {
    Glide.with(this)
        .load(data)
        .into(view)
}

fun Context.dimensionPixelSize(res: Int) {
    resources.getDimensionPixelSize(res)
}

@SuppressLint("HardwareIds")
fun Context.fetchDeviceId(): String {
    return if (isQAndAbove()) {
        // Android 10 and above
        Settings.Secure.getString(
            contentResolver,
            Settings.Secure.ANDROID_ID
        )
    }else if (isOAndAbove()) {
        // Android 8.0 (API 26) to Android 9.0 (API 28)
        Build.getSerial()
    }else {
        // Below Android 8.0
        @Suppress("DEPRECATION")
        Build.SERIAL
    }
}

fun CommonEditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // Not needed
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // Not needed
        }

        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable?.toString() ?: "")
        }
    })
}


fun CommonEditText.isStringValid(): Boolean {
    return text.toString().isNotEmpty()
}

fun CommonTextView.isTextValid(defaultString: String): Boolean {
    return text.toString() != defaultString
}

fun isValidEmail(email: String): Boolean {
    val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
    return email.matches(emailRegex.toRegex())
}

fun String.isEmptyString() : Boolean {
    return this.isEmpty()
}

fun CommonEditText.trimString(): String {
    return text.toString().trim()
}

fun formatDateToGujarati(inputDate: String): String {
    // Parse the ISO timestamp
    val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
    parser.timeZone = TimeZone.getTimeZone("UTC")
    val date = parser.parse(inputDate)

    // Format to dd-MM-yyyy
    val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.US)
    val formatted = formatter.format(date!!)

    // Replace English digits with Gujarati digits
    val gujaratiDigits = mapOf(
        '0' to '૦',
        '1' to '૧',
        '2' to '૨',
        '3' to '૩',
        '4' to '૪',
        '5' to '૫',
        '6' to '૬',
        '7' to '૭',
        '8' to '૮',
        '9' to '૯'
    )

    return formatted.map { gujaratiDigits[it] ?: it }.joinToString("")
}
