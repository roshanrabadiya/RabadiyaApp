package com.rabadiya.base.customviews.customtextview

import android.content.Context
import android.text.InputType
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.rabadiya.base.R

class SelectionEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): LinearLayout(context, attrs, defStyleAttr) {

    private var editText: CommonEditText? = null
    private var cancelButton: ImageButton? = null

    init {
        orientation = HORIZONTAL

        editText = CommonEditText(context).apply {
            layoutParams = LayoutParams(0, LayoutParams.WRAP_CONTENT, 1f)
            isClickable = true
            isFocusable = true
            isCursorVisible = false
            isEnabled = true
            background = null
        }
        addView(editText)

        cancelButton = ImageButton(context).apply {
            layoutParams = LayoutParams(130, LayoutParams.MATCH_PARENT)
            setImageResource(R.drawable.ic_close)
            background = ContextCompat.getDrawable(context, R.drawable.password_toggle_effect)
            setOnClickListener { cancelClick() }
        }
        addView(cancelButton)

        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.PasswordEditText)
            val hint = typedArray.getString(R.styleable.PasswordEditText_hint)
            editText?.hint = hint
            typedArray.recycle()
        }

        visibleClose()
    }

    private fun cancelClick() {
        editText?.text?.clear()
    }

    fun getText(): String = editText?.text.toString()

    fun setText(text: String) {
        editText?.setText(text)
    }

    fun visibleClose() {
        if ((editText?.text?.length ?: 0) > 0) {
            cancelButton?.visibility = View.VISIBLE
        }else {
            cancelButton?.visibility = View.GONE
        }
    }
}