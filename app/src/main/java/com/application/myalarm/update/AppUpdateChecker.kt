package com.application.myalarm.update

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.application.myalarm.BuildConfig
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.*
import java.io.IOException

@JsonClass(generateAdapter = true)
data class AppUpdateInfo(
    val latestVersionCode: Int,
    val latestVersionName: String,
    val apkUrl: String,
    val forceUpdate: Boolean,
    val releaseNotes: String? = null
)

object AppUpdateChecker {
    private const val TAG = "AppUpdateChecker"
    private val client = OkHttpClient()
    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()
    private val adapter = moshi.adapter(AppUpdateInfo::class.java)
    private val mainHandler = Handler(Looper.getMainLooper())

    fun checkForUpdate(context: Context, onResult: (AppUpdateInfo?) -> Unit) {
        val url = BuildConfig.UPDATE_JSON_URL
        if (url.isEmpty()) {
            onResult(null)
            return
        }

        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e(TAG, "Failed to fetch update JSON from $url", e)
                mainHandler.post { onResult(null) }
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) {
                        Log.e(TAG, "Unsuccessful response fetching update JSON: ${response.code}")
                        mainHandler.post { onResult(null) }
                        return
                    }

                    val bodyString = response.body?.string()
                    if (bodyString.isNullOrEmpty()) {
                        Log.e(TAG, "Empty response body fetching update JSON")
                        mainHandler.post { onResult(null) }
                        return
                    }

                    try {
                        val updateInfo = adapter.fromJson(bodyString)
                        if (updateInfo != null) {
                            val currentVersionCode = getAppVersionCode(context)
                            if (updateInfo.latestVersionCode > currentVersionCode) {
                                mainHandler.post { onResult(updateInfo) }
                                return
                            }
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "Failed to parse update JSON", e)
                    }
                    mainHandler.post { onResult(null) }
                }
            }
        })
    }

    fun downloadApk(
        context: Context,
        url: String,
        onProgress: (Int) -> Unit,
        onComplete: (java.io.File?) -> Unit
    ) {
        val request = Request.Builder().url(url).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e(TAG, "Failed to download APK", e)
                mainHandler.post { onComplete(null) }
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    Log.e(TAG, "Failed to download APK: HTTP ${response.code}")
                    mainHandler.post { onComplete(null) }
                    return
                }

                val body = response.body
                if (body == null) {
                    Log.e(TAG, "Empty response body for APK download")
                    mainHandler.post { onComplete(null) }
                    return
                }

                val directory = java.io.File(context.getExternalFilesDir(null), "updates")
                if (!directory.exists()) {
                    directory.mkdirs()
                }
                val apkFile = java.io.File(directory, "update.apk")

                try {
                    val totalBytes = body.contentLength()
                    var bytesDownloaded = 0L

                    body.byteStream().use { inputStream ->
                        java.io.FileOutputStream(apkFile).use { outputStream ->
                            val buffer = ByteArray(8192)
                            var bytesRead: Int
                            var lastProgress = -1

                            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                                outputStream.write(buffer, 0, bytesRead)
                                bytesDownloaded += bytesRead
                                if (totalBytes > 0) {
                                    val progress = ((bytesDownloaded * 100) / totalBytes).toInt()
                                    if (progress != lastProgress) {
                                        lastProgress = progress
                                        mainHandler.post { onProgress(progress) }
                                    }
                                }
                            }
                        }
                    }
                    mainHandler.post { onComplete(apkFile) }
                } catch (e: Exception) {
                    Log.e(TAG, "Exception downloading APK file", e)
                    mainHandler.post { onComplete(null) }
                }
            }
        })
    }

    private fun getAppVersionCode(context: Context): Long {
        return try {
            val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                pInfo.longVersionCode
            } else {
                @Suppress("DEPRECATION")
                pInfo.versionCode.toLong()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to get version code", e)
            0L
        }
    }
}
