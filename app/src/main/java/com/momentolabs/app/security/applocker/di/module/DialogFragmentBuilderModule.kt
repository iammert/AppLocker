package com.momentolabs.app.security.applocker.di.module

import com.iammert.hdwallpapers.di.scope.FragmentScope
import com.momentolabs.app.security.applocker.data.database.callblocker.addtoblacklist.AddToBlackListDialog
import com.momentolabs.app.security.applocker.ui.browser.bookmarks.BookmarksDialog
import com.momentolabs.app.security.applocker.ui.callblocker.blacklist.delete.BlackListItemDeleteDialog
import com.momentolabs.app.security.applocker.ui.permissiondialog.UsageAccessPermissionDialog
import com.momentolabs.app.security.applocker.ui.policydialog.PrivacyPolicyDialog
import com.momentolabs.app.security.applocker.ui.rateus.RateUsDialog
import com.momentolabs.app.security.applocker.ui.vault.addingvaultdialog.AddToVaultDialog
import com.momentolabs.app.security.applocker.ui.vault.removingvaultdialog.RemoveFromVaultDialog
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class DialogFragmentBuilderModule {

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun rateUsDialogFragment(): RateUsDialog

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun permissionDialogFragment(): UsageAccessPermissionDialog

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun privacyPolicyDialogFragment(): PrivacyPolicyDialog

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun bookmarksDialogFragment(): BookmarksDialog

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun addToVaultDialog(): AddToVaultDialog

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun addToBlackListDialog(): AddToBlackListDialog

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun deleteBlackListItemDialog(): BlackListItemDeleteDialog

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun removeFromVaultDialog(): RemoveFromVaultDialog
}