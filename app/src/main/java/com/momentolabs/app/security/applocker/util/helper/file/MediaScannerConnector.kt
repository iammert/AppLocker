package com.momentolabs.app.security.applocker.util.helper.file

import android.content.Context
import android.media.MediaScannerConnection
import android.net.Uri

class MediaScannerConnector(context: Context, private val filePath: String) :
    MediaScannerConnection.MediaScannerConnectionClient {

    private val connection: MediaScannerConnection = MediaScannerConnection(context, this)
        .apply {
            connect()
        }

    override fun onMediaScannerConnected() = connection.scanFile(filePath, null)

    override fun onScanCompleted(path: String?, uri: Uri?) = connection.disconnect()

    companion object {
        fun scan(context: Context, filePath: String) = MediaScannerConnector(context, filePath)
    }
}