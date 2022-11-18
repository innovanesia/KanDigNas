package id.innovanesia.kandignas.frontend.activity

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import id.innovanesia.kandignas.R
import id.innovanesia.kandignas.backend.api.InitAPI
import id.innovanesia.kandignas.backend.response.LoginRegisterResponse
import id.innovanesia.kandignas.backend.response.SchoolResponse
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

        binds.apply {
            var type: String?

            niknipLayout.visibility = View.GONE
            schoolSpinnerLayout.visibility = View.GONE
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
                schoolSpinnerLayout.visibility = View.VISIBLE
                nisLayout.visibility = View.GONE
                nisnLayout.visibility = View.GONE
                namaLayout.visibility = View.VISIBLE
                kontakLayout.visibility = View.VISIBLE
                emailLayout.visibility = View.VISIBLE
                usernameLayout.visibility = View.VISIBLE
                passwordLayout.visibility = View.VISIBLE
                confirmpassLayout.visibility = View.VISIBLE
                viewForm(type!!)
            }

            umumType.setOnClickListener {
                val selectedId = typeGroup.checkedRadioButtonId
                val button: RadioButton = findViewById(selectedId)

                type = button.text.toString()
                niknipLayout.visibility = View.VISIBLE
                schoolSpinnerLayout.visibility = View.VISIBLE
                nisLayout.visibility = View.GONE
                nisnLayout.visibility = View.GONE
                namaLayout.visibility = View.VISIBLE
                kontakLayout.visibility = View.VISIBLE
                emailLayout.visibility = View.VISIBLE
                usernameLayout.visibility = View.VISIBLE
                passwordLayout.visibility = View.VISIBLE
                confirmpassLayout.visibility = View.VISIBLE
                viewForm(type!!)
            }

            siswaType.setOnClickListener {
                val selectedId = typeGroup.checkedRadioButtonId
                val button: RadioButton = findViewById(selectedId)

                type = button.text.toString()
                niknipLayout.visibility = View.VISIBLE
                schoolSpinnerLayout.visibility = View.VISIBLE
                nisLayout.visibility = View.VISIBLE
                nisnLayout.visibility = View.VISIBLE
                namaLayout.visibility = View.VISIBLE
                kontakLayout.visibility = View.VISIBLE
                emailLayout.visibility = View.VISIBLE
                usernameLayout.visibility = View.VISIBLE
                passwordLayout.visibility = View.VISIBLE
                confirmpassLayout.visibility = View.VISIBLE
                viewForm(type!!)
            }
        }
    }

    private fun viewForm(type: String)
    {
        var schoolId = 0
        binds.apply {
            setSupportActionBar(toolbar)

            toolbar.setNavigationOnClickListener {
                finish()
            }

            InitAPI.api.getSchoolList()
                .enqueue(object : Callback<SchoolResponse>
                {
                    override fun onResponse(
                        call: Call<SchoolResponse>,
                        response: Response<SchoolResponse>
                    )
                    {
                        val result = response.body()?.schools
                        val data: ArrayList<String> = ArrayList()
                        data.add("Pilih Sekolah")
                        for (i in 0 until result?.size!!)
                        {
                            data.add(
                                result[i].name
                            )
                            val adapter = ArrayAdapter(
                                this@RegisterActivity,
                                R.layout.school_spinner_item,
                                R.id.spinner_item_text,
                                data
                            )
                            schoolSpinner.adapter = adapter
                            schoolSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
                            {
                                override fun onItemSelected(
                                    parent: AdapterView<*>?,
                                    view: View?,
                                    position: Int,
                                    id: Long
                                )
                                {
                                    schoolId = position
                                    Log.e("Selected School ID", schoolId.toString())
                                }

                                override fun onNothingSelected(parent: AdapterView<*>?)
                                {}
                            }
                        }
                    }

                    override fun onFailure(call: Call<SchoolResponse>, t: Throwable)
                    {
                        Snackbar.make(
                            binds.root,
                            "Daftar sekolah tidak dapat diambil.\nMohon periksa koneksi!",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                })

            Log.e("Selected School ID", schoolId.toString())

            submitButton.setOnClickListener {
                if (type.lowercase() == "umum" || type.lowercase() == "kantin")
                {
                    if (niknipInput.text!!.isEmpty() || namaInput.text!!.isEmpty()
                        || kontakInput.text!!.isEmpty() || emailInput.text!!.isEmpty()
                        || usernameInput.text!!.isEmpty() || passwordInput.text!!.isEmpty()
                        || confirmpassInput.text!!.isEmpty() || schoolId == 0
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
                        dataInsert(type, schoolId)
                    }
                }
                else if (type.lowercase() == "siswa")
                {
                    if (nisnInput.text!!.isEmpty() || nisInput.text!!.isEmpty() || namaInput.text!!.isEmpty()
                        || kontakInput.text!!.isEmpty() || emailInput.text!!.isEmpty()
                        || usernameInput.text!!.isEmpty() || passwordInput.text!!.isEmpty()
                        || confirmpassInput.text!!.isEmpty() || schoolId == 0
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
                        dataInsert(type, schoolId)
                    }
                }
            }

            alreadySignedUpButton.setOnClickListener {
                finish()
            }
        }
    }

    private fun dataInsert(type: String, schoolId: Int)
    {
        binds.apply {
            InitAPI.api.register(
                type.lowercase(),
                namaInput.text.toString(),
                schoolId,
                emailInput.text.toString(),
                usernameInput.text.toString(),
                passwordInput.text.toString(),
                kontakInput.text.toString(),
                if (type == "kantin" || type == "umum")
                    ""
                else
                    nisInput.text.toString(),
                if (type == "kantin" || type == "umum")
                    ""
                else
                    nisnInput.text.toString(),
                niknipInput.text.toString()
            ).enqueue(object : Callback<LoginRegisterResponse>
            {
                override fun onResponse(
                    call: Call<LoginRegisterResponse>,
                    response: Response<LoginRegisterResponse>
                )
                {
                    Log.e("Response Register", response.body().toString())
                    if (response.body()?.message == "User authenticated successfully")
                    {
                        finish()
                        Toast.makeText(
                            this@RegisterActivity,
                            "Berhasil terdaftar!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    else if (response.body() == null)
                    {
                        Snackbar.make(
                            binds.root,
                            "Registrasi gagal. Mohon periksa ulang formulir!",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(
                    call: Call<LoginRegisterResponse>,
                    t: Throwable
                )
                {
                    t.printStackTrace()
                    Snackbar.make(
                        binds.root,
                        "Gagal terdaftar",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            })
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