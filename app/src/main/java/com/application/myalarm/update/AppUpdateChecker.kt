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
