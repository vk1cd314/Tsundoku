package com.tsunderead.tsundoku.chapter.chapter_page

import android.graphics.drawable.Drawable
import android.net.Uri
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.transition.Transition
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import com.tsunderead.tsundoku.manga_reader.webtoon.WebtoonImageView
import java.io.File

class ChapterImageView(view: WebtoonImageView)
    : CustomViewTarget<WebtoonImageView, File>(view) {
    override fun onResourceReady(resource: File, transition: Transition<in File>?) {
        view.setImage(ImageSource.uri(Uri.fromFile(resource)))
    }

    override fun onLoadFailed(errorDrawable: Drawable?) {
        // Ignore
    }

    override fun onResourceCleared(placeholder: Drawable?) {
        // Ignore
    }
}