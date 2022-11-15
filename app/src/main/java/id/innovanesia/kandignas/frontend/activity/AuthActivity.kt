package id.innovanesia.kandignas.frontend.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import id.innovanesia.kandignas.backend.api.InitAPI
import id.innovanesia.kandignas.backend.response.LoginRegisterResponse
import id.innovanesia.kandignas.databinding.ActivityAuthBinding
import id.innovanesia.kandignas.frontend.activity.kantin.KantinMenuActivity
import id.innovanesia.kandignas.frontend.activity.koperasi.KoperasiMenuActivity
import id.innovanesia.kandignas.frontend.activity.siswa.SiswaMenuActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthActivity : AppCompatActivity()
{
    private lateinit var binds: ActivityAuthBinding
    private lateinit var sharedPreference: SharedPreferences
    private val keyToken = "key.token"
    private val keyType = "key.type"

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binds = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binds.root)

        sharedPreference = getSharedPreferences("KanDigNas", Context.MODE_PRIVATE)

        if (sharedPreference.getString(keyType, null) != null)
        {
            if (sharedPreference.getString(keyType, null) == "kantin")
            {
                startActivity(Intent(this, KantinMenuActivity::class.java))
                finish()
            }
            else if (sharedPreference.getString(keyType, null) == "koperasi")
            {
                startActivity(Intent(this, KoperasiMenuActivity::class.java))
                finish()
            }
            else if (sharedPreference.getString(keyType, null) == "siswa"
                || sharedPreference.getString(keyType, null) == "umum"
            )
            {
                startActivity(Intent(this, SiswaMenuActivity::class.java))
                finish()
            }
        }

        binds.apply {
            loginButton.setOnClickListener {
                if (usernameInput.text.toString().trim()
                        .isNotEmpty() && passwordInput.text.toString().trim().isNotEmpty()
                )
                {
                    InitAPI.api.login(usernameInput.text.toString(), passwordInput.text.toString())
                        .enqueue(object : Callback<LoginRegisterResponse>
                        {
                            override fun onResponse(
                                call: Call<LoginRegisterResponse>,
                                response: Response<LoginRegisterResponse>
                            )
                            {
                                Log.e("Response", response.body().toString())
                                if (response.body()!!.user.type == "koperasi"
                                    || response.body()!!.user.type == "kantin"
                                    || response.body()!!.user.type == "siswa"
                                    || response.body()!!.user.type == "umum"
                                )
                                {
                                    val commit: SharedPreferences.Editor = sharedPreference.edit()
                                    commit.putString(keyToken, response.body()!!.access_token)
                                    commit.putString(keyType, response.body()!!.user.type)
                                    commit.apply()
                                    startMainMenu(response.body()!!.user.type)
                                }
                                else
                                {
                                    Snackbar.make(
                                        binds.root,
                                        "Pengguna tidak terdaftar sebagai akun publik.",
                                        Snackbar.LENGTH_SHORT
                                    ).show()
                                }
                            }

                            override fun onFailure(call: Call<LoginRegisterResponse>, t: Throwable)
                            {
                                Snackbar.make(
                                    binds.root,
                                    "Gagal masuk, mohon periksa kembali!",
                                    Snackbar.LENGTH_SHORT
                                ).show()
                                t.printStackTrace()
                            }
                        })
                }
                else
                {
                    Snackbar.make(
                        binds.root,
                        "Mohon isi semua kolom yang kosong!",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }

            registerButton.setOnClickListener {
                startActivity(Intent(this@AuthActivity, RegisterActivity::class.java))
            }
        }
    }

    private fun startMainMenu(type: String)
    {
        when (type)
        {
            "koperasi" -> startActivity(
                Intent(
                    this@AuthActivity, KoperasiMenuActivity::class.java
                )
            )
            "kantin" -> startActivity(
                Intent(
                    this@AuthActivity, KantinMenuActivity::class.java
                )
            )
            "siswa" -> startActivity(
                Intent(
                    this@AuthActivity, SiswaMenuActivity::class.java
                )
            )
            "umum" -> startActivity(
                Intent(
                    this@AuthActivity, SiswaMenuActivity::class.java
                )
            )
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean
    {
        if (event.action == MotionEvent.ACTION_DOWN)
        {
            val v: View? = currentFocus
            if (v is EditText)
            {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt()))
                {
                    v.clearFocus()
                    val imm: InputMethodManager =
                        getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }
}