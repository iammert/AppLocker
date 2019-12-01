package com.momentolabs.app.security.applocker.ui.background

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

data class GradientItemViewState(
    val id: Int, @DrawableRes val gradientBackgroundRes: Int,
    var isChecked: Boolean = false
) : BackgroundItem {

    fun getGradiendDrawable(context: Context): Drawable? {
        return ContextCompat.getDrawable(context, gradientBackgroundRes)
    }

    fun isCheckedVisible(): Int = if (isChecked) View.VISIBLE else View.INVISIBLE

}