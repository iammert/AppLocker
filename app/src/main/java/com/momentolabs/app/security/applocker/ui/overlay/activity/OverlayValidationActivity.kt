package com.momentolabs.app.security.applocker.ui.overlay.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.andrognito.patternlockview.PatternLockView
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.momentolabs.app.security.applocker.R
import com.momentolabs.app.security.applocker.databinding.ActivityOverlayValidationBinding
import com.momentolabs.app.security.applocker.ui.BaseActivity
import com.momentolabs.app.security.applocker.ui.intruders.camera.FrontPictureLiveData
import com.momentolabs.app.security.applocker.ui.intruders.camera.FrontPictureState
import com.momentolabs.app.security.applocker.ui.newpattern.SimplePatternListener
import com.momentolabs.app.security.applocker.util.extensions.convertToPatternDot
import com.momentolabs.app.security.applocker.util.helper.file.FileManager
import javax.inject.Inject


class OverlayValidationActivity : BaseActivity<OverlayValidationViewModel>() {

    @Inject
    lateinit var fileManager: FileManager

    private lateinit var frontPictureLiveData: FrontPictureLiveData

    private lateinit var binding: ActivityOverlayValidationBinding

    private var biometricAuthenticator: BiometricAuthenticator? = null

    override fun getViewModel(): Class<OverlayValidationViewModel> =
        OverlayValidationViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_overlay_validation)

        updateLaunchingAppIcon(intent.getStringExtra(KEY_PACKAGE_NAME))

        frontPictureLiveData = FrontPictureLiveData(application, viewModel.getIntruderPictureImageFile())

        if (viewModel.isFingerPrintEnabled()) {
            biometricAuthenticator = BiometricAuthenticator(this, viewModel::onFingerPrintResult)
        }

        viewModel.getViewStateObservable().observe(this, Observer {
            binding.patternLockView.clearPattern()
            binding.viewState = it
            binding.executePendingBindings()

            if (it.isDrawnCorrect == true || it.fingerPrintResultData?.isSucces() == true) {
                setResult(Activity.RESULT_OK)
                finish()
            }

            if (it.isDrawnCorrect == false || it.fingerPrintResultData?.isNotSucces() == true) {
                YoYo.with(Techniques.Shake)
                    .duration(700)
                    .playOn(binding.textViewPrompt)

                if (it.isIntrudersCatcherMode) {
                    frontPictureLiveData.takePicture()
                }
            }
        })

        viewModel.getBackgroundDrawableLiveData().observe(this, Observer {
            binding.layoutOverlayMain.background = it.getGradiendDrawable(this)
        })

        binding.patternLockView.addPatternLockListener(object : SimplePatternListener() {
            override fun onComplete(pattern: MutableList<PatternLockView.Dot>?) {
                super.onComplete(pattern)
                pattern?.let { viewModel.onPatternDrawn(it.convertToPatternDot()) }
            }
        })

        // The picture is only taken while the live data has an active observer.
        frontPictureLiveData.observe(this, Observer { })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent("android.intent.action.MAIN")
        intent.addCategory("android.intent.category.HOME")
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }


    override fun onResume() {
        super.onResume()
        biometricAuthenticator?.authenticate()
    }

    override fun onPause() {
        biometricAuthenticator?.cancel()
        super.onPause()
    }

    private fun updateLaunchingAppIcon(appPackageName: String) {
        try {
            val icon = packageManager.getApplicationIcon(appPackageName)
            binding.avatarLock.setImageDrawable(icon)
        } catch (e: PackageManager.NameNotFoundException) {
            binding.avatarLock.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.ic_round_lock_24px
                )
            )
            e.printStackTrace()
        }
    }

    companion object {

        private const val KEY_PACKAGE_NAME = "KEY_PACKAGE_NAME"

        fun newIntent(context: Context, packageName: String): Intent {
            val intent = Intent(context, OverlayValidationActivity::class.java)
            intent.putExtra(KEY_PACKAGE_NAME, packageName)
            return intent
        }
    }

}