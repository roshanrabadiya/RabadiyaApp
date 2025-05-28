package com.rabadiya.base.customviews.customspinner

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.widget.AppCompatSpinner
import androidx.core.content.ContextCompat
import com.rabadiya.base.R
import com.rabadiya.base.customviews.customtextview.CommonTextView
import com.rabadiya.base.utils.getColorCompat


class CommonSpinner @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatSpinner(context, attrs, defStyleAttr) {

    private var isDefaultSelected: Boolean = true
    private val initialTextWasShown = false

    init {
        setupSpinner()
    }

    private fun setupSpinner() {
        background = ContextCompat.getDrawable(context, R.drawable.shape_common_bg)
        isClickable = true
        setPopupBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.spinner_dropdown_background))
        dropDownVerticalOffset = 50
    }

    fun setItems(items: Array<String>) {
        val adapter = object : ArrayAdapter<String>(context, R.layout.spinner_item, items) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                return createSpinnerItemView(position, convertView, parent)
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                return createSpinnerDropdownItemView(position, convertView, parent)
            }
        }
        this.adapter = adapter

        onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                isDefaultSelected = (position == 0)
                if (!isDefaultSelected) {
                    (view as? TextView)?.setTextColor(context.getColorCompat(R.color.textPrimaryDark))
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                isDefaultSelected = true
            }
        }
    }

    private fun createSpinnerItemView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.spinner_item, parent, false)
        val textView = view as TextView
        textView.text = adapter.getItem(position).toString()
        textView.setTextColor(if (isDefaultSelected && position == 0) Color.GRAY else context.getColorCompat(R.color.textPrimaryDark))
        return view
    }

    private fun createSpinnerDropdownItemView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.spinner_dropdown_item, parent, false)
        val textView = view as CommonTextView
        textView.text = adapter.getItem(position).toString()

        return view
    }

}