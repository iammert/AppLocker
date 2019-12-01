package com.momentolabs.app.security.applocker.ui.callblocker

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.telecom.TelecomManager
import androidx.annotation.RequiresApi
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

        binding.imageViewBack.setOnClickListener { onBackPressed() }

        binding.buttonBlockNumber.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                setDefaulDialer()
            } else {
                showAddToBlacklistDialog()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setDefaulDialer() {
        if (getSystemService(TelecomManager::class.java).defaultDialerPackage != packageName) {
            Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER)
                .putExtra(TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, packageName)
                .also { startActivityForResult(it, CHANGE_DIALER_REQUEST_CODE) }
        } else {
            showAddToBlacklistDialog()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when {
            requestCode == CHANGE_DIALER_REQUEST_CODE && resultCode == Activity.RESULT_OK -> {
                showAddToBlacklistDialog()
            }
        }
    }

    private fun showAddToBlacklistDialog() {
        AddToBlackListDialog.newInstance().show(supportFragmentManager, "")
    }

    companion object {

        private const val CHANGE_DIALER_REQUEST_CODE = 1001

        fun newIntent(context: Context): Intent {
            return Intent(context, CallBlockerActivity::class.java)
        }
    }
}