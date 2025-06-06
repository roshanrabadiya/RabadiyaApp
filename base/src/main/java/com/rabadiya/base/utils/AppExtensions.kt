package com.rabadiya.base.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.snackbar.Snackbar
import com.rabadiya.base.BuildConfig
import com.rabadiya.base.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

inline fun <reified T : Context> Context.launchActivity(
    flags: IntArray? = null,
    noinline extrasCallBack: Intent.() -> Unit = {}
) {
    val intent = Intent(this, T::class.java).apply {
        extrasCallBack()
        flags?.forEach { addFlags(it) }
    }
    startActivity(intent)
}

inline fun <reified T : Context> Context.launchActivityWithFinish(
    flags: IntArray? = null,
    noinline extrasCallBack: Intent.() -> Unit = {}
) {
    val intent = Intent(this, T::class.java).apply {
        extrasCallBack()
        flags?.forEach { addFlags(it) }
    }
    startActivity(intent)
    if (this is Activity) {
        finish()
    }
}

inline fun <T : ViewBinding> AppCompatActivity.viewBinding(
    crossinline bindingInflater: (LayoutInflater) -> T
) = lazy(LazyThreadSafetyMode.NONE) {
    bindingInflater.invoke(layoutInflater)
}

@SuppressLint("HardwareIds")
fun getAndroidDeviceId(context: Context): String {
    val androidId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)

    return androidId
}

val backgroundScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

val mainScope = CoroutineScope(Dispatchers.Main)

fun Context.getColors(@ColorRes color: Int): Int {
    return ContextCompat.getColor(this, color)
}

fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun Context.showErrorToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    if (BuildConfig.DEBUG) {
        Toast.makeText(this, message, duration).show()
    } else {
        Toast.makeText(this, resources.getString(R.string.something_went_wrong), duration).show()
    }
}

fun ImageView.loadImage(url: String, placeHolder: Int = R.drawable.ic_user) {
    val imageUrl = if (BuildConfig.DEBUG) {
        url.replace("localhost", LOCAL_SERVER_IP)
    } else {
        url
    }

    Glide.with(context)
        .load(imageUrl)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .centerCrop()
        .placeholder(placeHolder)
        .thumbnail(
            Glide.with(context)
                .load(imageUrl)
                .override(100, 100)
                .centerCrop()
        )
        .error(placeHolder)
        .into(this)
}

fun View.showSnackBar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_INDEFINITE)
        .setAction("OK") {}
        .show()
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.GONE
}

/**
 * Extension function to safe multiple time clicks
 */
fun View.setSafeOnClickListener(debounceTime: Long = 800, action: (View) -> Unit) {
    var lastClickTime: Long = 0

    this.setOnClickListener { view ->
        val currentTime = System.currentTimeMillis()

        if (currentTime - lastClickTime > debounceTime && view.isVisible) {
            lastClickTime = currentTime
            action(view)
        }
    }
}


/**
 * Extension function to show the keyboard for an Activity
 */
fun Activity.showKeyBoard() {
    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    currentFocus?.let {
        inputMethodManager.showSoftInput(it, InputMethodManager.SHOW_IMPLICIT)
    }
}

/**
 * Extension function to hide the keyboard for an Activity
 */
fun Activity.hideKeyboard() {
    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    currentFocus?.let {
        inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
    }?: run {
        val view = window.decorView.rootView
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

}

/**
 * Extension function to show the keyboard for a Fragment
 */
fun Fragment.showKeyboard() {
    view?.let { view ->
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        view.requestFocus()
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }
}

/**
 * Extension function to hide the keyboard for a Fragment
 */
fun Fragment.hideKeyboard() {
    view?.let { view ->
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}



