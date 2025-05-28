package com.rrr.app_admin_module.widget.bottomsheet

import android.content.Context
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.rabadiya.base.common.getStrings
import com.rabadiya.base.common.sensitizedString
import com.rabadiya.base.utils.setSafeOnClickListener
import com.rrr.app_admin_module.R
import com.rrr.app_admin_module.databinding.RejectApplicationMessageBsBinding

class RejectApplicationMessageBottomSheet(
    private val context: Context,
    private val onRejectClick: (String) -> Unit
) : BottomSheetDialog(context) {

    private val binding: RejectApplicationMessageBsBinding by lazy {
        RejectApplicationMessageBsBinding.inflate(layoutInflater)
    }

    private var bottomSheetDialog: BottomSheetDialog? = null

    init {
        setBottomSheetDialog()
    }

    private fun setBottomSheetDialog() {
        bottomSheetDialog = BottomSheetDialog(context)
        bottomSheetDialog?.apply {
            setContentView(binding.root)
            setCancelable(true)
            setCanceledOnTouchOutside(true)
        }

        binding.btnClose.setOnClickListener {
            bottomSheetDialog?.dismiss()
        }

        binding.btnReject.setSafeOnClickListener {
            if (binding.etReason.sensitizedString().isEmpty()) {
                binding.etReason.error = context.getStrings(R.string.reason_for_rejection_message)
                return@setSafeOnClickListener
            }
            onRejectClick(binding.etReason.sensitizedString())
            bottomSheetDialog?.dismiss()
        }

        bottomSheetDialog?.show()
    }

}