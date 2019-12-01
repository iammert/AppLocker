package com.momentolabs.app.security.applocker.ui.main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.momentolabs.app.security.applocker.R
import com.momentolabs.app.security.applocker.ui.background.BackgroundsFragment
import com.momentolabs.app.security.applocker.ui.security.SecurityFragment
import com.momentolabs.app.security.applocker.ui.settings.SettingsFragment

class MainPagerAdapter(context: Context, manager: FragmentManager) : FragmentPagerAdapter(manager) {

    private val tabs = context.resources.getStringArray(R.array.tabs)

    override fun getItem(position: Int): Fragment {
        return when (position) {
            INDEX_SECURITY -> SecurityFragment.newInstance()
            INDEX_SETTINGS -> SettingsFragment.newInstance()
            else -> SecurityFragment.newInstance()
        }
    }

    override fun getCount(): Int = tabs.size

    override fun getPageTitle(position: Int): CharSequence? = tabs[position]

    companion object {

        private const val INDEX_SECURITY = 0
        private const val INDEX_SETTINGS = 1
    }
}