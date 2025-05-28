package com.rabadiya.base.customviews.customtextview

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import android.view.inputmethod.InputConnectionWrapper
import androidx.core.view.inputmethod.EditorInfoCompat
import androidx.core.view.inputmethod.InputConnectionCompat
import androidx.core.view.inputmethod.InputContentInfoCompat
import org.koin.java.KoinJavaComponent.inject

class CommonEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : androidx.appcompat.widget.AppCompatEditText(context, attrs) {

    private val textViewStyler: TextViewStyler by inject(TextViewStyler::class.java)

    var onBackspace: (() -> Unit)? = null
    private var onInputContentSelected: ((InputContentInfoCompat) -> Unit)? = null
    private var supportsInputContent: Boolean = false

    companion object {
        const val IMAGE_JPEG = "image/jpeg"
        const val IMAGE_JPG = "image/jpg"
        const val IMAGE_PNG = "image/png"
        const val IMAGE_GIF = "image/gif"
    }

    init {
        if (!isInEditMode) {
            textViewStyler.applyAttributes(this@CommonEditText, attrs)
        } else {
            TextViewStyler.applyEditModeAttributes(this@CommonEditText, null)
        }
    }


    override fun onCreateInputConnection(outAttrs: EditorInfo): InputConnection {
        val inputConnection = object : InputConnectionWrapper(super.onCreateInputConnection(outAttrs), true) {
            override fun sendKeyEvent(event: KeyEvent?): Boolean {
                if (event?.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_DEL) {
                    onBackspace?.invoke()
                }
                return super.sendKeyEvent(event)
            }

            override fun deleteSurroundingText(beforeLength: Int, afterLength: Int): Boolean {
                if (beforeLength == 1 && afterLength == 0) {
                    onBackspace?.invoke()
                }
                return super.deleteSurroundingText(beforeLength, afterLength)
            }
        }

        if (supportsInputContent) {
            EditorInfoCompat.setContentMimeTypes(outAttrs, arrayOf(
                IMAGE_JPEG, IMAGE_JPG, IMAGE_PNG, IMAGE_GIF
            ))
        }

        InputConnectionCompat.OnCommitContentListener { inputContentInfo, flags, _ ->
            val grantReadPermission = flags and InputConnectionCompat.INPUT_CONTENT_GRANT_READ_URI_PERMISSION != 0
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1 && grantReadPermission) {
                try {
                    inputContentInfo.requestPermission()
                    onInputContentSelected?.invoke(inputContentInfo)
                    true
                } catch (e: Exception) {
                    false
                }
            } else {
                true
            }
        }

        return inputConnection //InputConnectionCompat.createWrapper(inputConnection, outAttrs, callback)
    }

}