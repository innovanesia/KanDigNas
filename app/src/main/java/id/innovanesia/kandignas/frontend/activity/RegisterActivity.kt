package id.innovanesia.kandignas.frontend.activity

import android.graphics.Rect
import android.os.Bundle
import android.telephony.ims.RegistrationManager.RegistrationCallback
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import id.innovanesia.kandignas.backend.api.InitAPI
import id.innovanesia.kandignas.backend.response.LoginRegisterResponse
import id.innovanesia.kandignas.databinding.RegisterAccountFormBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity()
{
    private lateinit var binds: RegisterAccountFormBinding

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binds = RegisterAccountFormBinding.inflate(layoutInflater)
        setContentView(binds.root)

        viewForm()
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

    private fun viewForm()
    {
        binds.apply {
            setSupportActionBar(toolbar)

            toolbar.setNavigationOnClickListener {
                finish()
            }

            var type: String? = null

            niknipLayout.visibility = View.GONE
            nisnLayout.visibility = View.GONE
            nisLayout.visibility = View.GONE
            namaLayout.visibility = View.GONE
            kontakLayout.visibility = View.GONE
            emailLayout.visibility = View.GONE
            usernameLayout.visibility = View.GONE
            passwordLayout.visibility = View.GONE
            confirmpassLayout.visibility = View.GONE

            kantinType.setOnClickListener {
                val selectedId = typeGroup.checkedRadioButtonId
                val button: RadioButton = findViewById(selectedId)

                type = button.text.toString()
                niknipLayout.visibility = View.VISIBLE
                nisLayout.visibility = View.GONE
                nisnLayout.visibility = View.GONE
                namaLayout.visibility = View.VISIBLE
                kontakLayout.visibility = View.VISIBLE
                emailLayout.visibility = View.VISIBLE
                usernameLayout.visibility = View.VISIBLE
                passwordLayout.visibility = View.VISIBLE
                confirmpassLayout.visibility = View.VISIBLE
            }

            umumType.setOnClickListener {
                val selectedId = typeGroup.checkedRadioButtonId
                val button: RadioButton = findViewById(selectedId)

                type = button.text.toString()
                niknipLayout.visibility = View.VISIBLE
                nisLayout.visibility = View.GONE
                nisnLayout.visibility = View.GONE
                namaLayout.visibility = View.VISIBLE
                kontakLayout.visibility = View.VISIBLE
                emailLayout.visibility = View.VISIBLE
                usernameLayout.visibility = View.VISIBLE
                passwordLayout.visibility = View.VISIBLE
                confirmpassLayout.visibility = View.VISIBLE
            }

            siswaType.setOnClickListener {
                val selectedId = typeGroup.checkedRadioButtonId
                val button: RadioButton = findViewById(selectedId)

                type = button.text.toString()
                niknipLayout.visibility = View.VISIBLE
                nisLayout.visibility = View.VISIBLE
                nisnLayout.visibility = View.VISIBLE
                namaLayout.visibility = View.VISIBLE
                kontakLayout.visibility = View.VISIBLE
                emailLayout.visibility = View.VISIBLE
                usernameLayout.visibility = View.VISIBLE
                passwordLayout.visibility = View.VISIBLE
                confirmpassLayout.visibility = View.VISIBLE
            }

            submitButton.setOnClickListener {
                if (type == null)
                {
                    Snackbar.make(
                        binds.root,
                        "Mohon isi semua data!",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
                else
                {
                    if (type!!.lowercase() == "umum" || type!!.lowercase() == "kantin")
                    {
                        if (niknipInput.text!!.isEmpty() || namaInput.text!!.isEmpty()
                            || kontakInput.text!!.isEmpty() || emailInput.text!!.isEmpty()
                            || usernameInput.text!!.isEmpty() || passwordInput.text!!.isEmpty()
                            || confirmpassInput.text!!.isEmpty()
                        )
                        {
                            Snackbar.make(
                                binds.root,
                                "Mohon isi semua data!",
                                Snackbar.LENGTH_SHORT
                            )
                                .show()
                        }
                        else if (passwordInput.text.toString() != confirmpassInput.text.toString())
                            Snackbar.make(
                                binds.root,
                                "Kata sandi tidak sama!",
                                Snackbar.LENGTH_SHORT
                            ).show()
                        else
                        {
                            InitAPI.api.register(
                                type!!.lowercase(),
                                namaInput.text.toString(),
                                1,
                                emailInput.text.toString(),
                                usernameInput.text.toString(),
                                passwordInput.text.toString(),
                                "",
                                "",
                                niknipInput.text.toString())
                                .enqueue(object : Callback<LoginRegisterResponse>
                                {
                                    override fun onResponse(
                                        call: Call<LoginRegisterResponse>,
                                        response: Response<LoginRegisterResponse>
                                    ) {
                                      finish()
                                    }

                                    override fun onFailure(
                                        call: Call<LoginRegisterResponse>,
                                        t: Throwable
                                    ) {
                                        Snackbar.make(
                                            binds.root,
                                            "Gagal",
                                            Snackbar.LENGTH_SHORT
                                        ).show()
                                    }


                                })
                        }
                    }
                    else if (type!!.lowercase() == "siswa")
                    {
                        if (nisnInput.text!!.isEmpty() || nisInput.text!!.isEmpty() || namaInput.text!!.isEmpty()
                            || kontakInput.text!!.isEmpty() || emailInput.text!!.isEmpty()
                            || usernameInput.text!!.isEmpty() || passwordInput.text!!.isEmpty()
                            || confirmpassInput.text!!.isEmpty()
                        )
                        {
                            Snackbar.make(
                                binds.root,
                                "Mohon isi semua data!",
                                Snackbar.LENGTH_SHORT
                            )
                                .show()
                        }
                        else if (passwordInput.text.toString() != confirmpassInput.text.toString())
                            Snackbar.make(
                                binds.root,
                                "Kata sandi tidak sama!",
                                Snackbar.LENGTH_SHORT
                            ).show()
                        else
                        {
                            InitAPI.api.register(
                                type!!.lowercase(),
                                namaInput.text.toString(),
                                1,
                                emailInput.text.toString(),
                                usernameInput.text.toString(),
                                passwordInput.text.toString(),
                                nisInput.text.toString(),
                                nisnInput.text.toString(),
                                niknipInput.text.toString())
                                .enqueue(object : Callback<LoginRegisterResponse>
                                {
                                    override fun onResponse(
                                        call: Call<LoginRegisterResponse>,
                                        response: Response<LoginRegisterResponse>
                                    ) {
                                        finish()
                                    }

                                    override fun onFailure(
                                        call: Call<LoginRegisterResponse>,
                                        t: Throwable
                                    ) {
                                        Snackbar.make(
                                            binds.root,
                                            "Gagal",
                                            Snackbar.LENGTH_SHORT
                                        ).show()
                                    }


                                })
                        }
                    }
                }
            }

            alreadySignedUpButton.setOnClickListener {
                finish()
            }
        }
    }

    private fun dataInsert()
    {

    }
}