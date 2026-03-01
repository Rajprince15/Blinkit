package com.example.blinkit.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.blinkit.R

/**
 * Utility object for loading images with Glide
 */
object ImageLoader {

    /**
     * Load image with default placeholder and error handling
     */
    fun load(
        imageView: ImageView,
        url: String?,
        placeholder: Int = R.mipmap.ic_launcher,
        error: Int = R.mipmap.ic_launcher
    ) {
        Glide.with(imageView.context)
            .load(url)
            .placeholder(placeholder)
            .error(error)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(imageView)
    }

    /**
     * Load image with rounded corners
     */
    fun loadRounded(
        imageView: ImageView,
        url: String?,
        cornerRadius: Int = 16,
        placeholder: Int = R.mipmap.ic_launcher,
        error: Int = R.mipmap.ic_launcher
    ) {
        Glide.with(imageView.context)
            .load(url)
            .placeholder(placeholder)
            .error(error)
            .transform(CenterCrop(), RoundedCorners(cornerRadius))
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(imageView)
    }

    /**
     * Load circular image (for categories, profile pictures)
     */
    fun loadCircular(
        imageView: ImageView,
        url: String?,
        placeholder: Int = R.mipmap.ic_launcher,
        error: Int = R.mipmap.ic_launcher
    ) {
        Glide.with(imageView.context)
            .load(url)
            .placeholder(placeholder)
            .error(error)
            .circleCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(imageView)
    }

    /**
     * Load image without placeholder (for smooth transitions)
     */
    fun loadWithoutPlaceholder(
        imageView: ImageView,
        url: String?,
        error: Int = R.mipmap.ic_launcher
    ) {
        Glide.with(imageView.context)
            .load(url)
            .error(error)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(imageView)
    }

    /**
     * Clear image from view
     */
    fun clear(imageView: ImageView) {
        Glide.with(imageView.context).clear(imageView)
    }
}