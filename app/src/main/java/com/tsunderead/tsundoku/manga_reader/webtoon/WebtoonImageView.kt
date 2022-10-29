package com.tsunderead.tsundoku.manga_reader.webtoon

import android.content.Context
import android.graphics.PointF
import android.util.AttributeSet
import android.view.View
import android.view.ViewParent
import androidx.recyclerview.widget.RecyclerView
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import org.koitharu.kotatsu.parsers.util.toIntUp

private const val SCROLL_UNKNOWN = -1

class WebtoonImageView @JvmOverloads constructor(
    context: Context,
    attr: AttributeSet? = null,
) : SubsamplingScaleImageView(context, attr) {

    private val ct = PointF()

    private var scrollPos = 0
    private var scrollRange = SCROLL_UNKNOWN

    fun scrollBy(delta: Int) {
        val maxScroll = getScrollRange()
        if (maxScroll == 0) {
            return
        }
        val newScroll = scrollPos + delta
        scrollToInternal(newScroll.coerceIn(0, maxScroll))
    }

    fun scrollTo(y: Int) {
        val maxScroll = getScrollRange()
        if (maxScroll == 0) {
            resetScaleAndCenter()
            return
        }
        scrollToInternal(y.coerceIn(0, maxScroll))
    }

    fun getScroll() = scrollPos

    private fun getScrollRange(): Int {
        if (scrollRange == SCROLL_UNKNOWN) {
            computeScrollRange()
        }
        return scrollRange.coerceAtLeast(0)
    }

    override fun recycle() {
        scrollRange = SCROLL_UNKNOWN
        scrollPos = 0
        super.recycle()
    }

    private fun scrollToInternal(pos: Int) {
        scrollPos = pos
        ct.set(sWidth / 2f, (height / 2f + pos.toFloat()) / minScale)
        setScaleAndCenter(minScale, ct)
    }

    private fun computeScrollRange() {
        if (!isReady) {
            return
        }
        val totalHeight = (sHeight * minScale).toIntUp()
        scrollRange = (totalHeight - height).coerceAtLeast(0)
    }

    private fun parentHeight(): Int {
        return parents.firstNotNullOfOrNull { it as? RecyclerView }?.height ?: 0
    }

    private val View.parents: Sequence<ViewParent>
        get() = sequence {
            var p: ViewParent? = parent
            while (p != null) {
                yield(p)
                p = p.parent
            }
        }
}
