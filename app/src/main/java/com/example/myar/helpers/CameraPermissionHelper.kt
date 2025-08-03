package com.example.myar.helpers


import androidx.appcompat.app.AppCompatActivity


object CameraPermissionHelper {
    private const val CAMERA_PERMISSION_CODE = 0
    private const val CAMERA_PERMISSION = android.Manifest.permission.CAMERA

    fun hasCameraPermission(activity: AppCompatActivity): Boolean {
        return androidx.core.content.ContextCompat.checkSelfPermission(
            activity,
            CAMERA_PERMISSION
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED
    }

    fun requestCameraPermission(activity: AppCompatActivity) {
        androidx.core.app.ActivityCompat.requestPermissions(
            activity,
            arrayOf(CAMERA_PERMISSION),
            CAMERA_PERMISSION_CODE
        )
    }

    fun shouldShowRequestPermissionRationale(activity: AppCompatActivity): Boolean {
        return androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale(
            activity,
            CAMERA_PERMISSION
        )
    }

    fun launchPermissionSettings(activity: AppCompatActivity) {
        val intent = android.content.Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = android.net.Uri.fromParts("package", activity.packageName, null)
        intent.data = uri
        activity.startActivity(intent)
    }
}
