package com.rabadiya.base.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

class GenericAdapter<T, VB : ViewBinding>(
    private var items: ArrayList<T>,
    private val bindingInflater: (LayoutInflater, ViewGroup, Boolean) -> VB,
    private val bind: (VB, T, Int) -> Unit
) : RecyclerView.Adapter<GenericAdapter<T, VB>.GenericViewHolder>() {

    private var originalList: ArrayList<T> = ArrayList(items)

    inner class GenericViewHolder(val binding: VB) : RecyclerView.ViewHolder(binding.root) {
        fun bindItem(item: T, position: Int) {
            bind(binding, item, position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenericViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = bindingInflater(inflater, parent, false)
        return GenericViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GenericViewHolder, position: Int) {
        holder.bindItem(items[position], position)
    }

    override fun getItemCount(): Int = items.size

    fun updateList(newItems: ArrayList<T>) {
        originalList = ArrayList(newItems)
        items = newItems
        notifyDataSetChanged()
    }

    fun filter(predicate: (T) -> Boolean): Boolean {
        val filteredItems = originalList.filter(predicate)
        items = ArrayList(filteredItems)
        notifyDataSetChanged()
        return items.isNotEmpty()
    }

    fun resetList() {
        items = ArrayList(originalList)
        notifyDataSetChanged()
    }

    fun getItem(position: Int): T = items[position]
}