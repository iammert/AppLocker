package com.momentolabs.app.security.applocker.ui.settings

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.momentolabs.app.security.applocker.R
import com.momentolabs.app.security.applocker.databinding.FragmentSettingsBinding
import com.momentolabs.app.security.applocker.ui.BaseFragment
import com.momentolabs.app.security.applocker.ui.intruders.IntrudersPhotosActivity
import com.momentolabs.app.security.applocker.ui.newpattern.CreateNewPatternActivity
import com.momentolabs.app.security.applocker.ui.permissiondialog.UsageAccessPermissionDialog
import com.momentolabs.app.security.applocker.ui.permissions.PermissionChecker
import com.momentolabs.app.security.applocker.ui.settings.analytics.SettingsAnalytics
import com.momentolabs.app.security.applocker.ui.vault.VaultActivity
import com.momentolabs.app.security.applocker.ui.vault.intent.VaultSelectorIntentHelper
import com.momentolabs.app.security.applocker.util.delegate.inflate
import com.momentolabs.app.security.applocker.util.extensions.toast
import com.tbruyelle.rxpermissions2.RxPermissions

class SettingsFragment : BaseFragment<SettingsViewModel>() {

    private val binding: FragmentSettingsBinding by inflate(R.layout.fragment_settings)

    override fun getViewModel(): Class<SettingsViewModel> = SettingsViewModel::class.java

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding.imageViewLockAll.setOnClickListener {
            activity?.let { activity ->
                if (PermissionChecker.checkUsageAccessPermission(activity).not()) {
                    UsageAccessPermissionDialog.newInstance()
                        .show(activity.supportFragmentManager, "")
                } else {
                    if (viewModel.isAllLocked()) {
                        viewModel.unlockAll()
                    } else {
                        viewModel.lockAll()
                    }
                }
            }
        }

        binding.layoutChangePattern.setOnClickListener {
            activity?.let {
                startActivityForResult(CreateNewPatternActivity.newIntent(it), RC_CHANGE_PATTERN)
            }
        }

        binding.switchStealth.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setHiddenDrawingMode(isChecked)
        }

        binding.switchFingerPrint.setOnCheckedChangeListener { buttonView, isChecked ->
            activity?.let { SettingsAnalytics.fingerPrintEnabled(it) }
            viewModel.setEnableFingerPrint(isChecked)
        }

        binding.switchEnableIntrudersCatcher.setOnCheckedChangeListener { buttonView, isChecked ->
            activity?.let { SettingsAnalytics.intrudersEnabled(it) }
            enableIntrudersCatcher(isChecked)
        }

        binding.layoutIntrudersFolder.setOnClickListener {
            activity?.let {
                if (viewModel.isIntrudersCatcherEnabled().not()) {
                    activity?.let { SettingsAnalytics.intrudersEnabled(it) }
                    enableIntrudersCatcher(true)
                } else {
                    SettingsAnalytics.intrudersFolderClicked(it)
                    startActivity(IntrudersPhotosActivity.newIntent(it))
                }
            }
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.getSettingsViewStateLiveData().observe(this, Observer {
            binding.viewState = it
        })

        viewModel.getFingerPrintStatusViewStateLiveData().observe(this, Observer {
            binding.fingerPrintStatus = it
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RC_CHANGE_PATTERN -> {
                if (resultCode == Activity.RESULT_OK) {
                    activity?.let { it.toast(R.string.message_pattern_changed) }
                }
            }
        }
    }

    @SuppressLint("CheckResult")
    private fun enableIntrudersCatcher(isChecked: Boolean) {
        if (isChecked) {
            RxPermissions(this)
                .request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe { granted ->
                    viewModel.setEnableIntrudersCatchers(granted)
                }
        } else {
            viewModel.setEnableIntrudersCatchers(false)
        }
    }

    companion object {

        private const val RC_CHANGE_PATTERN = 101
        fun newInstance() = SettingsFragment()
    }
}