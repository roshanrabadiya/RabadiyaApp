package com.rabadiya.base.customviews.customtextview

import android.content.Context
import android.text.InputType
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.rabadiya.base.R

class PasswordEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): LinearLayout(context, attrs, defStyleAttr) {

    private var editText: CommonEditText? = null
    private var toggleButton: ImageButton? = null
    private var isPasswordVisible = false

    init {
        orientation = HORIZONTAL

        editText = CommonEditText(context).apply {
            layoutParams = LayoutParams(0, LayoutParams.WRAP_CONTENT, 1f)
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            background = null
        }
        addView(editText)

        toggleButton = ImageButton(context).apply {
            layoutParams = LayoutParams(130, LayoutParams.MATCH_PARENT)
            setImageResource(R.drawable.ic_pass_show)
            background = ContextCompat.getDrawable(context, R.drawable.password_toggle_effect)
            setOnClickListener { togglePasswordVisibility() }
        }
        addView(toggleButton)

        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.PasswordEditText)
            val hint = typedArray.getString(R.styleable.PasswordEditText_hint)
            editText?.hint = hint
            typedArray.recycle()
        }
    }

    private fun togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible
        editText?.transformationMethod = if (isPasswordVisible) {
            toggleButton?.setImageResource(R.drawable.ic_pass_hide)
            HideReturnsTransformationMethod.getInstance()
        } else {
            toggleButton?.setImageResource(R.drawable.ic_pass_show)
            PasswordTransformationMethod.getInstance()
        }
        editText?.setSelection(editText?.text?.length ?: 0)
    }

    fun getText(): String = editText?.text.toString()

    fun setText(text: String) {
        editText?.setText(text)
    }
}