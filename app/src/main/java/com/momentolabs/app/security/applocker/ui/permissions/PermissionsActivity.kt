package com.momentolabs.app.security.applocker.ui.permissions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import com.momentolabs.app.security.applocker.databinding.ActivityPermissionsBinding
import com.momentolabs.app.security.applocker.ui.BaseActivity
import android.provider.Settings
import com.momentolabs.app.security.applocker.R
import com.momentolabs.app.security.applocker.service.notification.ServiceNotificationManager
import com.momentolabs.app.security.applocker.util.extensions.toast
import javax.inject.Inject

class PermissionsActivity : BaseActivity<PermissionsViewModel>() {

    private lateinit var binding: ActivityPermissionsBinding

    @Inject
    lateinit var serviceNotificationManager: ServiceNotificationManager

    override fun getViewModel(): Class<PermissionsViewModel> = PermissionsViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_permissions)

        binding.switchUsageAccess.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked && PermissionChecker.checkUsageAccessPermission(this).not()) {
                startActivity(IntentHelper.usageAccessIntent())
            }
        }

        binding.switchOverlay.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked && PermissionChecker.checkOverlayPermission(this).not()) {
                overlayPermissionLauncher.launch(IntentHelper.overlayIntent(packageName))
            }
        }

        binding.buttonNext.setOnClickListener {
            if (PermissionChecker.isAllPermissionChecked(this).not()) {
                toast(R.string.permission_toast_enable_permissions)
            }
        }

        binding.textViewPrivacyPolicy.setOnClickListener {
            startActivity(IntentHelper.privacyPolicyWebIntent())
        }
    }

    private val overlayPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            binding.switchOverlay.isChecked = Settings.canDrawOverlays(this)
            onPermissionResult()
        }

    private fun onPermissionResult() {
        if (isAllPermissionsPermitted()) {
            serviceNotificationManager.hidePermissionNotification()
            setResult(Activity.RESULT_OK)
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        binding.switchUsageAccess.isChecked = PermissionChecker.checkUsageAccessPermission(this)

        if (isAllPermissionsPermitted()) {
            finish()
        }
    }

    private fun isAllPermissionsPermitted() =
        PermissionChecker.checkUsageAccessPermission(this) &&
                PermissionChecker.checkOverlayPermission(this)

    companion object {

        fun newIntent(context: Context): Intent {
            return Intent(context, PermissionsActivity::class.java)
        }
    }

}