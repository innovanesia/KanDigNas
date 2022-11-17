package id.innovanesia.kandignas.frontend.activity.koperasi

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
import id.innovanesia.kandignas.databinding.ActivityKoperasiMenuBinding
import id.innovanesia.kandignas.frontend.activity.AuthActivity
import id.innovanesia.kandignas.frontend.activity.features.ScanQRActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class KoperasiMenuActivity : AppCompatActivity()
{
    private lateinit var binds: ActivityKoperasiMenuBinding
    private lateinit var sharedPreference: SharedPreferences
    private val keyToken = "key.token"

    companion object
    {
        private const val type = "koperasi"
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binds = ActivityKoperasiMenuBinding.inflate(layoutInflater)
        setContentView(binds.root)

        sharedPreference = getSharedPreferences("KanDigNas", Context.MODE_PRIVATE)
        val token = sharedPreference.getString(keyToken, null)!!

        binds.apply {
            setSupportActionBar(toolbar)

            mainMenuKoperasiLoading.visibility = View.VISIBLE

            getDB(token)

            swipeRefreshLayout.setOnRefreshListener {
                getDB(token)
            }

            setNews()

            koperasiScanqrButton.setOnClickListener {
                startActivity(Intent(this@KoperasiMenuActivity, ScanQRActivity::class.java)
                    .also {
                        it.putExtra("ACTIVITY", type)
                    })
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
                    this@KoperasiMenuActivity,
                    "Signed out sucessfully!",
                    Toast.LENGTH_SHORT
                ).show()
                startActivity(Intent(this@KoperasiMenuActivity, AuthActivity::class.java))
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
                    override fun onResponse(
                        call: Call<AccountResponse>,
                        response: Response<AccountResponse>
                    )
                    {
                        greetingsText.text = response.body()!!.user.fullname
                    }

                    override fun onFailure(call: Call<AccountResponse>, t: Throwable)
                    {
                        val delete: SharedPreferences.Editor = sharedPreference.edit()
                        delete.clear().apply()
                        Toast.makeText(
                            this@KoperasiMenuActivity,
                            "Gagal masuk! Mohon periksa koneksi.",
                            Toast.LENGTH_SHORT
                        ).show()
                        startActivity(Intent(this@KoperasiMenuActivity, AuthActivity::class.java))
                        finish()
                    }
                })
            mainMenuKoperasiLoading.visibility = View.GONE
            swipeRefreshLayout.isRefreshing = false
        }
    }
}