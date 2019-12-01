package com.momentolabs.app.security.applocker.di.module

import com.momentolabs.app.security.applocker.di.scope.ActivityScope
import com.momentolabs.app.security.applocker.ui.background.BackgroundsActivity
import com.momentolabs.app.security.applocker.ui.browser.BrowserActivity
import com.momentolabs.app.security.applocker.ui.callblocker.CallBlockerActivity
import com.momentolabs.app.security.applocker.ui.intruders.IntrudersPhotosActivity
import com.momentolabs.app.security.applocker.ui.main.MainActivity
import com.momentolabs.app.security.applocker.ui.newpattern.CreateNewPatternActivity
import com.momentolabs.app.security.applocker.ui.overlay.activity.OverlayValidationActivity
import com.momentolabs.app.security.applocker.ui.permissions.PermissionsActivity
import com.momentolabs.app.security.applocker.ui.vault.VaultActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by mertsimsek on 12/11/2017.
 */
@Module
abstract class ActivityBuilderModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class, DialogFragmentBuilderModule::class])
    abstract fun mainActivity(): MainActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class, DialogFragmentBuilderModule::class])
    abstract fun backgroundActivity(): BackgroundsActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class, DialogFragmentBuilderModule::class])
    abstract fun browserActivity(): BrowserActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class, DialogFragmentBuilderModule::class])
    abstract fun vaultActivity(): VaultActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class, DialogFragmentBuilderModule::class])
    abstract fun callBlockerActivity(): CallBlockerActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class, DialogFragmentBuilderModule::class])
    abstract fun intrudersPhotosActivity(): IntrudersPhotosActivity

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun permissionsActivity(): PermissionsActivity

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun createNewPatternActivity(): CreateNewPatternActivity

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun fingerPrintOverlayActivity(): OverlayValidationActivity
}