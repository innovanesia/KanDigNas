package id.innovanesia.kandignas.frontend.activity.features

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Point
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Display
import android.view.WindowManager
import android.widget.Toast
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import id.innovanesia.kandignas.backend.api.InitAPI
import id.innovanesia.kandignas.backend.response.AccountResponse
import id.innovanesia.kandignas.databinding.ActivityShowQrBinding
import id.innovanesia.kandignas.databinding.SaveQrCodeFileConfirmationDialogBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ShowQRActivity : AppCompatActivity()
{
    private lateinit var binds: ActivityShowQrBinding
    private lateinit var bitmap: Bitmap
    private lateinit var qrEncoder: QRGEncoder
    private val keyToken = "key.token"
    private lateinit var sharedPreference: SharedPreferences

    companion object
    {
        private const val FILE_REQ = 101
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binds = ActivityShowQrBinding.inflate(layoutInflater)
        setContentView(binds.root)
        val writePermission = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val readPermission = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        )

        if (writePermission != PackageManager.PERMISSION_GRANTED
            && readPermission != PackageManager.PERMISSION_GRANTED
        )
        {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                FILE_REQ
            )
        }

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
                qrCodeImg.setOnClickListener {
                    showConfirmSaveDialog(bitmap)
                }
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    )
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode)
        {
            FILE_REQ ->
            {
                if (grantResults.isEmpty() ||
                    (grantResults[0] != PackageManager.PERMISSION_GRANTED
                            && grantResults[1] != PackageManager.PERMISSION_GRANTED))
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

    private fun showConfirmSaveDialog(image: Bitmap)
    {
        val dialog = BottomSheetDialog(this@ShowQRActivity)
        val dialogBinding: SaveQrCodeFileConfirmationDialogBinding =
            SaveQrCodeFileConfirmationDialogBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)
        dialog.setCancelable(true)
        dialog.show()

        dialogBinding.apply {
            cancelSaveButton.setOnClickListener {
                dialog.dismiss()
            }

            confirmSaveButton.setOnClickListener {
                storeImage(image)
                dialog.dismiss()
            }
        }
    }

    private fun storeImage(image: Bitmap)
    {
        val pictureFile: File? = getOutputMediaFile()
        if (pictureFile == null)
        {
            Log.e("ERROR", "Error creating media file, check storage permissions: ")
            Toast.makeText(
                this@ShowQRActivity,
                "Masalah pada proses menyimpan, periksa perizinan ruang internal!",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        try
        {
            val outputStream = FileOutputStream(pictureFile)
            image.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.close()
            Toast.makeText(
                this@ShowQRActivity,
                "QR Code Berhasil Tersimpan di Ponsel!",
                Toast.LENGTH_SHORT
            ).show()
        }
        catch (e: FileNotFoundException)
        {
            Log.e("ERROR", "File not found: " + e.message)
        }
        catch (e: IOException)
        {
            Log.e("ERROR", "Error accessing file: " + e.message)
        }
    }

    private fun getOutputMediaFile(): File?
    {
        val dir = File(
            Environment.getExternalStorageDirectory()
                .toString() + "/Pictures/KanDigNas/QR Code Generated"
        )

        if (!dir.exists())
        {
            if (!dir.mkdirs())
            {
                return null
            }
        }
        val timeStamp: String = SimpleDateFormat("ddMMyyyy_HHmm").format(Date())
        val mediaFile: File
        val imageName = "KanDigNas_QR_Code_$timeStamp.jpg"
        mediaFile = File(dir.path + File.separator.toString() + imageName)
        return mediaFile
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