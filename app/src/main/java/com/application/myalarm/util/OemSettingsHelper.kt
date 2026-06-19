package com.application.myalarm.util

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings

object OemSettingsHelper {

    fun isOemDevice(): Boolean {
        val manufacturer = Build.MANUFACTURER.lowercase()
        val brand = Build.BRAND.lowercase()
        val targetBrands = listOf("xiaomi", "redmi", "tecno", "infinix", "samsung", "oppo", "realme", "huawei", "honor")
        return targetBrands.any { manufacturer.contains(it) || brand.contains(it) }
    }

    fun getOemSettingsIntent(context: Context): Intent {
        val manufacturer = Build.MANUFACTURER.lowercase()
        val brand = Build.BRAND.lowercase()

        val intent = Intent()

        when {
            // Xiaomi / Redmi
            manufacturer.contains("xiaomi") || brand.contains("xiaomi") || manufacturer.contains("redmi") || brand.contains("redmi") -> {
                try {
                    intent.action = "miui.intent.action.APP_PERM_EDITOR"
                    intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity")
                    intent.putExtra("extra_pkgname", context.packageName)
                    if (isIntentResolvable(context, intent)) return intent
                } catch (e: Exception) {}
                try {
                    val startIntent = Intent("miui.intent.action.OP_AUTO_START")
                    startIntent.setClassName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity")
                    if (isIntentResolvable(context, startIntent)) return startIntent
                } catch (e: Exception) {}
            }

            // Oppo / Realme
            manufacturer.contains("oppo") || brand.contains("oppo") || manufacturer.contains("realme") || brand.contains("realme") -> {
                val oppoComponents = listOf(
                    "com.coloros.safecenter" to "com.coloros.safecenter.permission.startup.StartupAppListActivity",
                    "com.coloros.safecenter" to "com.coloros.safecenter.startupapp.StartupAppListActivity",
                    "com.oppo.safe" to "com.oppo.safe.permission.startup.StartupAppListActivity",
                    "com.coloros.safecenter" to "com.coloros.safecenter.permission.floatwindow.FloatWindowListActivity",
                    "com.oppo.safe" to "com.oppo.safe.permission.floatwindow.FloatWindowListActivity"
                )
                for ((pkg, cls) in oppoComponents) {
                    try {
                        val compIntent = Intent().setClassName(pkg, cls)
                        if (isIntentResolvable(context, compIntent)) return compIntent
                    } catch (e: Exception) {}
                }
            }

            // Huawei / Honor
            manufacturer.contains("huawei") || brand.contains("huawei") || manufacturer.contains("honor") || brand.contains("honor") -> {
                val huaweiComponents = listOf(
                    "com.huawei.systemmanager" to "com.huawei.systemmanager.appcontrol.activity.StartupAppControlActivity",
                    "com.huawei.systemmanager" to "com.huawei.systemmanager.optimize.process.ProtectActivity",
                    "com.huawei.systemmanager" to "com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity"
                )
                for ((pkg, cls) in huaweiComponents) {
                    try {
                        val compIntent = Intent().setClassName(pkg, cls)
                        if (isIntentResolvable(context, compIntent)) return compIntent
                    } catch (e: Exception) {}
                }
            }

            // Tecno / Infinix
            manufacturer.contains("tecno") || brand.contains("tecno") || manufacturer.contains("infinix") || brand.contains("infinix") -> {
                val transsionComponents = listOf(
                    "com.transsion.phonemaster" to "com.cyin.himgr.autostart.AutoStartActivity",
                    "com.transsion.phonemaster" to "com.transsion.phonemaster.autostart.AutoStartActivity"
                )
                for ((pkg, cls) in transsionComponents) {
                    try {
                        val compIntent = Intent().setClassName(pkg, cls)
                        if (isIntentResolvable(context, compIntent)) return compIntent
                    } catch (e: Exception) {}
                }
            }

            // Samsung
            manufacturer.contains("samsung") || brand.contains("samsung") -> {
                try {
                    val intentBattery = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
                        data = Uri.parse("package:${context.packageName}")
                    }
                    if (isIntentResolvable(context, intentBattery)) return intentBattery
                } catch (e: Exception) {}
            }
        }

        // Default fallback: App Details Settings page
        return Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", context.packageName, null)
        }
    }

    private fun isIntentResolvable(context: Context, intent: Intent): Boolean {
        return try {
            val packageManager = context.packageManager
            val info = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
            info != null
        } catch (e: Exception) {
            false
        }
    }
}
