package id.innovanesia.kandignas.frontend.activity.kantin

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.denzcoskun.imageslider.models.SlideModel
import com.google.firebase.firestore.FirebaseFirestore
import id.innovanesia.kandignas.R
import id.innovanesia.kandignas.databinding.ActivityKantinMenuBinding
import id.innovanesia.kandignas.frontend.activity.AuthActivity
import id.innovanesia.kandignas.frontend.activity.features.CalculatorActivity
import id.innovanesia.kandignas.frontend.activity.features.ShowQRActivity

class KantinMenuActivity : AppCompatActivity()
{
    private lateinit var binds: ActivityKantinMenuBinding
    private lateinit var sharedPreference: SharedPreferences
    private val keyUser = "key.user_name"
    private val keyType = "key.type"
    private val db = FirebaseFirestore.getInstance()

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
        val username = sharedPreference.getString(keyUser, null)!!

        binds.apply {
            setSupportActionBar(toolbar)

            calculatorButton.setOnClickListener {
                startActivity(Intent(this@KantinMenuActivity, CalculatorActivity::class.java))
            }

            cairsaldoButton.setOnClickListener {
                startActivity(Intent(this@KantinMenuActivity, ShowQRActivity::class.java)
                    .also {
                        it.putExtra("API", sharedPreference.getString(keyUser, null))
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
}