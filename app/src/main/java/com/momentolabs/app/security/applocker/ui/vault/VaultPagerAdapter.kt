package com.momentolabs.app.security.applocker.ui.vault

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.momentolabs.app.security.applocker.R
import com.momentolabs.app.security.applocker.data.database.vault.VaultMediaType
import com.momentolabs.app.security.applocker.ui.vault.vaultlist.VaultListFragment

class VaultPagerAdapter(context: Context, manager: FragmentManager) : FragmentPagerAdapter(manager) {

    private val tabs = context.resources.getStringArray(R.array.vault_tabs)

    override fun getItem(position: Int): Fragment {
        return when (position) {
            INDEX_IMAGES -> VaultListFragment.newInstance(VaultMediaType.TYPE_IMAGE)
            INDEX_VIDEOS -> VaultListFragment.newInstance(VaultMediaType.TYPE_VIDEO)
            else -> VaultListFragment.newInstance(VaultMediaType.TYPE_IMAGE)
        }
    }

    override fun getCount(): Int = tabs.size

    override fun getPageTitle(position: Int): CharSequence? = tabs[position]

    companion object {

        private const val INDEX_IMAGES = 0
        private const val INDEX_VIDEOS = 1
    }
}