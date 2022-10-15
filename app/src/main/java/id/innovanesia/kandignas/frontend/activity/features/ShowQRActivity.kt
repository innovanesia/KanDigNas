package id.innovanesia.kandignas.frontend.activity.features

import android.graphics.Bitmap
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.Display
import android.view.WindowManager
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import id.innovanesia.kandignas.databinding.ActivityShowQrBinding

class ShowQRActivity : AppCompatActivity()
{
    private lateinit var binds: ActivityShowQrBinding
    private lateinit var bitmap: Bitmap
    private lateinit var qrEncoder: QRGEncoder

    companion object
    {
        const val username = "USERNAME"
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binds = ActivityShowQrBinding.inflate(layoutInflater)
        setContentView(binds.root)
        val user = intent.getStringExtra(username)

        Log.d("Username", user.toString())

        binds.apply {
            setSupportActionBar(toolbar)
            toolbar.setNavigationOnClickListener {
                finish()
            }

            initQR(user)
            try
            {
                bitmap = qrEncoder.bitmap
                qrCodeImg.setImageBitmap(bitmap)
            }
            catch (e: Exception)
            {
                e.printStackTrace()
            }
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true)
        {
            override fun handleOnBackPressed()
            {
                finish()
            }
        })
    }

    @Suppress("DEPRECATION")
    private fun initQR(user: String?)
    {
        val windowManager: WindowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        val display: Display = windowManager.defaultDisplay
        val point = Point()
        display.getSize(point)
        val width = point.x
        val height = point.y
        var dimen = if (width < height) width else height
        dimen = dimen * 3 / 4
        qrEncoder = QRGEncoder(user, null, QRGContents.Type.TEXT, dimen)
    }
}