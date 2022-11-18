package id.innovanesia.kandignas.frontend.activity.siswa

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.denzcoskun.imageslider.models.SlideModel
import id.innovanesia.kandignas.R
import id.innovanesia.kandignas.backend.api.InitAPI
import id.innovanesia.kandignas.backend.response.AccountResponse
import id.innovanesia.kandignas.databinding.ActivitySiswaMenuBinding
import id.innovanesia.kandignas.frontend.activity.AuthActivity
import id.innovanesia.kandignas.frontend.activity.UnderDevelopmentActivity
import id.innovanesia.kandignas.frontend.activity.features.ScanQRActivity
import id.innovanesia.kandignas.frontend.activity.features.ShowQRActivity
import id.innovanesia.kandignas.frontend.activity.features.TransactionHistoryActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import java.text.NumberFormat

class SiswaMenuActivity : AppCompatActivity()
{
    private lateinit var binds: ActivitySiswaMenuBinding
    private lateinit var sharedPreference: SharedPreferences
    private val keyToken = "key.token"
    private val keyType = "key.type"

    companion object
    {
        private const val type = "siswa"
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binds = ActivitySiswaMenuBinding.inflate(layoutInflater)
        setContentView(binds.root)

        sharedPreference = getSharedPreferences("KanDigNas", Context.MODE_PRIVATE)
        val token = sharedPreference.getString(keyToken, null)!!

        binds.apply {
            setSupportActionBar(toolbar)

            mainMenuSiswaLoading.visibility = View.VISIBLE

            if (sharedPreference.getString(keyType, null) == "siswa")
                toolbar.title = "Siswa"
            else if (sharedPreference.getString(keyType, null) == "umum")
                toolbar.title = "Umum"

            getDB(token)

            swipeRefreshLayout.setOnRefreshListener {
                getDB(token)
            }

            setNews()

            topupButton.setOnClickListener {
                startActivity(
                    Intent(
                        this@SiswaMenuActivity, ShowQRActivity::class.java
                    )
                )
            }
            tabunganButton.setOnClickListener {
                startActivity(
                    Intent(
                        this@SiswaMenuActivity, UnderDevelopmentActivity::class.java
                    )
                )
            }
            siswaScanqrButton.setOnClickListener {
                startActivity(Intent(this@SiswaMenuActivity, ScanQRActivity::class.java)
                    .also {
                        it.putExtra("ACTIVITY", type)
                    })
            }
            transactionHistoryButton.setOnClickListener {
                startActivity(
                    Intent(
                        this@SiswaMenuActivity, TransactionHistoryActivity::class.java
                    )
                )
            }
            editProfileButton.setOnClickListener {
                startActivity(
                    Intent(
                        this@SiswaMenuActivity, UnderDevelopmentActivity::class.java
                    )
                )
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean
    {
        menuInflater.inflate(R.menu.menu_layout, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        when (item.itemId)
        {
            R.id.logout_action ->
            {
                val delete: SharedPreferences.Editor = sharedPreference.edit()
                delete.clear().apply()
                Toast.makeText(
                    this@SiswaMenuActivity,
                    "Berhasil keluar!",
                    Toast.LENGTH_SHORT
                ).show()
                startActivity(Intent(this@SiswaMenuActivity, AuthActivity::class.java))
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setNews()
    {
        val news: MutableList<SlideModel> = ArrayList()

        news.add(
            SlideModel(
                R.drawable.poster_1
            )
        )
        news.add(
            SlideModel(
                R.drawable.poster_2
            )
        )
        news.add(
            SlideModel(
                R.drawable.poster_3
            )
        )

        binds.apply {
            newsCarousel.setImageList(news)
        }
    }

    private fun getDB(token: String)
    {
        binds.apply {
            InitAPI.api.getAccount("Bearer $token")
                .enqueue(object : Callback<AccountResponse>
                {
                    @SuppressLint("SetTextI18n")
                    override fun onResponse(
                        call: Call<AccountResponse>,
                        response: Response<AccountResponse>
                    )
                    {
                        greetingsText.text = "Hai, ${response.body()!!.user.fullname}!"
                        val format: NumberFormat = DecimalFormat("#,###")
                        balanceAmount.text = format.format(response.body()!!.user.balance)
                    }

                    override fun onFailure(call: Call<AccountResponse>, t: Throwable)
                    {
                        val delete: SharedPreferences.Editor = sharedPreference.edit()
                        delete.clear().apply()
                        Toast.makeText(
                            this@SiswaMenuActivity,
                            "Gagal masuk! Mohon periksa koneksi.",
                            Toast.LENGTH_SHORT
                        ).show()
                        startActivity(Intent(this@SiswaMenuActivity, AuthActivity::class.java))
                        finish()
                    }
                })
            mainMenuSiswaLoading.visibility = View.GONE
            swipeRefreshLayout.isRefreshing = false
        }
    }
}