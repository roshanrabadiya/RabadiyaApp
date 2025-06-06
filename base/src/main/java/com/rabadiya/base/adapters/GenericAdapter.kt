package com.rabadiya.base.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

class GenericAdapter<T, VB : ViewBinding>(
    private var items: ArrayList<T>,
    private val bindingInflater: (LayoutInflater, ViewGroup, Boolean) -> VB,
    private val loadingBindingInflater: (LayoutInflater, ViewGroup, Boolean) -> ViewBinding? = { _, _, _ -> null },
    private val bind: (VB, T, Int) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var originalList: ArrayList<T> = ArrayList(items)
    private var isLoading = false

    companion object {
        private const val VIEW_TYPE_ITEM = 0
        private const val VIEW_TYPE_LOADING = 1
    }

    inner class GenericViewHolder(val binding: VB) : RecyclerView.ViewHolder(binding.root) {
        fun bindItem(item: T, position: Int) {
            bind(binding, item, position)
        }
    }

    inner class LoadingViewHolder(binding: ViewBinding?) : RecyclerView.ViewHolder(binding?.root!!)

    override fun getItemViewType(position: Int): Int {
        return if (position == items.size && isLoading) {
            VIEW_TYPE_LOADING
        } else {
            VIEW_TYPE_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_LOADING -> {
                val loadingBinding = loadingBindingInflater(inflater, parent, false)
                LoadingViewHolder(loadingBinding)
            }
            else -> {
                val binding = bindingInflater(inflater, parent, false)
                GenericViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is GenericAdapter<T, *>.GenericViewHolder -> {
                holder.bindItem(items[position], position)
            }
            is GenericAdapter<*, *>.LoadingViewHolder -> {
                // Loading view doesn't need binding, it's just a static loading indicator
            }
        }
    }

    override fun getItemCount(): Int {
        return if (isLoading) items.size + 1 else items.size
    }

    fun updateList(newItems: ArrayList<T>) {
        originalList = ArrayList(newItems)
        items = newItems
        isLoading = false
        notifyDataSetChanged()
    }

    fun updatePaginationList(newItems: ArrayList<T>) {
        val oldSize = items.size
        originalList.addAll(newItems)
        items.addAll(newItems)
        isLoading = false

        // Remove loading view first, then add new items
        notifyItemRemoved(oldSize)
        notifyItemRangeInserted(oldSize, newItems.size)
    }

    fun showLoadingView() {
        if (!isLoading) {
            isLoading = true
            notifyItemInserted(items.size)
        }
    }

    fun hideLoadingView() {
        if (isLoading) {
            isLoading = false
            notifyItemRemoved(items.size)
        }
    }

    fun filter(predicate: (T) -> Boolean): Boolean {
        val filteredItems = originalList.filter(predicate)
        items = ArrayList(filteredItems)
        isLoading = false
        notifyDataSetChanged()
        return items.isNotEmpty()
    }

    fun resetList() {
        items = ArrayList(originalList)
        isLoading = false
        notifyDataSetChanged()
    }

    fun getItem(position: Int): T = items[position]

    fun isLoadingVisible(): Boolean = isLoading
}