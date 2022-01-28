package com.peye.characters.ui.common

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

private const val NEAR_END_ELEMENTS_THRESHOLD = 5

@BindingAdapter("onScrolledNearEnd")
fun RecyclerView.setOnScrolledNearEndAction(action: (collectionSize: Int) -> Unit) {
    clearOnScrollListeners()
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val linearLayoutManager = layoutManager as? LinearLayoutManager ?: return
            val totalItemCount = linearLayoutManager.itemCount
            val lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition()

            val isNearEnd = lastVisibleItem + NEAR_END_ELEMENTS_THRESHOLD >= totalItemCount

            if (isNearEnd) {
                action(totalItemCount)
            }
        }
    })
}
