package com.momentolabs.app.security.applocker.ui.security

import android.animation.Animator
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.momentolabs.app.security.applocker.ui.permissions.view.SimpleAnimationListener

class MainActionsShowHideScrollListener(val view: View, private val viewHeight: Float) :
    RecyclerView.OnScrollListener() {

    private var scrollPosition = 0
    private var animatingUp = false
    private var animatingDown = false

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        scrollPosition += if (dy > 0) dy else dy

        if (scrollPosition < viewHeight) {

            if (view.translationY < viewHeight && animatingDown.not()) {
                view.animate()
                    .translationY(0f)
                    .setDuration(300)
                    .setListener(object : SimpleAnimationListener() {
                        override fun onAnimationEnd(animation: Animator?) {
                            animatingDown = false
                        }
                    })
                    .start()
                animatingDown = true
                animatingUp = false
            }

            return
        }

        if (dy > 0) {
            if (animatingUp.not()) {
                view.animate()
                    .translationY(-viewHeight)
                    .setDuration(300)
                    .setListener(object : SimpleAnimationListener() {
                        override fun onAnimationEnd(animation: Animator?) {
                            animatingUp = false
                        }
                    })
                    .start()
                animatingUp = true
                animatingDown = false
            }

        } else {
            if (animatingDown.not()) {
                view.animate()
                    .translationY(0f)
                    .setDuration(300)
                    .setListener(object : SimpleAnimationListener() {
                        override fun onAnimationEnd(animation: Animator?) {
                            animatingDown = false
                        }
                    })
                    .start()
                animatingDown = true
                animatingUp = false
            }

        }
    }
}