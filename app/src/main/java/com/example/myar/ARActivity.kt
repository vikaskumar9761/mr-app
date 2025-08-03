package com.example.myar

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myar.helpers.CameraPermissionHelper
import com.google.ar.core.*
import com.google.ar.core.exceptions.CameraNotAvailableException
import com.google.ar.core.exceptions.UnavailableUserDeclinedInstallationException
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.Color
import com.google.ar.sceneform.rendering.MaterialFactory
import com.google.ar.sceneform.rendering.ShapeFactory
import com.google.ar.sceneform.ux.ArFragment

class ARActivity : AppCompatActivity() {

    private lateinit var arFragment: ArFragment
    private var mSession: Session? = null
    private var mUserRequestedInstall = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ar)

        arFragment = supportFragmentManager.findFragmentById(R.id.arFragment) as ArFragment

        // Check AR availability
        maybeEnableArFragment()

        // AR object placement
        arFragment.setOnTapArPlaneListener { hitResult, plane, motionEvent ->
            addCube(hitResult)
        }
    }

    // ✅ This method checks AR support without async
    private fun maybeEnableArFragment() {
        val availability = ArCoreApk.getInstance().checkAvailability(this)

        if (!availability.isSupported) {
            Toast.makeText(this, "AR is not supported on this device", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    override fun onResume() {
        super.onResume()

        // Ask for camera permission
        if (!CameraPermissionHelper.hasCameraPermission(this)) {
            CameraPermissionHelper.requestCameraPermission(this)
            return
        }

        try {
            if (mSession == null) {
                // Try to install ARCore
                val installStatus = ArCoreApk.getInstance().requestInstall(this, mUserRequestedInstall)
                when (installStatus) {
                    ArCoreApk.InstallStatus.INSTALLED -> {
                        // ARCore is installed
                        mSession = Session(this)
                        arFragment.arSceneView.setupSession(mSession)
                    }

                    ArCoreApk.InstallStatus.INSTALL_REQUESTED -> {
                        // User has been prompted, don't ask again in this session
                        mUserRequestedInstall = false
                        return
                    }
                }
            }
        } catch (e: UnavailableUserDeclinedInstallationException) {
            Toast.makeText(this, "Please install ARCore to use this app", Toast.LENGTH_LONG).show()
            mUserRequestedInstall = true  // So it prompts again next time
            return
        } catch (e: Exception) {
            Toast.makeText(this, "ARCore is not available: $e", Toast.LENGTH_LONG).show()
            return
        }

        // Resume the AR scene
        try {
            arFragment.arSceneView.resume()
        } catch (e: CameraNotAvailableException) {
            Toast.makeText(this, "Camera not available. Try restarting the app.", Toast.LENGTH_LONG).show()
            mSession = null
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, results: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, results)
        if (!CameraPermissionHelper.hasCameraPermission(this)) {
            Toast.makeText(this, "Camera permission is required", Toast.LENGTH_LONG).show()
            if (!CameraPermissionHelper.shouldShowRequestPermissionRationale(this)) {
                CameraPermissionHelper.launchPermissionSettings(this)
            }
            finish()
        }
    }

    private fun addCube(hitResult: HitResult) {
        MaterialFactory.makeOpaqueWithColor(this, Color(android.graphics.Color.RED))
            .thenAccept { material ->
                val cube = ShapeFactory.makeCube(
                    Vector3(0.1f, 0.1f, 0.1f),
                    Vector3.zero(),
                    material
                )

                val anchor = hitResult.createAnchor()
                val anchorNode = AnchorNode(anchor)
                anchorNode.renderable = cube
                arFragment.arSceneView.scene.addChild(anchorNode)
            }
    }
}
