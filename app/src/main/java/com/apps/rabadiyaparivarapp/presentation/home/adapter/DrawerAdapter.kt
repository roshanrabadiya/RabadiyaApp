package com.apps.rabadiyaparivarapp.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.apps.rabadiyaparivarapp.databinding.LayoutNevItemBinding
import com.rabadiya.base.model.home.NavItem

class DrawerAdapter(
    private val menuList: List<NavItem>,
    private val onItemClick: (Int, NavItem) -> Unit
) : RecyclerView.Adapter<DrawerAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(val binding: LayoutNevItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutNevItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return menuList.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        with(holder) {
            val item = menuList[position]
            binding.navMenuTitle.text = item.title
            binding.ivMenuIcon.setImageResource(item.icon)

            binding.root.setOnClickListener {
                onItemClick.invoke(position, item)
            }
        }
    }
}