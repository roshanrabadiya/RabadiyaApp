package com.rabadiya.base.customviews.customtextview


import android.util.AttributeSet
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.rabadiya.base.R
import com.rabadiya.base.common.AppPreference
import com.rabadiya.base.common.PreferenceKey.KEY_TEXT_SIZE
import com.rabadiya.base.utils.TEXT_SIZE_LARGE
import com.rabadiya.base.utils.TEXT_SIZE_LARGER
import com.rabadiya.base.utils.TEXT_SIZE_NORMAL
import com.rabadiya.base.utils.TEXT_SIZE_SMALL
import com.rabadiya.base.utils.getColorCompat

class TextViewStyler (
    private val appPref: AppPreference
) {

    companion object {
        private const val COLOR_THEME = 0
        private const val COLOR_PRIMARY_ON_THEME = 1
        private const val COLOR_SECONDARY_ON_THEME = 2
        private const val COLOR_TERTIARY_ON_THEME = 3

        const val SIZE_PRIMARY = 0
        const val SIZE_SECONDARY = 1
        const val SIZE_TERTIARY = 2
        const val SIZE_TOOLBAR = 3
        const val SIZE_DIALOG = 4
        const val SIZE_EMOJI = 5

        const val STYLE_REGULAR = 0
        const val STYLE_MEDIUM = 1
        const val STYLE_SEMI_BOLD = 2
        const val STYLE_BOLD = 3

        fun applyEditModeAttributes(textView: TextView, attrs: AttributeSet?) {
            textView.run {
                var colorAttr = 0
                var textSizeAttr = 0
                var textStyleAttr = 0

                when (this) {
                    is CommonTextView -> context.obtainStyledAttributes(
                        attrs, R.styleable.CustomTextView
                    ).run {
                        colorAttr = getInt(R.styleable.CustomTextView_textColor, -1)
                        textSizeAttr = getInt(R.styleable.CustomTextView_textSize, -1)
                        textStyleAttr = getInt(R.styleable.CustomTextView_textStyle, -1)
                        recycle()
                    }

                    is CommonEditText -> context.obtainStyledAttributes(
                        attrs, R.styleable.CustomEditText
                    ).run {
                        colorAttr = getInt(R.styleable.CustomEditText_textColor, -1)
                        textSizeAttr = getInt(R.styleable.CustomEditText_textSize, -1)
                        textStyleAttr = getInt(R.styleable.CustomEditText_textStyle, -1)
                        recycle()
                    }
                }

                setTextColor(
                    when (colorAttr) {
                        COLOR_PRIMARY_ON_THEME -> context.getColorCompat(R.color.textPrimaryDark)
                        COLOR_SECONDARY_ON_THEME -> context.getColorCompat(R.color.textSecondaryDark)
                        COLOR_TERTIARY_ON_THEME -> context.getColorCompat(R.color.textTertiaryDark)
                        COLOR_THEME -> context.getColorCompat(R.color.tools_theme)
                        else -> currentTextColor
                    }
                )


                textSize = when (textSizeAttr) {
                    SIZE_PRIMARY -> context.resources.getDimension(R.dimen.text_size_primary)
                    SIZE_SECONDARY -> context.resources.getDimension(R.dimen.text_size_secondary)
                    SIZE_TERTIARY -> context.resources.getDimension(R.dimen.text_size_tertiary)
                    SIZE_TOOLBAR -> context.resources.getDimension(R.dimen.text_size_toolbar)
                    SIZE_DIALOG -> context.resources.getDimension(R.dimen.text_size_dialog)
                    SIZE_EMOJI -> context.resources.getDimension(R.dimen.text_size_emoji)
                    else -> textSize / paint.density
                }

                typeface = when(textStyleAttr) {
                    STYLE_REGULAR -> ResourcesCompat.getFont(textView.context, R.font.font_bold)
                    STYLE_MEDIUM -> ResourcesCompat.getFont(textView.context, R.font.font_medium)
                    STYLE_SEMI_BOLD -> ResourcesCompat.getFont(textView.context, R.font.font_semi_bold)
                    STYLE_BOLD -> ResourcesCompat.getFont(textView.context, R.font.font_bold)
                    else -> ResourcesCompat.getFont(textView.context, R.font.font_regular)
                }
            }
        }
    }

    fun applyAttributes(textView: TextView, attrs: AttributeSet?, ) {
        var textSizeAttr: Int
        var textStyleAttr: Int
        var colorAttr: Int

        when (textView) {
            is CommonTextView -> textView.context.obtainStyledAttributes(
                attrs, R.styleable.CustomTextView
            ).run {
                colorAttr = getInt(R.styleable.CustomTextView_textColor, -1)
                textSizeAttr = getInt(R.styleable.CustomTextView_textSize, -1)
                textStyleAttr = getInt(R.styleable.CustomTextView_textStyle, -1)
                recycle()
            }
            else -> return
        }

        textView.setTypeface(
            when(textStyleAttr) {
                STYLE_REGULAR -> ResourcesCompat.getFont(textView.context, R.font.font_bold)
                STYLE_MEDIUM -> ResourcesCompat.getFont(textView.context, R.font.font_medium)
                STYLE_SEMI_BOLD -> ResourcesCompat.getFont(textView.context, R.font.font_semi_bold)
                STYLE_BOLD -> ResourcesCompat.getFont(textView.context, R.font.font_bold)
                else -> ResourcesCompat.getFont(textView.context, R.font.font_regular)
            }
        )

        setTextSize(textView, textSizeAttr)
        setTextColor(textView, colorAttr)
    }

    private fun setTextColor(textView: TextView, colorAttr: Int) {
        textView.run {
            setTextColor(
                when (colorAttr) {
                    COLOR_PRIMARY_ON_THEME -> context.getColorCompat(R.color.textPrimaryDark)
                    COLOR_SECONDARY_ON_THEME -> context.getColorCompat(R.color.textSecondaryDark)
                    COLOR_TERTIARY_ON_THEME -> context.getColorCompat(R.color.textTertiaryDark)
                    COLOR_THEME -> context.getColorCompat(R.color.tools_theme)
                    else -> currentTextColor
                }
            )
        }
    }

    private fun setTextSize(textView: TextView, textSizeAttr: Int) {
        val textSizePref = appPref.getIntData(KEY_TEXT_SIZE, TEXT_SIZE_NORMAL)

        when (textSizeAttr) {
            SIZE_PRIMARY -> textView.textSize = when (textSizePref) {
                TEXT_SIZE_SMALL -> 14f
                TEXT_SIZE_NORMAL -> 16f
                TEXT_SIZE_LARGE -> 18f
                TEXT_SIZE_LARGER -> 20f
                else -> 16f
            }

            SIZE_SECONDARY -> textView.textSize = when (textSizePref) {
                TEXT_SIZE_SMALL -> 12f
                TEXT_SIZE_NORMAL -> 14f
                TEXT_SIZE_LARGE -> 16f
                TEXT_SIZE_LARGER -> 18f
                else -> 14f
            }

            SIZE_TERTIARY -> textView.textSize = when (textSizePref) {
                TEXT_SIZE_SMALL -> 10f
                TEXT_SIZE_NORMAL -> 12f
                TEXT_SIZE_LARGE -> 14f
                TEXT_SIZE_LARGER -> 16f
                else -> 12f
            }

            SIZE_TOOLBAR -> textView.textSize = when (textSizePref) {
                TEXT_SIZE_SMALL -> 18f
                TEXT_SIZE_NORMAL -> 20f
                TEXT_SIZE_LARGE -> 22f
                TEXT_SIZE_LARGER -> 26f
                else -> 20f
            }

            SIZE_DIALOG -> textView.textSize = when (textSizePref) {
                TEXT_SIZE_SMALL -> 16f
                TEXT_SIZE_NORMAL -> 18f
                TEXT_SIZE_LARGE -> 20f
                TEXT_SIZE_LARGER -> 24f
                else -> 18f
            }

            SIZE_EMOJI -> textView.textSize = when (textSizePref) {
                TEXT_SIZE_SMALL -> 28f
                TEXT_SIZE_NORMAL -> 32f
                TEXT_SIZE_LARGE -> 36f
                TEXT_SIZE_LARGER -> 40f
                else -> 32f
            }
        }
    }
}