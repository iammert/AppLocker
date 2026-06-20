package com.momentolabs.app.security.applocker.ui.intruders.camera

import android.annotation.SuppressLint
import android.app.Application
import android.graphics.SurfaceTexture
import android.hardware.Camera
import androidx.lifecycle.MutableLiveData
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.io.FileOutputStream


class FrontPictureLiveData(val app: Application, private val destinationImageFile: File) :
    MutableLiveData<FrontPictureState>() {

    private enum class State {
        IDLE, TAKING, TAKEN, ERROR
    }

    private var camera: Camera? = null

    private var state: State = State.IDLE

    override fun onInactive() {
        super.onInactive()
        stopCamera()
    }

    fun takePicture() {
        if (state == State.TAKEN || state == State.TAKING) {
            return
        }

        state = State.TAKING
        startCamera()
        val openCamera = camera ?: return
        try {
            openCamera.takePicture(null, null) { data, _ ->
                // Release the camera right away, so the privacy indicator turns off and other
                // apps can use the camera again.
                stopCamera()
                savePicture(data)
            }
        } catch (exception: RuntimeException) {
            stopCamera()
            state = State.ERROR
            value = FrontPictureState.Error(exception)
        }
    }

    private fun startCamera() {
        val dummy = SurfaceTexture(0)

        try {
            val cameraId = getFrontCameraId()
            if (cameraId == NO_CAMERA_ID) {
                value = FrontPictureState.Error(IllegalStateException("No front camera found"))
                state = State.ERROR
                return
            }
            stopCamera()
            camera = Camera.open(cameraId).also {
                it.setPreviewTexture(dummy)
                it.startPreview()
            }
            value = FrontPictureState.Started()
        } catch (e: RuntimeException) {
            value = FrontPictureState.Error(e)
            state = State.ERROR
        }
    }

    private fun stopCamera() {
        val openCamera = camera ?: return
        camera = null
        openCamera.stopPreview()
        openCamera.release()
        value = FrontPictureState.Destroyed()
    }

    private fun getFrontCameraId(): Int {
        var camId =
            NO_CAMERA_ID
        val numberOfCameras = Camera.getNumberOfCameras()
        val ci = Camera.CameraInfo()

        for (i in 0 until numberOfCameras) {
            Camera.getCameraInfo(i, ci)
            if (ci.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                camId = i
            }
        }

        return camId
    }

    @SuppressLint("CheckResult")
    private fun savePicture(data: ByteArray) {
        Single
            .create<String> { emitter ->
                try {
                    FileOutputStream(destinationImageFile).use { it.write(data) }
                    emitter.onSuccess(destinationImageFile.absolutePath)
                } catch (e: Exception) {
                    emitter.onError(e)
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                { filePath ->
                    state = State.TAKEN
                    value = FrontPictureState.Taken(filePath)
                },
                {
                    state = State.ERROR
                    value = FrontPictureState.Error(it)
                })
    }

    companion object {
        private const val NO_CAMERA_ID = -1
    }

}