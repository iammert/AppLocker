package com.momentolabs.app.security.applocker.ui.main

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import android.content.Intent
import android.os.Bundle
import com.google.android.material.navigation.NavigationView
import androidx.core.view.GravityCompat
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.android.material.tabs.TabLayout
import com.momentolabs.app.security.applocker.R
import com.momentolabs.app.security.applocker.databinding.ActivityMainBinding
import com.momentolabs.app.security.applocker.ui.BaseActivity
import com.momentolabs.app.security.applocker.ui.newpattern.CreateNewPatternActivity
import com.momentolabs.app.security.applocker.ui.overlay.activity.OverlayValidationActivity
import com.momentolabs.app.security.applocker.ui.permissions.PermissionChecker
import com.momentolabs.app.security.applocker.ui.permissions.PermissionsActivity
import com.momentolabs.app.security.applocker.ui.policydialog.PrivacyPolicyDialog
import com.momentolabs.app.security.applocker.ui.rateus.RateUsDialog
import com.momentolabs.app.security.applocker.util.helper.NavigationIntentHelper

class MainActivity : BaseActivity<MainViewModel>(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding

    override fun getViewModel(): Class<MainViewModel> = MainViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.viewPager.adapter = MainPagerAdapter(this, supportFragmentManager)
        binding.tablayout.setupWithViewPager(binding.viewPager)

        binding.imageViewMenu.setOnClickListener { binding.drawerLayout.openDrawer(GravityCompat.START) }

        binding.navView.setNavigationItemSelectedListener(this)

        viewModel.getPatternCreationNeedLiveData().observe(this, Observer { isPatternCreateNeed ->
            when {
                isPatternCreateNeed -> createPatternLauncher.launch(CreateNewPatternActivity.newIntent(this))
                viewModel.isAppLaunchValidated().not() -> validatePatternLauncher.launch(
                    OverlayValidationActivity.newIntent(this, this.packageName)
                )
            }
        })
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_share -> startActivity(NavigationIntentHelper.getShareAppIntent())
            R.id.nav_rate_us -> startActivity(NavigationIntentHelper.getRateAppIntent())
            R.id.nav_feedback -> startActivity(NavigationIntentHelper.getFeedbackIntent())
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private val createPatternLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                onAppUnlocked()
            } else {
                finish()
            }
        }

    private val validatePatternLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                onAppUnlocked()
            } else {
                finish()
            }
        }

    private fun onAppUnlocked() {
        viewModel.onAppLaunchValidated()
        showPrivacyPolicyIfNeeded()
        requestNotificationPermissionIfNeeded()
    }

    private val notificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { }

    /** The protection notification and the permission reminder need this on Android 13. */
    private fun requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        ) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    private fun showPrivacyPolicyIfNeeded() {
        if (viewModel.isPrivacyPolicyAccepted().not()) {
            PrivacyPolicyDialog.newInstance().show(supportFragmentManager, "")
        }
    }
}
