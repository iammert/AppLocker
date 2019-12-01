package com.momentolabs.app.security.applocker.util.helper.progress

import android.os.Handler

class FakeProgress {
    private val FASTEST_LOADING_MS = 30L //100 milliseconds
    private val FAST_LOADING_MS = 1000L //1 seconds
    private val NORMAL_LOADING_MS = 4000L //4 seconds
    private val SLOW_LOADING_MS = 16000L // 16 seconds
    private val SLOWEST_LOADING_MS = 32000L // 32 seconds
    private val MOST_SLOWEST_LOADING_MS = 100000L //100 seconds
    private val INFINITE_SLOWEST_LOADING_MS = 500000L //500 seconds

    private val MAX_PROGRESS = 100
    private val INITIAL_PROGRESS = 0

    private var loadingSpeedMs: Long = FAST_LOADING_MS

    private var handler: Handler = Handler()

    private var progress = INITIAL_PROGRESS

    private var isCompleted = false

    private var isProgressing = false

    private var isCancelled = false

    private var onProgress: ((Int) -> Unit)? = null

    private var onCompleted: (() -> Unit)? = null

    private var onFail: ((Throwable) -> Unit)? = null

    private var onCancelled: (() -> Unit)? = null

    fun setOnProgressListener(onProgress: (Int) -> Unit) {
        this.onProgress = onProgress
    }

    fun setOnCompletedListener(onCompleted: () -> Unit) {
        this.onCompleted = onCompleted
    }

    fun setOnFailListener(onFail: (Throwable) -> Unit) {
        this.onFail = onFail
    }

    fun setOnCancelledListener(onCancelled: () -> Unit) {
        this.onCancelled = onCancelled
    }

    fun isProgressing() = isProgressing

    fun start() {
        if (!isProgressing) {
            reinitializeValues()
            isProgressing = true
            handler.postDelayed(runnable, FAST_LOADING_MS)
        }
    }

    fun complete() {
        isCompleted = true
        loadingSpeedMs = FASTEST_LOADING_MS
    }

    fun completeImmediate() {
        isCompleted = true
        progress = MAX_PROGRESS
    }

    fun cancel() {
        isCompleted = true
        isCancelled = true
        progress = MAX_PROGRESS
        onCancelled?.invoke()
    }

    fun fail(fFmpegError: Throwable) {
        onFail?.invoke(fFmpegError)
        handler.removeCallbacks(runnable)
    }

    private fun reinitializeValues() {
        progress = INITIAL_PROGRESS
        loadingSpeedMs = FAST_LOADING_MS
        isCompleted = false
        isCancelled = false
        isProgressing = false
    }

    private var runnable: Runnable = object : Runnable {
        override fun run() {
            if (progress >= MAX_PROGRESS) {
                if (isCancelled.not()) {
                    onCompleted?.invoke()
                }
                handler.removeCallbacks(this)
                reinitializeValues()
                return
            }

            loadingSpeedMs = when {
                isCompleted -> FASTEST_LOADING_MS
                progress > 95 -> INFINITE_SLOWEST_LOADING_MS
                progress > 90 -> MOST_SLOWEST_LOADING_MS
                progress > 85 -> SLOWEST_LOADING_MS
                progress > 70 -> SLOW_LOADING_MS
                progress > 40 -> NORMAL_LOADING_MS
                else -> FAST_LOADING_MS
            }

            progress += 1


            onProgress?.invoke(progress)
            handler.postDelayed(this, loadingSpeedMs)
        }
    }
}