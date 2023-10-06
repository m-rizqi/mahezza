package com.mahezza.mahezza.ui.ext

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat

fun Context.isPermissionGranted(permission : String): Boolean {
    return ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
}

fun isAndroidVersionGreaterThanTiramisu() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

fun Context.getUnGrantedPermissions(vararg permissions: String) : List<String>{
    val unGrantedPermissions = mutableListOf<String>();
    permissions.forEach {
        if (it == Manifest.permission.POST_NOTIFICATIONS && !isAndroidVersionGreaterThanTiramisu()) {
            return@forEach;
        }
        if (!isPermissionGranted(it)){
            unGrantedPermissions.add(it);
        }
    }
    return unGrantedPermissions;
}