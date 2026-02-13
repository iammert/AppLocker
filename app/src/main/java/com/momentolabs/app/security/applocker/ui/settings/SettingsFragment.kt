package com.momentolabs.app.security.applocker.ui.settings

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
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
import com.momentolabs.app.security.applocker.ui.vault.VaultActivity
import com.momentolabs.app.security.applocker.ui.vault.intent.VaultSelectorIntentHelper
import com.momentolabs.app.security.applocker.util.delegate.inflate
import com.momentolabs.app.security.applocker.util.extensions.toast

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
            viewModel.setEnableFingerPrint(isChecked)
        }

        binding.switchEnableIntrudersCatcher.setOnCheckedChangeListener { buttonView, isChecked ->
            enableIntrudersCatcher(isChecked)
        }

        binding.layoutIntrudersFolder.setOnClickListener {
            activity?.let {
                if (viewModel.isIntrudersCatcherEnabled().not()) {
                    enableIntrudersCatcher(true)
                } else {
                    startActivity(IntrudersPhotosActivity.newIntent(it))
                }
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getSettingsViewStateLiveData().observe(viewLifecycleOwner, Observer {
            binding.viewState = it
        })

        viewModel.getFingerPrintStatusViewStateLiveData().observe(viewLifecycleOwner, Observer {
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

    // Photos are stored in app specific storage, so only the camera permission is needed.
    private val intrudersPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            viewModel.setEnableIntrudersCatchers(granted)
        }

    private fun enableIntrudersCatcher(isChecked: Boolean) {
        if (isChecked) {
            intrudersPermissionLauncher.launch(Manifest.permission.CAMERA)
        } else {
            viewModel.setEnableIntrudersCatchers(false)
        }
    }

    companion object {

        private const val RC_CHANGE_PATTERN = 101
        fun newInstance() = SettingsFragment()
    }
}