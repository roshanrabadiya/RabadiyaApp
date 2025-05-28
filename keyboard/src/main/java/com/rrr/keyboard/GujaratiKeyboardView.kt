package com.rrr.keyboard

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.inputmethod.InputConnection
import android.widget.LinearLayout

class GujaratiKeyboardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var inputConnection: InputConnection? = null

    init {
        // Inflate the keyboard layout
        LayoutInflater.from(context).inflate(R.layout.gujarati_keyboard, this, true)
        orientation = VERTICAL

        // Set up key listeners for all the buttons
     //   setupKeyListeners()
    }
}