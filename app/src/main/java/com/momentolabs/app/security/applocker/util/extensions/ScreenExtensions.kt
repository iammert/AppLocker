package com.momentolabs.app.security.applocker.util.extensions

import android.content.Context
import android.content.res.Resources
import android.graphics.Point
import android.view.WindowManager

fun screenWidth() = Resources.getSystem().displayMetrics.widthPixels

fun getRealScreenSize(context: Context): Point {
    val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display = wm.defaultDisplay
    val screenSize = Point()
    display.getRealSize(screenSize)
    return screenSize
}