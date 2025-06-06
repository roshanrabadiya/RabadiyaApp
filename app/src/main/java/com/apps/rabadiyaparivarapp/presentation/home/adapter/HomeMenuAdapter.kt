package com.apps.rabadiyaparivarapp.presentation.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.apps.rabadiyaparivarapp.databinding.HomeMainMenuItemBinding
import com.rabadiya.base.model.home.MenuData
import com.rabadiya.base.utils.showGlideImage

class HomeMenuAdapter(
    private val context: Context, private val dataList: List<MenuData>,
    private val onItemClick: (Int, MenuData) -> Unit
) : RecyclerView.Adapter<HomeMenuAdapter.HomeMenuViewHolder>() {

    inner class HomeMenuViewHolder(val binding: HomeMainMenuItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeMenuViewHolder {
        return HomeMenuViewHolder(
            HomeMainMenuItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: HomeMenuViewHolder, position: Int) {
        val dataItem = dataList[position]

        holder.binding.apply {
            context.showGlideImage(dataItem.menuIcon, menuIcon)
            menuTitle.text = dataItem.menuTitle

            root.setOnClickListener {
                onItemClick(position, dataItem)
            }
        }
    }
}