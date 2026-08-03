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
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import android.content.Intent
import android.content.pm.PackageManager

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

    private val defaultDevUpdateInfo = AppUpdateInfo(
        latestVersionCode = 16,
        latestVersionName = "2.7.2.0-DEV",
        apkUrl = "https://github.com/aljayvee/MyAlarm/releases/download/v2.7.2.0-dev/alarm-dev.apk",
        forceUpdate = false,
        releaseNotes = "Debug Updates:\n• Fixes issue where the update version was not applying.\n• Updated toolchain specs and Gradle daemon criteria.\n• Dynamic release notes and version matching in update dialogs."
    )

    // Background update states & info (only debug gets a local fallback for offline development)
    val fetchedUpdateInfo = mutableStateOf<AppUpdateInfo?>(
        if (BuildConfig.BUILD_TYPE == "debug") defaultDevUpdateInfo else null
    )
    val updateAvailableInBackground = mutableStateOf(BuildConfig.BUILD_TYPE == "debug")
    
    // Download simulation states
    enum class DownloadState {
        IDLE, DOWNLOADING, PAUSED, DOWNLOADED
    }
    
    val downloadState = mutableStateOf(DownloadState.IDLE)
    val downloadProgress = mutableStateOf(0)
    
    private var downloadJob: Job? = null

    fun startRealDownload(context: Context) {
        val info = fetchedUpdateInfo.value ?: return
        val url = info.apkUrl
        if (url.isEmpty()) return

        downloadState.value = DownloadState.DOWNLOADING
        downloadProgress.value = 0

        downloadApk(
            context = context,
            url = url,
            onProgress = { progress ->
                downloadProgress.value = progress
            },
            onComplete = { apkFile ->
                if (apkFile != null && apkFile.exists()) {
                    downloadProgress.value = 100
                    downloadState.value = DownloadState.DOWNLOADED
                } else {
                    downloadState.value = DownloadState.IDLE
                    downloadProgress.value = 0
                }
            }
        )
    }

    fun openInBrowser(context: Context, url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, android.net.Uri.parse(url)).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to open URL in browser", e)
        }
    }
    
    fun startSimulationDownload(scope: CoroutineScope) {
        downloadState.value = DownloadState.DOWNLOADING
        downloadJob = scope.launch {
            while (downloadProgress.value < 100) {
                delay(300)
                if (downloadState.value == DownloadState.DOWNLOADING) {
                    downloadProgress.value = (downloadProgress.value + 5).coerceAtMost(100)
                    if (downloadProgress.value == 100) {
                        downloadState.value = DownloadState.DOWNLOADED
                        break
                    }
                } else {
                    break
                }
            }
        }
    }
    
    fun pauseSimulationDownload() {
        downloadState.value = DownloadState.PAUSED
        downloadJob?.cancel()
    }
    
    fun resumeSimulationDownload(scope: CoroutineScope) {
        startSimulationDownload(scope)
    }

    fun resetSimulation() {
        downloadState.value = DownloadState.IDLE
        downloadProgress.value = 0
        downloadJob?.cancel()
    }

    private val client = OkHttpClient()
    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()
    private val adapter = moshi.adapter(AppUpdateInfo::class.java)
    private val mainHandler = Handler(Looper.getMainLooper())

    fun checkForUpdate(context: Context, onResult: (AppUpdateInfo?) -> Unit) {
        val url = BuildConfig.UPDATE_JSON_URL
        val fallback = if (BuildConfig.BUILD_TYPE == "debug") defaultDevUpdateInfo else null
        
        if (url.isEmpty()) {
            fetchedUpdateInfo.value = fallback
            val currentVersionCode = getAppVersionCode(context)
            val isAvailable = fallback != null && fallback.latestVersionCode > currentVersionCode
            updateAvailableInBackground.value = isAvailable
            onResult(if (isAvailable) fallback else null)
            return
        }

        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e(TAG, "Failed to fetch update JSON from $url", e)
                mainHandler.post {
                    fetchedUpdateInfo.value = fallback
                    val currentVersionCode = getAppVersionCode(context)
                    val isAvailable = fallback != null && fallback.latestVersionCode > currentVersionCode
                    updateAvailableInBackground.value = isAvailable
                    onResult(if (isAvailable) fallback else null)
                }
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) {
                        Log.e(TAG, "Unsuccessful response fetching update JSON: ${response.code}")
                        mainHandler.post {
                            fetchedUpdateInfo.value = fallback
                            val currentVersionCode = getAppVersionCode(context)
                            val isAvailable = fallback != null && fallback.latestVersionCode > currentVersionCode
                            updateAvailableInBackground.value = isAvailable
                            onResult(if (isAvailable) fallback else null)
                        }
                        return
                    }

                    val bodyString = response.body?.string()
                    if (bodyString.isNullOrEmpty()) {
                        Log.e(TAG, "Empty response body fetching update JSON")
                        mainHandler.post {
                            fetchedUpdateInfo.value = fallback
                            val currentVersionCode = getAppVersionCode(context)
                            val isAvailable = fallback != null && fallback.latestVersionCode > currentVersionCode
                            updateAvailableInBackground.value = isAvailable
                            onResult(if (isAvailable) fallback else null)
                        }
                        return
                    }

                    try {
                        val updateInfo = adapter.fromJson(bodyString)
                        if (updateInfo != null) {
                            fetchedUpdateInfo.value = updateInfo
                            val currentVersionCode = getAppVersionCode(context)
                            val isAvailable = updateInfo.latestVersionCode > currentVersionCode
                            updateAvailableInBackground.value = isAvailable
                            mainHandler.post { onResult(if (isAvailable) updateInfo else null) }
                            return
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "Failed to parse update JSON", e)
                    }
                    mainHandler.post {
                        fetchedUpdateInfo.value = fallback
                        val currentVersionCode = getAppVersionCode(context)
                        val isAvailable = fallback != null && fallback.latestVersionCode > currentVersionCode
                        updateAvailableInBackground.value = isAvailable
                        onResult(if (isAvailable) fallback else null)
                    }
                }
            }
        })
    }

    val downloadSpeedText = mutableStateOf("")

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
                mainHandler.post {
                    downloadSpeedText.value = ""
                    onComplete(null)
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    Log.e(TAG, "Failed to download APK: HTTP ${response.code}")
                    mainHandler.post {
                        downloadSpeedText.value = ""
                        onComplete(null)
                    }
                    return
                }

                val body = response.body
                if (body == null) {
                    Log.e(TAG, "Empty response body for APK download")
                    mainHandler.post {
                        downloadSpeedText.value = ""
                        onComplete(null)
                    }
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
                    var lastTime = System.currentTimeMillis()
                    var bytesInInterval = 0L

                    body.byteStream().use { inputStream ->
                        java.io.FileOutputStream(apkFile).use { outputStream ->
                            val buffer = ByteArray(8192)
                            var bytesRead: Int
                            var lastProgress = -1

                            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                                outputStream.write(buffer, 0, bytesRead)
                                bytesDownloaded += bytesRead
                                bytesInInterval += bytesRead

                                val currentTime = System.currentTimeMillis()
                                val timeDiff = currentTime - lastTime
                                if (timeDiff >= 500) {
                                    val speedBytesPerSec = (bytesInInterval * 1000.0) / timeDiff
                                    val speedStr = if (speedBytesPerSec >= 1024 * 1024) {
                                        String.format(java.util.Locale.US, "%.1f MB/s", speedBytesPerSec / (1024.0 * 1024.0))
                                    } else {
                                        String.format(java.util.Locale.US, "%.0f KB/s", speedBytesPerSec / 1024.0)
                                    }
                                    mainHandler.post { downloadSpeedText.value = speedStr }
                                    lastTime = currentTime
                                    bytesInInterval = 0L
                                }

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
                    mainHandler.post {
                        downloadSpeedText.value = ""
                        onComplete(apkFile)
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Exception downloading APK file", e)
                    mainHandler.post {
                        downloadSpeedText.value = ""
                        onComplete(null)
                    }
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

    fun verifyAndInstallApk(context: Context, apkFile: java.io.File): Boolean {
        try {
            val pm = context.packageManager
            
            // 1. Get package info of the downloaded APK
            val flags = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                PackageManager.GET_SIGNING_CERTIFICATES
            } else {
                @Suppress("DEPRECATION")
                PackageManager.GET_SIGNATURES
            }
            val archiveInfo = pm.getPackageArchiveInfo(apkFile.absolutePath, flags)
            if (archiveInfo == null) {
                Log.e(TAG, "Failed to parse downloaded APK package info")
                return false
            }
            
            // 2. Verify Package Name matches
            val currentPackageName = context.packageName
            if (archiveInfo.packageName != currentPackageName) {
                Log.e(TAG, "Package name mismatch: downloaded ${archiveInfo.packageName} vs installed $currentPackageName")
                return false
            }
            
            // 3. Verify Signatures match
            val currentInfo = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                pm.getPackageInfo(currentPackageName, PackageManager.GET_SIGNING_CERTIFICATES)
            } else {
                @Suppress("DEPRECATION")
                pm.getPackageInfo(currentPackageName, PackageManager.GET_SIGNATURES)
            }
            
            val archiveSignatures = getSignatures(archiveInfo)
            val currentSignatures = getSignatures(currentInfo)
            
            if (archiveSignatures.isEmpty() || currentSignatures.isEmpty()) {
                Log.e(TAG, "Failed to retrieve signatures for verification")
                return false
            }
            
            // Compare signatures
            val signaturesMatch = archiveSignatures.any { archiveSig ->
                currentSignatures.any { currentSig ->
                    archiveSig.contentEquals(currentSig)
                }
            }
            
            if (!signaturesMatch) {
                Log.e(TAG, "Signature verification failed! APK signature does not match currently installed app.")
                return false
            }
            
            Log.d(TAG, "APK verification successful! Launching package installer...")
            
            // 4. Launch installation intent
            val apkUri = androidx.core.content.FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                apkFile
            )
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(apkUri, "application/vnd.android.package-archive")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(intent)
            return true
        } catch (e: Exception) {
            Log.e(TAG, "Error during APK verification and installation", e)
            return false
        }
    }
    
    private fun getSignatures(packageInfo: android.content.pm.PackageInfo): List<ByteArray> {
        val sigList = mutableListOf<ByteArray>()
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            val signingInfo = packageInfo.signingInfo
            if (signingInfo != null) {
                if (signingInfo.hasMultipleSigners()) {
                    signingInfo.apkContentsSigners?.forEach {
                        sigList.add(it.toByteArray())
                    }
                } else {
                    signingInfo.signingCertificateHistory?.forEach {
                        sigList.add(it.toByteArray())
                    }
                }
            }
        } else {
            @Suppress("DEPRECATION")
            packageInfo.signatures?.forEach {
                sigList.add(it.toByteArray())
            }
        }
        return sigList
    }
}
