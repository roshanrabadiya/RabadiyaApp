package com.rabadiya.base.widget.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AlertDialog
import com.rabadiya.base.R
import com.rabadiya.base.databinding.ProgressDialogBinding
import androidx.core.graphics.drawable.toDrawable

class ProgressDialog(private val context: Context) : Dialog(context, R.style.Theme_Dialog) {
    private var alertDialog: AlertDialog? = null

    fun showProgressDialog(message: String) {
        val dialogView = ProgressDialogBinding.inflate(layoutInflater)
        dialogView.tvMessage.text = message

        alertDialog = AlertDialog.Builder(context)
            .setView(dialogView.root)
            .setCancelable(false)
            .create()
        alertDialog?.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
        alertDialog?.show()
    }

    fun dismissProgressDialog() {
        alertDialog?.dismiss() ?: {
            val dialogView = ProgressDialogBinding.inflate(layoutInflater)
            alertDialog = AlertDialog.Builder(context)
                .setView(dialogView.root)
                .setCancelable(false)
                .create()
            alertDialog?.dismiss()
        }
    }
}