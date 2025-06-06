package com.rabadiya.base.common

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rabadiya.base.common.Logging.LOGI
import com.rabadiya.base.utils.TAG

class PaginationScrollListener(
    private val layoutManager: LinearLayoutManager,
    private val hasMoreData: () -> Boolean,
    private val loadMore: () -> Unit,
    private val prefetchDistance: Int = 5
) : RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        LOGI(TAG, "onScrolled call $hasMoreData")
        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
        if ((visibleItemCount + firstVisibleItemPosition + prefetchDistance) >= totalItemCount
            && firstVisibleItemPosition >= 0 && hasMoreData()) {
            loadMore()
        }
    }
}