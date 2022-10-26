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
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import id.innovanesia.kandignas.R
import id.innovanesia.kandignas.databinding.ActivitySiswaMenuBinding
import id.innovanesia.kandignas.frontend.activity.AuthActivity
import id.innovanesia.kandignas.frontend.activity.features.ScanQRActivity
import id.innovanesia.kandignas.frontend.activity.features.ShowQRActivity
import id.innovanesia.kandignas.frontend.activity.features.TransactionHistoryActivity
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

class SiswaMenuActivity : AppCompatActivity()
{
    private lateinit var binds: ActivitySiswaMenuBinding
    private lateinit var sharedPreference: SharedPreferences
    private val keyUser = "key.user_name"
    private val keyType = "key.type"
    private val db = FirebaseFirestore.getInstance()

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
        val username = sharedPreference.getString(keyUser, null)!!

        binds.apply {
            setSupportActionBar(toolbar)

            mainMenuSiswaLoading.visibility = View.VISIBLE

            if (sharedPreference.getString(keyType, null) == "siswa")
                toolbar.title = "Siswa"
            else if (sharedPreference.getString(keyType, null) == "umum")
                toolbar.title = "Umum"

            getDB(username)

            swipeRefreshLayout.setOnRefreshListener {
                getDB(username)
            }

            setNews()

            topupButton.setOnClickListener {
                startActivity(Intent(this@SiswaMenuActivity, ShowQRActivity::class.java))
            }
            scanqrButton.setOnClickListener {
                startActivity(Intent(this@SiswaMenuActivity, ScanQRActivity::class.java)
                    .also {
                        it.putExtra("ACTIVITY", type)
                    })
            }
            transactionHistoryButton.setOnClickListener {
                startActivity(
                    Intent(
                        this@SiswaMenuActivity, TransactionHistoryActivity::class.java
                    ).also {
                        it.putExtra("ACTIVITY", type)
                    }
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
                    "Signed out sucessfully!",
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

    @SuppressLint("SetTextI18n")
    private fun getDB(username: String)
    {
        binds.apply {
            db.collection("users").document(username).get()
                .addOnSuccessListener {
                    mainMenuSiswaLoading.visibility = View.GONE
                    greetingsText.text = "Hai, ${it.data?.get("fullname")}!"
                    val format: NumberFormat = DecimalFormat("#,###")
                    balanceAmount.text = format.format(it.data?.get("balance"))
                }
                .addOnFailureListener {
                    Snackbar.make(
                        binds.root,
                        "Something wrong, please try again!",
                        Snackbar.LENGTH_SHORT
                    ).show()
                    it.printStackTrace()
                }
            swipeRefreshLayout.isRefreshing = false
        }
    }
}