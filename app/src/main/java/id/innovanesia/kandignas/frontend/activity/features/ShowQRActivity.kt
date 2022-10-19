package id.innovanesia.kandignas.frontend.activity.features

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Point
import android.os.Bundle
import android.view.Display
import android.view.WindowManager
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import id.innovanesia.kandignas.databinding.ActivityShowQrBinding

class ShowQRActivity : AppCompatActivity()
{
    private lateinit var binds: ActivityShowQrBinding
    private lateinit var bitmap: Bitmap
    private lateinit var qrEncoder: QRGEncoder
    private val keyUser = "key.user_name"
    private val db = FirebaseFirestore.getInstance()
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

            val user = sharedPreference.getString(keyUser, null)

            getDB(user!!)

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

    private fun getDB(username: String)
    {
        db.collection("users").document(username).get()
            .addOnSuccessListener {
                binds.apply {
                    if (it.data?.get("nisn") == "")
                    {
                        idText.text = it.data?.get("nik").toString()
                    }
                    else
                    {
                        idText.text = it.data?.get("nisn").toString()
                    }
                    usernameText.text = it.data?.get("fullname").toString()
                }
            }
    }
}