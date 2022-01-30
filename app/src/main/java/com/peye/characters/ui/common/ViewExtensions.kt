package com.peye.characters.ui.common

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.google.android.material.textfield.TextInputLayout
import com.bumptech.glide.request.target.Target

private const val NEAR_END_ELEMENTS_THRESHOLD = 5

@BindingAdapter("visibility")
fun View.setVisibility(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}

@BindingAdapter("srcUrl", "placeholderDrawable", requireAll = true)
fun ImageView.setImageFromUrl(srcUrl: String?, placeholder: Drawable) {
    val transitionStartingListener = // Support for the Shared Element Screen Transitions
        OnResourceReadyListener { _: Drawable?, _: Any?, _: Target<Drawable>?, _: DataSource?, _: Boolean ->
            false.also { startPostponedEnterTransition() }
        }
    srcUrl?.let { url ->
        Glide
            .with(context)
            .load(url)
            .centerCrop()
            .placeholder(placeholder)
            .listener(transitionStartingListener)
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

/**
 * If only possible, starts postponed enter transition on a view's fragment.
 * If the view is no longer added to any fragment, there is nothing for us to do.
 * No need for any special reaction either to that so-called 'illegal state'.
 */
private fun View.startPostponedEnterTransition() {
    try { findFragment<Fragment>().startPostponedEnterTransition() }
    catch (ex: IllegalStateException) { } // It's thrown when fragment could not be found
    // This is normal situation which happens eg. in a RecyclerView when scrolling
}
