package com.rabadiya.base.customviews.customtextview

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import org.koin.java.KoinJavaComponent.inject

class CommonTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : androidx.appcompat.widget.AppCompatTextView(context, attrs) {

    private val textViewStyler: TextViewStyler by inject(TextViewStyler::class.java)

    var collapseEnabled: Boolean = false

    init {
        if (!isInEditMode) {
            textViewStyler.applyAttributes(this@CommonTextView, attrs)
        } else {
            TextViewStyler.applyEditModeAttributes(this@CommonTextView, null)
        }
    }


    @SuppressLint("SetTextI18n")
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        if (collapseEnabled) {
            layout
                ?.takeIf { layout -> layout.lineCount > 0 }
                ?.let { layout -> layout.getEllipsisCount(layout.lineCount - 1) }
                ?.takeIf { ellipsisCount -> ellipsisCount > 0 }
                ?.let { ellipsisCount -> text.dropLast(ellipsisCount).lastIndexOf(',') }
                ?.takeIf { lastComma -> lastComma >= 0 }
                ?.let { lastComma ->
                    val remainingNames = text.drop(lastComma).count { c -> c == ',' }
                    text = "${text.take(lastComma)}, +$remainingNames"
                }
        }
    }

    override fun setTextColor(color: Int) {
        super.setTextColor(color)
        setLinkTextColor(color)
    }

}