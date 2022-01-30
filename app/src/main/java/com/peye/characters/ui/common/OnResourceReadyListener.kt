package com.peye.characters.ui.common

import android.graphics.drawable.Drawable
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

/**
 * A glide.request.RequestListener with only a need to implement the onResourceReady() callback,
 * leaving the remaining onLoadFailed() with a default empty implementation.
 * @see RequestListener
 */
fun interface OnResourceReadyListener : RequestListener<Drawable> {

    override fun onLoadFailed(
        e: GlideException?,
        model: Any?,
        target: Target<Drawable>?,
        isFirstResource: Boolean
    ): Boolean {
        return false
    }
}
