package com.example.samadhannepalapp.view

import android.content.Context
import android.net.Uri
import android.util.Log
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import java.util.*
import com.cloudinary.android.callback.UploadCallback
object CloudinaryManager {

    // Initialize Cloudinary
    fun init(context: Context) {
        // Replace these with your Cloudinary credentials
        val config = HashMap<String, String>()
        config["cloud_name"] = "duuf9qvyf"
        config["api_key"] = "927873781144973"
        config["api_secret"] = "-zNJWSsy62ZJlTi92W8Nok1Cm1k"
        config["secure"] = "true"

        MediaManager.init(context, config)
    }

    // Upload image from Uri
    fun uploadImage(
        imageUri: Uri,
        onSuccess: (url: String) -> Unit,
        onError: (error: String) -> Unit
    ) {
        MediaManager.get().upload(imageUri)
            .option("folder", "SamadhanNepal")
            .callback(object : UploadCallback {
                override fun onStart(requestId: String) {
                    Log.d("Cloudinary", "Upload started: $requestId")
                }

                override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {
                    val progress = (bytes.toDouble() / totalBytes.toDouble() * 100).toInt()
                    Log.d("Cloudinary", "Upload progress: $progress%")
                }

                override fun onSuccess(requestId: String, resultData: MutableMap<Any?, Any?>?) {
                    val url = resultData?.get("secure_url")?.toString()
                    if (url != null) {
                        onSuccess(url)
                    } else {
                        onError("Failed to get URL")
                    }
                }

                override fun onError(requestId: String, error: ErrorInfo) {
                    onError(error.description)
                }

                override fun onReschedule(requestId: String, error: ErrorInfo) {
                    Log.d("Cloudinary", "Rescheduled upload: ${error.description}")
                }
            })
            .dispatch()
    }
}