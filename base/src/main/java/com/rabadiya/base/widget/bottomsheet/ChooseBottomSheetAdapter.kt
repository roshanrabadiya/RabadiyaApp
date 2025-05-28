package com.rabadiya.base.widget.bottomsheet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rabadiya.base.databinding.BottomSheetItemBinding

class ChooseBottomSheetAdapter(
    private val dataList: Array<String>,
    private val clickListener: (String) -> Unit
) : RecyclerView.Adapter<ChooseBottomSheetAdapter.BottomSheetViewHolder>() {

    inner class BottomSheetViewHolder(private val binding: BottomSheetItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setDataItem(data: String) {
            binding.tvItem.text = data
            binding.tvItem.setOnClickListener {
                clickListener(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomSheetViewHolder {
        return BottomSheetViewHolder(
            BottomSheetItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: BottomSheetViewHolder, position: Int) {
        holder.setDataItem(dataList[position])
    }
}