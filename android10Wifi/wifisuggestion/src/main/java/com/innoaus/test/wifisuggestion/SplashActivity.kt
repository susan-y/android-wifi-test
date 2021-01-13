package com.innoaus.test.wifisuggestion

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.core.content.ContextCompat
import java.util.ArrayList

class SplashActivity : AppCompatActivity() {
    private val handler = Handler()
    private var permissionResult: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        checkPermission()
    }

    private val runnableToMain = Runnable {
        if (permissionResult) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            finish()
        }
    }

    private fun checkPermission() {
        val list = ArrayList<String>()
        val perm = Manifest.permission.ACCESS_FINE_LOCATION
        if (checkSelf(perm) == PackageManager.PERMISSION_DENIED) {
            // 권한 없음
            list.add(perm)
        }

        if (list.size > 0) {
            val arr = arrayOfNulls<String>(list.size)
            for (i in list.indices) {
                arr[i] = list[i]
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (arr.isNotEmpty())
                    requestPermissions(arr, 8282)
            }
        } else {
            handler.postDelayed(runnableToMain, 1000)
        }
    }

    private fun checkSelf(permission: String): Int {
        return ContextCompat.checkSelfPermission(this, permission)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == 8282) {
            for (i in permissions.indices) {
                if (grantResults.isNotEmpty() && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    this.permissionResult = true
                } else {
                    val msg = "Permission denied."
                    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
                    this.permissionResult = false
                }
            }
            handler.postDelayed(runnableToMain, 800)
        }
    }
}
