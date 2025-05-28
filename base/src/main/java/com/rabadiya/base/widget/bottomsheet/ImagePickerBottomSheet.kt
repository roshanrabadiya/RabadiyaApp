package com.rabadiya.base.widget.bottomsheet

import android.content.Context
import android.view.View
import androidx.core.view.isVisible
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.rabadiya.base.databinding.ImagePickerBottomSheetBinding


class ImagePickerBottomSheet(
    private val context: Context,
    private val imageSelected: Boolean,
    private val cameraCallBack: () -> Unit,
    private val galleryCallBack: () -> Unit,
    private val removeCallBack: () -> Unit
): BottomSheetDialog(context) {
    private val binding: ImagePickerBottomSheetBinding by lazy {
        ImagePickerBottomSheetBinding.inflate(layoutInflater)
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

        binding.btnRemove.isVisible = imageSelected

        binding.btnCamera.setOnClickListener {
            cameraCallBack()
            bottomSheetDialog?.dismiss()
        }

        binding.btnGallery.setOnClickListener {
            galleryCallBack()
            bottomSheetDialog?.dismiss()
        }

        binding.btnRemove.setOnClickListener {
            removeCallBack()
            bottomSheetDialog?.dismiss()
        }

        bottomSheetDialog?.show()
    }
}