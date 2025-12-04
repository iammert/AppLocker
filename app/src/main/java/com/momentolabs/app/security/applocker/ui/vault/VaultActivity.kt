package com.momentolabs.app.security.applocker.ui.vault

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.momentolabs.app.security.applocker.R
import com.momentolabs.app.security.applocker.data.database.vault.VaultMediaType
import com.momentolabs.app.security.applocker.databinding.ActivityVaultBinding
import com.momentolabs.app.security.applocker.ui.BaseActivity
import com.momentolabs.app.security.applocker.ui.rateus.RateUsDialog
import com.momentolabs.app.security.applocker.ui.vault.addingvaultdialog.AddToVaultDialog
import com.momentolabs.app.security.applocker.ui.vault.intent.VaultSelectorIntentHelper
import com.momentolabs.app.security.applocker.util.helper.file.FilePathHelper
import com.tbruyelle.rxpermissions2.RxPermissions


class VaultActivity : BaseActivity<VaultViewModel>() {

    private lateinit var binding: ActivityVaultBinding

    override fun getViewModel(): Class<VaultViewModel> = VaultViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_vault)


        binding.viewPagerVault.adapter = VaultPagerAdapter(this, supportFragmentManager)
        binding.tabLayoutVault.setupWithViewPager(binding.viewPagerVault)
        binding.imageViewBack.setOnClickListener { finish() }
        binding.buttonAddToVault.setOnClickListener { addToVaultClicked() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                RC_VAULT_SELECTION_IMAGE -> {

                    val pathList = arrayListOf<String?>()

                    if (data?.data != null) {
                        pathList.add(FilePathHelper.getSafePath(this, data.data ?: Uri.EMPTY))
                    } else if (data?.clipData != null) {

                        val selectedItemCount = data.clipData?.itemCount ?: 0
                        for (i in 0 until selectedItemCount) {
                            val imageUri = data.clipData?.getItemAt(i)?.uri
                            pathList.add(FilePathHelper.getSafePath(this, imageUri ?: Uri.EMPTY))
                        }
                    }

                    if (pathList.isEmpty().not()) {
                        AddToVaultDialog
                            .newInstance(pathList, VaultMediaType.TYPE_IMAGE)
                            .apply { onDismissListener = { showRateUsDialog() } }
                            .also { it.show(supportFragmentManager, "") }
                    }
                }
                RC_VAULT_SELECTION_VIDEO -> {
                    val path = FilePathHelper.getSafePath(this, data?.data ?: Uri.EMPTY)
                    val paths = arrayListOf<String?>().also { it.add(path) }

                    path?.let {
                        AddToVaultDialog
                            .newInstance(paths, VaultMediaType.TYPE_VIDEO)
                            .apply { onDismissListener = { showRateUsDialog() } }
                            .also { it.show(supportFragmentManager, "") }
                    }
                }
            }
        }
    }

    @SuppressLint("CheckResult")
    private fun addToVaultClicked() {
        RxPermissions(this)
            .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .subscribe { granted ->
                if (granted) {
                    when (binding.viewPagerVault.currentItem) {
                        0 -> startActivityForResult(
                            VaultSelectorIntentHelper.selectMultipleImageIntent(),
                            RC_VAULT_SELECTION_IMAGE
                        )
                        1 -> startActivityForResult(
                            VaultSelectorIntentHelper.selectVideoIntent(),
                            RC_VAULT_SELECTION_VIDEO
                        )
                        else -> startActivityForResult(
                            VaultSelectorIntentHelper.selectMultipleImageIntent(),
                            RC_VAULT_SELECTION_IMAGE
                        )
                    }
                }
            }
    }

    private fun showRateUsDialog() {
        if (viewModel.shouldShowRateUs()) {
            RateUsDialog.newInstance().show(supportFragmentManager, "")
            viewModel.setRateUsAsked()
        }
    }


    companion object {

        private const val RC_VAULT_SELECTION_IMAGE = 101

        private const val RC_VAULT_SELECTION_VIDEO = 102

        fun newIntent(context: Context): Intent {
            return Intent(context, VaultActivity::class.java)
        }

    }

}