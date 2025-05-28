package com.rabadiya.base.widget.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AlertDialog
import com.rabadiya.base.R
import com.rabadiya.base.databinding.DialogTaskSucessBinding

class TaskSuccessDialog(
    private val context: Context,
    private val onDialogDismiss: () -> Unit
) : Dialog(context, R.style.Theme_Dialog) {

    private var alertDialog: AlertDialog? = null

    fun showDialog(
        title: String = "",
        buttonText: String = "",
        isCancelable: Boolean = true,
    ) {
        val dialogView = DialogTaskSucessBinding.inflate(layoutInflater)

        alertDialog =
            AlertDialog.Builder(context)
                .setView(dialogView.root)
                .setCancelable(isCancelable)
                .create()

        if (title.isNotEmpty()) {
            dialogView.dialogTitle.text = title
        }
        if (buttonText.isNotEmpty()) {
            dialogView.btnOk.text = buttonText
        }

        dialogView.btnOk.setOnClickListener {
            onDialogDismiss()
            alertDialog?.dismiss()
        }

        alertDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog?.show()
    }

}