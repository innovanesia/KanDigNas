package id.innovanesia.kandignas.frontend.activity.features

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.*
import com.google.android.material.snackbar.Snackbar
import id.innovanesia.kandignas.databinding.ActivityScanQrBinding
import id.innovanesia.kandignas.frontend.activity.koperasi.KoperasiMenuActivity
import id.innovanesia.kandignas.frontend.activity.siswa.SiswaMenuActivity

class ScanQRActivity : AppCompatActivity()
{
    private lateinit var binds: ActivityScanQrBinding
    private lateinit var codeScanner: CodeScanner

    companion object
    {
        private const val CAMERA_REQ = 101
        private const val ACTIVITY = "ACTIVITY"
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binds = ActivityScanQrBinding.inflate(layoutInflater)
        setContentView(binds.root)

        val activity = intent.getStringExtra(ACTIVITY)

        val permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
        if (permission != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(
                this, arrayOf(android.Manifest.permission.CAMERA),
                CAMERA_REQ
            )
        }

        binds.apply {
            setSupportActionBar(toolbar)
            toolbar.setNavigationOnClickListener {
                onBackPressed()
            }

            codeScanner = CodeScanner(this@ScanQRActivity, qrscannerView)

            codeScanner.apply {
                camera = CodeScanner.CAMERA_BACK
                formats = CodeScanner.ALL_FORMATS
                autoFocusMode = AutoFocusMode.SAFE
                scanMode = ScanMode.CONTINUOUS
                isAutoFocusEnabled = true
                isFlashEnabled = false

                decodeCallback = DecodeCallback { res ->
                    if (activity == "siswa")
                    {
                        startActivity(Intent(this@ScanQRActivity, SelectNominalActivity::class.java)
                            .also { int ->
                                int.putExtra("USERNAME", res.text)
                                int.putExtra("ACTIVITY", activity)
                            })
                        finish()
                    }
                    else if (activity == "koperasi")
                    {
                        startActivity(Intent(this@ScanQRActivity, SelectNominalActivity::class.java)
                            .also { int ->
                                int.putExtra("USERNAME", res.text)
                                int.putExtra("ACTIVITY", activity)
                            })
                        finish()
                    }
                }

                errorCallback = ErrorCallback {
                    Log.e("Main", "codeScanner: ${it.message}")
                }

                qrscannerView.setOnClickListener {
                    codeScanner.startPreview()
                }
            }
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true)
        {
            override fun handleOnBackPressed()
            {
                if (activity.equals("koperasi"))
                {
                    startActivity(Intent(this@ScanQRActivity, KoperasiMenuActivity::class.java))
                    finish()
                }
                else if (activity.equals("siswa"))
                {
                    startActivity(Intent(this@ScanQRActivity, SiswaMenuActivity::class.java))
                    finish()
                }
            }
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    )
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode)
        {
            CAMERA_REQ ->
            {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED)
                {
                    Snackbar.make(
                        binds.root,
                        "Kamu perlu perizinan kamera untuk menggunakan aplikasi ini!",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onResume()
    {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause()
    {
        codeScanner.releaseResources()
        super.onPause()
    }
}