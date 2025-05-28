package com.rabadiya.base.widget.bottomsheet

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.rabadiya.base.databinding.ChooseBottomSheetBinding

class ChooseBottomSheetDialog(
    private val context: Context,
    private val dataList: Array<String>,
    private val callBack: (String) -> Unit
) : BottomSheetDialog(context) {

    private val binding: ChooseBottomSheetBinding by lazy {
        ChooseBottomSheetBinding.inflate(layoutInflater)
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

        binding.bottomSheetTitle.text = dataList[0]

        binding.rvBottomSheet.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        val newDataList = dataList.toMutableList()
        newDataList.removeAt(0)
        binding.rvBottomSheet.adapter = ChooseBottomSheetAdapter(newDataList.toTypedArray()) { data ->
            callBack(data)
            bottomSheetDialog?.dismiss()
        }

        binding.btnClose.setOnClickListener {
            bottomSheetDialog?.dismiss()
        }

        bottomSheetDialog?.show()
    }

}