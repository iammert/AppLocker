package com.momentolabs.app.security.applocker.ui.security

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.google.android.gms.ads.*
import com.momentolabs.app.security.applocker.R
import com.momentolabs.app.security.applocker.databinding.FragmentSecurityBinding
import com.momentolabs.app.security.applocker.ui.BaseFragment
import com.momentolabs.app.security.applocker.ui.background.BackgroundsActivity
import com.momentolabs.app.security.applocker.ui.browser.BrowserActivity
import com.momentolabs.app.security.applocker.ui.callblocker.CallBlockerActivity
import com.momentolabs.app.security.applocker.ui.permissiondialog.UsageAccessPermissionDialog
import com.momentolabs.app.security.applocker.ui.permissions.PermissionChecker
import com.momentolabs.app.security.applocker.ui.security.analytics.SecurityFragmentAnalytics
import com.momentolabs.app.security.applocker.ui.vault.VaultActivity
import com.momentolabs.app.security.applocker.ui.vault.analytics.VaultAdAnalytics
import com.momentolabs.app.security.applocker.util.ads.AdTestDevices
import com.momentolabs.app.security.applocker.util.delegate.inflate

class SecurityFragment : BaseFragment<SecurityViewModel>() {

    private val binding: FragmentSecurityBinding by inflate(R.layout.fragment_security)

    private val adapter: AppLockListAdapter = AppLockListAdapter()

    override fun getViewModel(): Class<SecurityViewModel> = SecurityViewModel::class.java

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding.recyclerViewAppLockList.adapter = adapter

        binding.recyclerViewAppLockList.addOnScrollListener(
            MainActionsShowHideScrollListener(
                binding.layoutMainActions.root,
                resources.getDimension(R.dimen.main_actions_size) + resources.getDimension(R.dimen.margin_16dp)
            )
        )

        binding.layoutMainActions.layoutCallBlocker.setOnClickListener {
            activity?.let {
                startActivity(CallBlockerActivity.newIntent(it))
                SecurityFragmentAnalytics.onCallBlockerClicked(it)
            }
        }
        binding.layoutMainActions.layoutTheme.setOnClickListener {
            activity?.let {
                startActivity(BackgroundsActivity.newIntent(it))
                SecurityFragmentAnalytics.onBackgroundClicked(it)
            }
        }

        binding.layoutMainActions.layoutBrowser.setOnClickListener {
            activity?.let {
                startActivity(BrowserActivity.newIntent(it))
                SecurityFragmentAnalytics.onBrowserClicked(it)
            }
        }

        binding.layoutMainActions.layoutVault.setOnClickListener {
            activity?.let {
                startActivity(VaultActivity.newIntent(it))
                SecurityFragmentAnalytics.onVaultClicked(it)
            }
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        showBannerAd()

        viewModel.getAppDataListLiveData().observe(this, Observer {
            adapter.setAppDataList(it)
        })

        adapter.appItemClicked = this@SecurityFragment::onAppSelected
    }

    private fun onAppSelected(selectedApp: AppLockItemItemViewState) {
        activity?.let {
            if (PermissionChecker.checkUsageAccessPermission(it).not()) {
                UsageAccessPermissionDialog.newInstance().show(it.supportFragmentManager, "")
            } else {
                if (selectedApp.isLocked) {
                    activity?.let { SecurityFragmentAnalytics.onAppUnlocked(it) }
                    viewModel.unlockApp(selectedApp)
                } else {
                    activity?.let { SecurityFragmentAnalytics.onAppLocked(it) }
                    viewModel.lockApp(selectedApp)
                }
            }
        }
    }

    private fun showBannerAd() {
        activity?.let { MobileAds.initialize(activity) }

        activity?.let {
            val mAdView = AdView(it).apply {
                adSize = AdSize.BANNER
                adUnitId = getString(R.string.dashboard_banner_ad_unit_id)
                adListener = object : AdListener() {
                    override fun onAdClicked() {
                        super.onAdClicked()
                        VaultAdAnalytics.bannerAdClicked(it)
                    }

                    override fun onAdFailedToLoad(p0: Int) {
                        super.onAdFailedToLoad(p0)
                        VaultAdAnalytics.bannerAdFailed(it)
                    }

                    override fun onAdLoaded() {
                        super.onAdLoaded()
                        VaultAdAnalytics.bannerAdLoaded(it)
                    }
                }
            }

            binding.adLayout.addView(mAdView)
            val adRequestBuilder = AdRequest.Builder()
            AdTestDevices.DEVICES.forEach {
                adRequestBuilder.addTestDevice(it)
            }
            mAdView.loadAd(adRequestBuilder.build())
        }
    }

    companion object {
        fun newInstance() = SecurityFragment()
    }
}