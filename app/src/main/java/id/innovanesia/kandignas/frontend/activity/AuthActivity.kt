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
import com.google.firebase.firestore.FirebaseFirestore
import id.innovanesia.kandignas.databinding.ActivityAuthBinding

class AuthActivity : AppCompatActivity()
{
    private lateinit var binds: ActivityAuthBinding
    private lateinit var sharedPreference: SharedPreferences
    private val keyUser = "key.user_name"
    private val keyType = "key.type"
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binds = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binds.root)

        sharedPreference = getSharedPreferences("KanDigNas", Context.MODE_PRIVATE)
        /*if (sharedPreference.getString(keyType, null) != null)
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
            else if (sharedPreference.getString(keyType, null) == "siswa")
            {
                startActivity(Intent(this, SiswaMenuActivity::class.java))
                finish()
            }
        }*/

        binds.apply {
            loginButton.setOnClickListener {
                db.collection("users").get()
                    .addOnCompleteListener {
                        if (it.isSuccessful)
                        {
                            val result = it.result
                            for (docs in result)
                            {
                                if (usernameInput.text.toString() == docs.id)
                                {
                                    Log.d("User ada!", docs.id)
                                }
                                else if (usernameInput.text.toString() != docs.id)
                                {
                                    Log.d("Error", "User tidak ada!")
                                }
                            }
                        }
                    }
                /*val commit: SharedPreferences.Editor = sharedPreference.edit()
                                commit.putString(keyUser, documents.id)
                                commit.putString(keyType, documents.data["account_type"].toString())
                                commit.apply()*/
                /*if (sharedPreference.getString(keyType, null) == "kantin")
                {
                    Toast.makeText(
                        this@AuthActivity, "Berhasil masuk!", Toast.LENGTH_SHORT
                    ).show()
                    startActivity(Intent(this@AuthActivity, KantinMenuActivity::class.java))
                    finish()
                }
                else if (sharedPreference.getString(keyType, null) == "koperasi")
                {
                    Toast.makeText(
                        this@AuthActivity, "Berhasil masuk!", Toast.LENGTH_SHORT
                    ).show()
                    startActivity(Intent(this@AuthActivity, KoperasiMenuActivity::class.java))
                    finish()
                }
                else if (sharedPreference.getString(keyType, null) == "siswa")
                {
                    Toast.makeText(
                        this@AuthActivity, "Berhasil masuk!", Toast.LENGTH_SHORT
                    ).show()
                    startActivity(Intent(this@AuthActivity, SiswaMenuActivity::class.java))
                    finish()
                }
                if (usernameInput.text.toString() == "" || passwordInput.text.toString() == "")
                {
                    Snackbar.make(
                        binds.root,
                        "Gagal untuk masuk.\nMohon periksa kembali!",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }*/
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