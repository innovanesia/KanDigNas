package id.innovanesia.kandignas.frontend.activity

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import id.innovanesia.kandignas.backend.models.Users
import id.innovanesia.kandignas.databinding.RegisterAccountFormBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binds: RegisterAccountFormBinding
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binds = RegisterAccountFormBinding.inflate(layoutInflater)
        setContentView(binds.root)

        viewForm()
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v: View? = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    val imm: InputMethodManager =
                        getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

    private fun viewForm() {
        binds.apply {
            setSupportActionBar(toolbar)

            toolbar.setNavigationOnClickListener {
                finish()
            }

            var type: String? = null

            kantinType.setOnClickListener {
                val selectedId = typeGroup.checkedRadioButtonId
                val button: RadioButton = findViewById(selectedId)

                type = button.text.toString()

                Log.d("Selected Button", type!!.lowercase())
            }

            umumType.setOnClickListener {
                val selectedId = typeGroup.checkedRadioButtonId
                val button: RadioButton = findViewById(selectedId)

                type = button.text.toString()

                Log.d("Selected Button", type!!.lowercase())
            }

            siswaType.setOnClickListener {
                val selectedId = typeGroup.checkedRadioButtonId
                val button: RadioButton = findViewById(selectedId)

                type = button.text.toString()

                Log.d("Selected Button", type!!.lowercase())
            }

            submitButton.setOnClickListener {
                if (type!!.lowercase() == "umum" || type!!.lowercase() == "kantin") {
                    if (niknipInput.text!!.isEmpty() || namaInput.text!!.isEmpty()
                        || kontakInput.text!!.isEmpty() || emailInput.text!!.isEmpty()
                        || usernameInput.text!!.isEmpty() || passwordInput.text!!.isEmpty()
                        || confirmpassInput.text!!.isEmpty()
                    ) {
                        Snackbar.make(binds.root, "Mohon isi semua data!", Snackbar.LENGTH_SHORT)
                            .show()
                    } else
                        dataInsert(type)
                } else if (type!!.lowercase() == "siswa") {
                    if (nisnInput.text!!.isEmpty() || nisInput.text!!.isEmpty() || namaInput.text!!.isEmpty()
                        || kontakInput.text!!.isEmpty() || emailInput.text!!.isEmpty()
                        || usernameInput.text!!.isEmpty() || passwordInput.text!!.isEmpty()
                        || confirmpassInput.text!!.isEmpty()
                    ) {
                        Snackbar.make(binds.root, "Mohon isi semua data!", Snackbar.LENGTH_SHORT)
                            .show()
                    } else
                        dataInsert(type)
                } else if (passwordInput.text.toString() != confirmpassInput.text.toString()) {
                    Snackbar.make(
                        binds.root,
                        "Kata sandi tidak sama!",
                        Snackbar.LENGTH_SHORT
                    )
                        .show()
                }
            }

            loginText.setOnClickListener {
                finish()
            }
        }
    }

    private fun dataInsert(type: String?) {
        binds.apply {
            var exist = false

            val user = Users(
                type!!.lowercase(),
                0,
                emailInput.text.toString(),
                namaInput.text.toString(),
                niknipInput.text.toString(),
                nisInput.text.toString(),
                nisnInput.text.toString(),
                passwordInput.text.toString(),
                kontakInput.text.toString(),
                usernameInput.text.toString()
            )

            if (type.lowercase() == "umum" || type.lowercase() == "siswa") {
                db.collection("users").get()
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            for (docs in it.result) {
                                if (usernameInput.text.toString() == docs.id) {
                                    exist = true
                                }
                            }
                            if (exist) {
                                Snackbar.make(
                                    binds.root,
                                    "Username sudah terdaftar! Gunakan username lain.",
                                    Snackbar.LENGTH_SHORT
                                ).show()
                            } else {
                                db.collection("users").document(usernameInput.text.toString())
                                    .set(user)
                                    .addOnSuccessListener {
                                        Log.d("Data insert", "Success!")
                                        Toast.makeText(
                                            this@RegisterActivity,
                                            "Registrasi sukses!",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                        finish()
                                    }
                                    .addOnFailureListener {
                                        Log.e("Data insert", "Failed!")
                                        Snackbar.make(
                                            binds.root,
                                            "Registrasi gagal",
                                            Snackbar.LENGTH_SHORT
                                        ).show()
                                    }
                            }
                        }
                    }
            } else {
                if (exist) {
                    Snackbar.make(
                        binds.root,
                        "Username sudah terdaftar! Gunakan username lain.",
                        Snackbar.LENGTH_SHORT
                    ).show()
                } else {
                    db.collection(type.lowercase()).document(usernameInput.text.toString())
                        .set(user)
                        .addOnSuccessListener {
                            Log.d("Data insert", "Success!")
                            Toast.makeText(
                                this@RegisterActivity,
                                "Registrasi sukses!",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            finish()
                        }
                        .addOnFailureListener {
                            Log.e("Data insert", "Failed!")
                            Snackbar.make(
                                binds.root,
                                "Registrasi gagal",
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                }
                Toast.makeText(this@RegisterActivity, "Registrasi sukses!", Toast.LENGTH_SHORT)
                    .show()
                finish()
            }


        }
    }
}