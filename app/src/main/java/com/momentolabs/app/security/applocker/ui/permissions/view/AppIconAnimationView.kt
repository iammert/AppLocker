package com.momentolabs.app.security.applocker.ui.permissions.view

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.PointF
import android.util.AttributeSet
import android.view.View
import android.view.animation.BounceInterpolator
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.momentolabs.app.security.applocker.R

class AppIconAnimationView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val sizeAppIcon = context.resources.getDimension(R.dimen.size_animation_app_icon)

    val dotViewCount = 10

    var viewWidth: Float = 0f

    var viewHeight: Float = 0f

    var centerX: Float = 0f

    var centerY: Float = 0f

    val dotViews = arrayListOf<AppCompatImageView>()

    val translateXAnimators = arrayListOf<ValueAnimator>()

    val translateYAnimators = arrayListOf<ValueAnimator>()

    val scaleXAnimators = arrayListOf<ValueAnimator>()

    val scaleYAnimators = arrayListOf<ValueAnimator>()

    val destinationPoints = arrayListOf<PointF>()

    val defaultInterpolator = FastOutSlowInInterpolator()

    val bounceInterpolator = BounceInterpolator()

    init {
        createViews()

        dotViews.forEach {
            addView(it)
        }
    }

    private fun createDot(): AppCompatImageView {
        return AppCompatImageView(context).apply {
            background = ContextCompat.getDrawable(context, R.drawable.bg_dot)
            layoutParams = FrameLayout.LayoutParams(sizeAppIcon.toInt(), sizeAppIcon.toInt())
            visibility = View.INVISIBLE
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        viewWidth = measuredWidth.toFloat()
        viewHeight = measuredHeight.toFloat()
        centerX = viewWidth / 2
        centerY = viewHeight / 2

        createDestinationPoints(centerX, centerY)

        for (i in 0 until dotViews.count()) {
            val dotView = dotViews[i]
            val newLayoutParams = dotView.layoutParams as MarginLayoutParams
            newLayoutParams.leftMargin = (centerX - dotView.measuredWidth / 2).toInt()
            newLayoutParams.topMargin = (centerY - dotView.measuredHeight / 2).toInt()
            dotView.layoutParams = newLayoutParams

            val translateXObjectAnimator =
                ObjectAnimator.ofFloat(dotView, View.TRANSLATION_X, destinationPoints[i].x).apply {
                    duration = ANIM_DURATION
                    interpolator = defaultInterpolator
                }

            val translateYObjectAnimator =
                ObjectAnimator.ofFloat(dotView, View.TRANSLATION_Y, destinationPoints[i].y).apply {
                    duration = ANIM_DURATION
                    interpolator = defaultInterpolator
                }

            val scaleXObjectAnimator = ObjectAnimator.ofFloat(dotView, View.SCALE_X, 0f, 1f).apply {
                interpolator = defaultInterpolator
                duration = ANIM_DURATION
                addListener(object :SimpleAnimationListener(){
                    override fun onAnimationStart(animation: Animator?) {
                        super.onAnimationStart(animation)
                        dotView.visibility = View.VISIBLE
                    }
                })
            }

            val scaleYObjectAnimator = ObjectAnimator.ofFloat(dotView, View.SCALE_Y, 0f, 1f).apply {
                interpolator = defaultInterpolator
                duration = ANIM_DURATION
            }

            translateXAnimators.add(translateXObjectAnimator)
            translateYAnimators.add(translateYObjectAnimator)
            scaleXAnimators.add(scaleXObjectAnimator)
            scaleYAnimators.add(scaleYObjectAnimator)
        }

        playAll()
    }

    private fun createDestinationPoints(min: Float, max: Float) {
        destinationPoints.clear()
        destinationPoints.addAll(
            arrayListOf(
                PointF(min / 2, max / 2),
                PointF(min / 2, -max / 2),
                PointF(-min / 2, max / 2),
                PointF(-min / 2, -max / 2)
            )
        )
    }

    private fun createViews() {
        dotViews.addAll(
            arrayListOf(
                createDot(),
                createDot(),
                createDot(),
                createDot()
            )
        )
    }

    private fun playAll() {
        val playerSet = AnimatorSet()
        for (i in 0 until translateXAnimators.size) {
            val calculateDelay: Long = (i * 200).toLong()
            playerSet.playTogether(translateXAnimators[i].apply { startDelay = calculateDelay})
            playerSet.playTogether(translateYAnimators[i].apply { startDelay = calculateDelay})
            playerSet.playTogether(scaleXAnimators[i].apply { startDelay = calculateDelay})
            playerSet.playTogether(scaleYAnimators[i].apply { startDelay = calculateDelay})
        }
        playerSet.startDelay = 500
        playerSet.start()
    }

    companion object {

        private const val ANIM_DURATION = 500L
    }
}