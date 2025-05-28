package com.rabadiya.base.widget.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AlertDialog
import com.rabadiya.base.R
import com.rabadiya.base.databinding.CommonDialogBinding

class CommonDialog(
    private val context: Context
) : Dialog(context, R.style.Theme_Dialog) {

    private var alertDialog: AlertDialog? = null

    fun showDialog(
        title: String,
        message: String,
        positiveButtonText: String = "",
        negativeButtonText: String = "",
        isCancelable: Boolean = true,
        handlePositiveButton: () -> Unit = {},
        handleNegativeButton: () -> Unit = {},
    ) {
        val dialogView = CommonDialogBinding.inflate(layoutInflater)

        alertDialog =
            AlertDialog.Builder(context)
                .setView(dialogView.root)
                .setCancelable(isCancelable)
                .create()

        dialogView.dialogTitle.text = title
        dialogView.dialogDesc.text = message
        dialogView.dialogButtonPositive.text = positiveButtonText
        dialogView.dialogButtonNegative.text = negativeButtonText

        dialogView.dialogButtonPositive.setOnClickListener {
            handlePositiveButton()
            alertDialog?.dismiss()
        }

        dialogView.dialogButtonNegative.setOnClickListener {
            handleNegativeButton()
            alertDialog?.dismiss()
        }

        alertDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog?.show()
    }
}