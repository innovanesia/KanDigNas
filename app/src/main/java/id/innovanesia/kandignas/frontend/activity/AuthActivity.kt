package id.innovanesia.kandignas.frontend.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import id.innovanesia.kandignas.databinding.ActivityAuthBinding
import id.innovanesia.kandignas.frontend.activity.kantin.KantinMenuActivity
import id.innovanesia.kandignas.frontend.activity.koperasi.KoperasiMenuActivity
import id.innovanesia.kandignas.frontend.activity.siswa.SiswaMenuActivity

class AuthActivity : AppCompatActivity()
{
    private lateinit var binds: ActivityAuthBinding
    private lateinit var sharedPreference: SharedPreferences
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

            }

            registerButton.setOnClickListener {
                startActivity(Intent(this@AuthActivity, RegisterActivity::class.java))
            }
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