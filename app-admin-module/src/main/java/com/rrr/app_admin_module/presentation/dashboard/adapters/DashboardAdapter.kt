package com.rrr.app_admin_module.presentation.dashboard.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rrr.app_admin_module.databinding.ItemDashboardBinding

class DashboardMenuAdapter(
    private val dashboardMenuList: List<DashboardMenuModel>,
    private val onMenuItemClick: (Int) -> Unit
) : RecyclerView.Adapter<DashboardMenuAdapter.DashboardMenuViewHolder>() {

    inner class DashboardMenuViewHolder(val binding: ItemDashboardBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardMenuViewHolder {
        return DashboardMenuViewHolder(
            ItemDashboardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return dashboardMenuList.size
    }

    override fun onBindViewHolder(holder: DashboardMenuViewHolder, position: Int) {
        val menuItem = dashboardMenuList[position]
        with(holder.binding) {
            tvTitle.text = menuItem.menuTitle
            ivImage.setImageResource(menuItem.menuIcon)
            main.setOnClickListener {
                onMenuItemClick(position)
            }
        }
    }
}