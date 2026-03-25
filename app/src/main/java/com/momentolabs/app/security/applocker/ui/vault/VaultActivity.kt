package com.momentolabs.app.security.applocker.ui.vault

import com.momentolabs.app.security.applocker.util.extensions.secureWindow
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import com.momentolabs.app.security.applocker.R
import com.momentolabs.app.security.applocker.data.database.vault.VaultMediaType
import com.momentolabs.app.security.applocker.databinding.ActivityVaultBinding
import com.momentolabs.app.security.applocker.ui.BaseActivity
import com.momentolabs.app.security.applocker.ui.rateus.RateUsDialog
import com.momentolabs.app.security.applocker.ui.vault.addingvaultdialog.AddToVaultDialog
import com.momentolabs.app.security.applocker.ui.vault.intent.VaultSelectorIntentHelper
import com.momentolabs.app.security.applocker.util.helper.file.FilePathHelper


class VaultActivity : BaseActivity<VaultViewModel>() {

    private lateinit var binding: ActivityVaultBinding

    override fun getViewModel(): Class<VaultViewModel> = VaultViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        secureWindow()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_vault)


        binding.viewPagerVault.adapter = VaultPagerAdapter(this, supportFragmentManager)
        binding.tabLayoutVault.setupWithViewPager(binding.viewPagerVault)
        binding.imageViewBack.setOnClickListener { finish() }
        binding.buttonAddToVault.setOnClickListener { addToVaultClicked() }
    }

    private val imagePickerLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val data = result.data
            if (result.resultCode != Activity.RESULT_OK || data == null) {
                return@registerForActivityResult
            }
            val uris = data.clipData
                ?.let { clipData -> (0 until clipData.itemCount).map { clipData.getItemAt(it).uri } }
                ?: listOfNotNull(data.data)
            showAddToVaultDialog(uris, VaultMediaType.TYPE_IMAGE)
        }

    private val videoPickerLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val uri = result.data?.data
            if (result.resultCode == Activity.RESULT_OK && uri != null) {
                showAddToVaultDialog(listOf(uri), VaultMediaType.TYPE_VIDEO)
            }
        }

    private fun showAddToVaultDialog(uris: List<Uri>, mediaType: VaultMediaType) {
        val paths = ArrayList(uris.mapNotNull { FilePathHelper.getSafePath(this, it) })
        if (paths.isEmpty()) {
            return
        }
        AddToVaultDialog
            .newInstance(ArrayList<String?>(paths), mediaType)
            .apply { onDismissListener = { showRateUsDialog() } }
            .also { it.show(supportFragmentManager, "") }
    }

    private val storagePermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results ->
            if (results.values.all { it }) {
                openMediaPicker()
            }
        }

    private fun addToVaultClicked() {
        storagePermissionLauncher.launch(mediaPermissions())
    }

    private fun mediaPermissions(): Array<String> = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> arrayOf(
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_VIDEO
        )
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        else -> arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    private fun openMediaPicker() {
        when (binding.viewPagerVault.currentItem) {
            1 -> videoPickerLauncher.launch(VaultSelectorIntentHelper.selectVideoIntent())
            else -> imagePickerLauncher.launch(VaultSelectorIntentHelper.selectMultipleImageIntent())
        }
    }

    private fun showRateUsDialog() {
        if (viewModel.shouldShowRateUs()) {
            RateUsDialog.newInstance().show(supportFragmentManager, "")
            viewModel.setRateUsAsked()
        }
    }

    companion object {

        fun newIntent(context: Context): Intent {
            return Intent(context, VaultActivity::class.java)
        }
    }
}
