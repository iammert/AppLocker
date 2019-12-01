package com.momentolabs.app.security.applocker.ui.overlay.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import androidx.databinding.DataBindingUtil
import com.andrognito.patternlockview.PatternLockView
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.momentolabs.app.security.applocker.R
import com.momentolabs.app.security.applocker.databinding.ViewPatternOverlayBinding
import com.momentolabs.app.security.applocker.ui.newpattern.SimplePatternListener
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.momentolabs.app.security.applocker.data.AppLockerPreferences
import com.momentolabs.app.security.applocker.ui.background.GradientBackgroundDataProvider
import com.momentolabs.app.security.applocker.ui.overlay.OverlayValidateType
import com.momentolabs.app.security.applocker.ui.overlay.OverlayViewState

class PatternOverlayView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private var onPatternCompleted: ((List<PatternLockView.Dot>) -> Unit)? = null

    private var appLockerPreferences = AppLockerPreferences(context.applicationContext)

    val binding: ViewPatternOverlayBinding =
        DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.view_pattern_overlay, this, true)

    init {
        binding.patternLockView.addPatternLockListener(object : SimplePatternListener() {
            override fun onComplete(pattern: MutableList<PatternLockView.Dot>?) {
                super.onComplete(pattern)
                pattern?.let { onPatternCompleted?.invoke(it) }
            }
        })
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        updateSelectedBackground()
        binding.patternLockView.clearPattern()
        binding.viewState = OverlayViewState()
    }

    fun observePattern(onPatternCompleted: (List<PatternLockView.Dot>) -> Unit) {
        this.onPatternCompleted = onPatternCompleted
    }

    fun notifyDrawnWrong() {
        binding.patternLockView.clearPattern()
        binding.viewState =
            OverlayViewState(
                overlayValidateType = OverlayValidateType.TYPE_PATTERN,
                isDrawnCorrect = false
            )
        YoYo.with(Techniques.Shake)
            .duration(700)
            .playOn(binding.textViewPrompt)
    }

    fun notifyDrawnCorrect() {
        binding.patternLockView.clearPattern()
        binding.viewState =
            OverlayViewState(
                overlayValidateType = OverlayValidateType.TYPE_PATTERN,
                isDrawnCorrect = true
            )
    }

    fun setHiddenDrawingMode(isHiddenDrawingMode: Boolean) {
        binding.patternLockView.isInStealthMode = isHiddenDrawingMode
    }

    fun setAppPackageName(appPackageName: String) {
        try {
            val icon = context.packageManager.getApplicationIcon(appPackageName)
            binding.avatarLock.setImageDrawable(icon)
        } catch (e: PackageManager.NameNotFoundException) {
            binding.avatarLock.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_round_lock_24px))
            e.printStackTrace()
        }
    }

    private fun updateSelectedBackground() {
        val selectedBackgroundId = appLockerPreferences.getSelectedBackgroundId()
        GradientBackgroundDataProvider.gradientViewStateList.forEach {
            if (it.id == selectedBackgroundId) {
                binding.layoutOverlayMain.background = it.getGradiendDrawable(context)
            }
        }
    }
}