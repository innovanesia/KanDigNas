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
import id.innovanesia.kandignas.databinding.AccountTypeViewBinding
import id.innovanesia.kandignas.databinding.GeneralAccountFormBinding
import id.innovanesia.kandignas.databinding.StudentsAccountFormBinding

class RegisterActivity : AppCompatActivity()
{
    private lateinit var accTypeBinds: AccountTypeViewBinding
    private lateinit var generalBinds: GeneralAccountFormBinding
    private lateinit var studentBinds: StudentsAccountFormBinding
    private var general: Boolean = false
    private var student: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        accTypeBinds = AccountTypeViewBinding.inflate(layoutInflater)
        setContentView(accTypeBinds.root)

        accTypeBinds.apply {
            setSupportActionBar(toolbar)

            generalButton.setOnClickListener {
                generalBinds = GeneralAccountFormBinding.inflate(layoutInflater)
                setContentView(generalBinds.root)

                generalType()
                general = true
            }

            studentButton.setOnClickListener {
                studentBinds = StudentsAccountFormBinding.inflate(layoutInflater)
                setContentView(studentBinds.root)

                studentType()
                student = true
            }
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true)
        {
            override fun handleOnBackPressed()
            {
                if (general)
                {
                    setContentView(accTypeBinds.root)
                    general = false
                }
                else if (student)
                {
                    setContentView(accTypeBinds.root)
                    student = false
                }
                else
                {
                    finish()
                }
            }
        })
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

    private fun generalType()
    {
        generalBinds.apply {
            setSupportActionBar(toolbar)

            toolbar.setNavigationOnClickListener {
                setContentView(accTypeBinds.root)
                general = false
            }

            koperasiType.setOnClickListener {
                val selectedId = typeGroup.checkedRadioButtonId
                val button: RadioButton = findViewById(selectedId)

                Log.d("Selected Button", button.text.toString())
            }

            kantinType.setOnClickListener {
                val selectedId = typeGroup.checkedRadioButtonId
                val button: RadioButton = findViewById(selectedId)

                Log.d("Selected Button", button.text.toString())
            }

            umumType.setOnClickListener {
                val selectedId = typeGroup.checkedRadioButtonId
                val button: RadioButton = findViewById(selectedId)

                Log.d("Selected Button", button.text.toString())
            }

            submitButton.setOnClickListener {
                if (niknipInput.text!!.isEmpty() || namaInput.text!!.isEmpty()
                    || kontakInput.text!!.isEmpty() || emailInput.text!!.isEmpty()
                    || usernameInput.text!!.isEmpty() || passwordInput.text!!.isEmpty()
                    || confirmpassInput.text!!.isEmpty()
                )
                {
                    Snackbar.make(generalBinds.root, "Mohon isi semua data!", Snackbar.LENGTH_SHORT)
                        .show()
                }
                else if (passwordInput.text.toString() != confirmpassInput.text.toString())
                {
                    Snackbar.make(
                        generalBinds.root,
                        "Kata sandi tidak sama!",
                        Snackbar.LENGTH_SHORT
                    )
                        .show()
                }
                else
                {
                    Toast.makeText(this@RegisterActivity, "Registrasi sukses!", Toast.LENGTH_SHORT)
                        .show()
                    finish()
                }
            }

            loginText.setOnClickListener {
                finish()
            }
        }
    }

    private fun studentType()
    {
        studentBinds.apply {
            setSupportActionBar(toolbar)

            toolbar.setNavigationOnClickListener {
                setContentView(accTypeBinds.root)
                student = false
            }

            submitButton.setOnClickListener {
                if (nisnInput.text!!.isEmpty() || nikInput.text!!.isEmpty()
                    || namaInput.text!!.isEmpty() || nisInput.text!!.isEmpty()
                    || kontakInput.text!!.isEmpty() || emailInput.text!!.isEmpty()
                    || usernameInput.text!!.isEmpty() || passwordInput.text!!.isEmpty()
                    || confirmpassInput.text!!.isEmpty()
                )
                {
                    Snackbar.make(studentBinds.root, "Mohon isi semua data!", Snackbar.LENGTH_SHORT)
                        .show()
                }
                else if (passwordInput.text.toString() != confirmpassInput.text.toString())
                {
                    Snackbar.make(
                        studentBinds.root,
                        "Kata sandi tidak sama!",
                        Snackbar.LENGTH_SHORT
                    )
                        .show()
                }
                else
                {
                    Toast.makeText(this@RegisterActivity, "Registrasi sukses!", Toast.LENGTH_SHORT)
                        .show()
                    finish()
                }
            }

            loginText.setOnClickListener {
                finish()
            }
        }
    }
}