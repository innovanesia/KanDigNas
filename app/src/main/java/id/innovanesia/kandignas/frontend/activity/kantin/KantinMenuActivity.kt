package id.innovanesia.kandignas.frontend.activity.kantin

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
import id.innovanesia.kandignas.databinding.ActivityKantinMenuBinding
import id.innovanesia.kandignas.frontend.activity.AuthActivity
import id.innovanesia.kandignas.frontend.activity.features.CalculatorActivity
import id.innovanesia.kandignas.frontend.activity.features.ShowQRActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import java.text.NumberFormat

class KantinMenuActivity : AppCompatActivity()
{
    private lateinit var binds: ActivityKantinMenuBinding
    private lateinit var sharedPreference: SharedPreferences
    private val keyToken = "key.token"

    companion object
    {
        private const val type = "kantin"
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binds = ActivityKantinMenuBinding.inflate(layoutInflater)
        setContentView(binds.root)

        sharedPreference = getSharedPreferences("KanDigNas", Context.MODE_PRIVATE)
        val token = sharedPreference.getString(keyToken, null)!!

        binds.apply {
            setSupportActionBar(toolbar)

            mainMenuKantinLoading.visibility = View.VISIBLE

            getDB(token)

            swipeRefreshLayout.setOnRefreshListener {
                getDB(token)
            }

            calculatorButton.setOnClickListener {
                startActivity(Intent(this@KantinMenuActivity, CalculatorActivity::class.java))
            }

            showQrButton.setOnClickListener {
                startActivity(Intent(this@KantinMenuActivity, ShowQRActivity::class.java)
                    .also {
                        it.putExtra("API", sharedPreference.getString(keyToken, null))
                    })
            }

            setNews()
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
                    this@KantinMenuActivity,
                    "Signed out sucessfully!",
                    Toast.LENGTH_SHORT
                ).show()
                startActivity(Intent(this@KantinMenuActivity, AuthActivity::class.java))
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
                            val format: NumberFormat = DecimalFormat("#,###")
                            balanceAmount.text = format.format(response.body()!!.user.balance)
                        }

                        override fun onFailure(call: Call<AccountResponse>, t: Throwable)
                        {
                            val delete: SharedPreferences.Editor = sharedPreference.edit()
                            delete.clear().apply()
                            Toast.makeText(
                                this@KantinMenuActivity,
                                "Gagal masuk! Mohon periksa koneksi.",
                                Toast.LENGTH_SHORT
                            ).show()
                            startActivity(Intent(this@KantinMenuActivity, AuthActivity::class.java))
                            finish()
                        }
                    })
                mainMenuKantinLoading.visibility = View.GONE
                swipeRefreshLayout.isRefreshing = false
            }
    }

}