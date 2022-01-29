package com.peye.characters.ui.common

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputLayout

private const val NEAR_END_ELEMENTS_THRESHOLD = 5

@BindingAdapter("visibility")
fun View.setVisibility(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}

@BindingAdapter("srcUrl", "placeholderDrawable", requireAll = true)
fun ImageView.setImageFromUrl(srcUrl: String?, placeholder: Drawable) {
    srcUrl?.let { url ->
        Glide
            .with(context)
            .load(url)
            .centerCrop()
            .placeholder(placeholder)
            .into(this)
    } ?: setImageDrawable(placeholder)
}

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

@BindingAdapter("errorMessage", "errorMessageShown", requireAll = true)
fun TextInputLayout.setErrorMessage(messageContent: String, errorMessageShown: Boolean) {
    error = messageContent.takeIf { errorMessageShown }
}
