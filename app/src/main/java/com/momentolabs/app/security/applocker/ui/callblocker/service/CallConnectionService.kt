package com.momentolabs.app.security.applocker.ui.callblocker.service

import android.annotation.TargetApi
import android.os.Build
import android.telecom.InCallService

@TargetApi(Build.VERSION_CODES.M)
class CallService : InCallService()