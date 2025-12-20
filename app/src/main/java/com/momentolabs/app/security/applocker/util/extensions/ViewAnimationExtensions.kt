package com.momentolabs.app.security.applocker.util.extensions

import android.animation.ObjectAnimator
import android.view.View

/** Shakes the view horizontally, for example to signal a wrong pattern. */
fun View.shake(durationMillis: Long = 700) {
    val distance = 12 * resources.displayMetrics.density
    ObjectAnimator.ofFloat(this, View.TRANSLATION_X, 0f, distance, -distance, distance, -distance, distance / 2, -distance / 2, 0f)
        .setDuration(durationMillis)
        .start()
}
