package id.innovanesia.kandignas.frontend.activity.features

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Point
import android.os.Bundle
import android.view.Display
import android.view.WindowManager
import android.widget.Toast
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import id.innovanesia.kandignas.backend.api.InitAPI
import id.innovanesia.kandignas.backend.response.AccountResponse
import id.innovanesia.kandignas.databinding.ActivityShowQrBinding
import id.innovanesia.kandignas.frontend.activity.AuthActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShowQRActivity : AppCompatActivity()
{
    private lateinit var binds: ActivityShowQrBinding
    private lateinit var bitmap: Bitmap
    private lateinit var qrEncoder: QRGEncoder
    private val keyToken = "key.token"
    private lateinit var sharedPreference: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binds = ActivityShowQrBinding.inflate(layoutInflater)
        setContentView(binds.root)

        sharedPreference = getSharedPreferences("KanDigNas", Context.MODE_PRIVATE)

        binds.apply {
            setSupportActionBar(toolbar)
            toolbar.setNavigationOnClickListener {
                finish()
            }

            val token = sharedPreference.getString(keyToken, null)!!

            getDB(token)

            initQR(token)

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
    private fun initQR(userToken: String?)
    {
        val windowManager: WindowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        val display: Display = windowManager.defaultDisplay
        val point = Point()
        display.getSize(point)
        val width = point.x
        val height = point.y
        var dimen = if (width < height) width else height
        dimen = dimen * 3 / 4
        qrEncoder = QRGEncoder(userToken, null, QRGContents.Type.TEXT, dimen)
    }

    private fun getDB(token: String)
    {
        binds.apply {
            InitAPI.api.getAccount("Bearer $token")
                .enqueue(object : Callback<AccountResponse>
                {
                    override fun onResponse(
                        call: Call<AccountResponse>,
                        response: Response<AccountResponse>
                    )
                    {
                        val result = response.body()
                        usernameText.text = result?.user?.fullname
                        idText.text =
                            if (result?.user?.nik == "")
                                result.user.nisn
                        else
                            result?.user?.nik
                    }

                    override fun onFailure(call: Call<AccountResponse>, t: Throwable)
                    {
                        Toast.makeText(
                            this@ShowQRActivity,
                            "Mohon periksa kembali koneksi!",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }
                })
        }
    }
}