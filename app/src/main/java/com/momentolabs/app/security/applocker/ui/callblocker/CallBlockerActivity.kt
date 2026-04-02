package com.momentolabs.app.security.applocker.ui.callblocker

import android.app.Activity
import android.app.role.RoleManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.telecom.TelecomManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import com.momentolabs.app.security.applocker.R
import com.momentolabs.app.security.applocker.data.database.callblocker.addtoblacklist.AddToBlackListDialog
import com.momentolabs.app.security.applocker.databinding.ActivityCallBlockerBinding
import com.momentolabs.app.security.applocker.ui.BaseActivity

class CallBlockerActivity : BaseActivity<CallBlockerViewModel>() {

    private lateinit var binding: ActivityCallBlockerBinding

    override fun getViewModel(): Class<CallBlockerViewModel> = CallBlockerViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_call_blocker)

        binding.viewPager.adapter = CallBlockerPagerAdapter(this, supportFragmentManager)
        binding.tablayout.setupWithViewPager(binding.viewPager)

        binding.imageViewBack.setOnClickListener { onBackPressedDispatcher.onBackPressed() }

        binding.buttonBlockNumber.setOnClickListener { requestCallScreening() }
    }

    private val callScreeningRequest =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                showAddToBlacklistDialog()
            }
        }

    /**
     * Calls only reach [CallBlockerScreeningService] when the app holds the call screening role
     * (Android 10 and later) or is the default dialer (Android 7 to 9).
     */
    private fun requestCallScreening() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val roleManager = getSystemService(RoleManager::class.java)
            if (roleManager.isRoleAvailable(RoleManager.ROLE_CALL_SCREENING) &&
                roleManager.isRoleHeld(RoleManager.ROLE_CALL_SCREENING).not()
            ) {
                callScreeningRequest.launch(roleManager.createRequestRoleIntent(RoleManager.ROLE_CALL_SCREENING))
                return
            }
        } else if (getSystemService(TelecomManager::class.java).defaultDialerPackage != packageName) {
            callScreeningRequest.launch(
                Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER)
                    .putExtra(TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, packageName)
            )
            return
        }
        showAddToBlacklistDialog()
    }

    private fun showAddToBlacklistDialog() {
        AddToBlackListDialog.newInstance().show(supportFragmentManager, "")
    }

    companion object {

        fun newIntent(context: Context): Intent {
            return Intent(context, CallBlockerActivity::class.java)
        }
    }
}