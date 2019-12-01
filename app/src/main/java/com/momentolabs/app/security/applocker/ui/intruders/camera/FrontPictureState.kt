package com.momentolabs.app.security.applocker.ui.intruders.camera

sealed class FrontPictureState {
    class Taken(val filePath: String) : FrontPictureState()
    class Error(val error: Throwable) : FrontPictureState()
    class Started() : FrontPictureState()
    class Destroyed() : FrontPictureState()
}