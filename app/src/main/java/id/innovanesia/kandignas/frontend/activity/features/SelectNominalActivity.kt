package id.innovanesia.kandignas.frontend.activity.features

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Rect
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialog
import id.innovanesia.kandignas.R
import id.innovanesia.kandignas.databinding.ActivitySelectNominalBinding
import id.innovanesia.kandignas.databinding.TransferConfirmationDialogBinding
import java.util.*

class SelectNominalActivity : AppCompatActivity()
{
    private lateinit var binds: ActivitySelectNominalBinding
    private lateinit var sharedPreference: SharedPreferences
    private val keyUser = "key.user_name"

    companion object
    {
        private const val ACTIVITY = "ACTIVITY"
        private const val USERNAME = "USERNAME"
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binds = ActivitySelectNominalBinding.inflate(layoutInflater)
        setContentView(binds.root)
        val activity = intent.getStringExtra(ACTIVITY)
        val username = intent.getStringExtra(USERNAME)!!
        val userBalance: Int? = null

        sharedPreference = getSharedPreferences("KanDigNas", Context.MODE_PRIVATE)

        binds.apply {
            setSupportActionBar(toolbar)
            toolbar.setNavigationOnClickListener {
                finish()
            }

            if (activity == "siswa")
            {

            }
            else
            {
                dividerInfo.visibility = View.GONE
                yourBalanceContent.visibility = View.GONE
            }

            setNominalButton()

            nominalInput.addTextChangedListener(object : TextWatcher
            {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int)
                {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int)
                {
                    fiveKNominal.setCardBackgroundColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.white
                        )
                    )
                    fiveK.setTextColor(ContextCompat.getColor(applicationContext, R.color.blue))
                    tenKNominal.setCardBackgroundColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.white
                        )
                    )
                    tenK.setTextColor(ContextCompat.getColor(applicationContext, R.color.blue))
                    twentyKNominal.setCardBackgroundColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.white
                        )
                    )
                    twentyK.setTextColor(ContextCompat.getColor(applicationContext, R.color.blue))
                    thirtyfiveKNominal.setCardBackgroundColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.white
                        )
                    )
                    thirtyfiveK.setTextColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.blue
                        )
                    )
                    fiftyKNominal.setCardBackgroundColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.white
                        )
                    )
                    fiftyK.setTextColor(ContextCompat.getColor(applicationContext, R.color.blue))
                    oneHundredKNominal.setCardBackgroundColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.white
                        )
                    )
                    oneHundredK.setTextColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.blue
                        )
                    )
                    okButton.setCardBackgroundColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.submarine
                        )
                    )
                    okText.setTextColor(ContextCompat.getColor(applicationContext, R.color.white))
                    okText.alpha = 1F

                    okButton.setOnClickListener {
                        val dialog = BottomSheetDialog(this@SelectNominalActivity)
                        val dialogBinding: TransferConfirmationDialogBinding =
                            TransferConfirmationDialogBinding.inflate(layoutInflater)
                        dialog.setContentView(dialogBinding.root)
                        dialog.setCancelable(true)
                        dialog.show()

                        dialogBinding.apply {

                            cancelButtonDialog.setOnClickListener {
                                dialog.dismiss()
                            }

                            okButtonDialog.setOnClickListener {
                                if (userBalance == 0 || userBalance!! < nominalInput.text.toString()
                                        .toInt()
                                )
                                {
                                    startActivity(
                                        Intent(
                                            this@SelectNominalActivity,
                                            TransactionSuccessActivity::class.java
                                        ).also {
                                            it.putExtra("STATUS", "failed")
                                            it.putExtra("ACTIVITY", activity)
                                            it.putExtra("TARGET_USER", username)
                                            it.putExtra("AMOUNT", nominalInput.text.toString())
                                        }
                                    )
                                    finish()
                                }
                                else
                                {
                                    /*if (activity == "koperasi")
                                    {

                                    }
                                    else if (activity == "siswa")
                                    {

                                    }*/
                                }
                            }
                        }
                    }

                    if (nominalInput.text.isEmpty())
                    {
                        okButton.setCardBackgroundColor(
                            ContextCompat.getColor(
                                applicationContext,
                                R.color.white
                            )
                        )
                        okText.setTextColor(
                            ContextCompat.getColor(
                                applicationContext,
                                R.color.grey
                            )
                        )
                        okText.alpha = 0.5F
                    }
                }

                override fun afterTextChanged(p0: Editable?)
                {
                }
            })
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true)
        {
            override fun handleOnBackPressed()
            {
                startActivity(Intent(this@SelectNominalActivity, ScanQRActivity::class.java)
                    .also {
                        it.putExtra("ACTIVITY", activity)
                    })
                finish()
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun getData()
    {
        binds.apply {

        }
    }

    @SuppressLint("SetTextI18n")
    private fun setNominalButton()
    {
        binds.apply {
            fiveKNominal.setOnClickListener {
                nominalInput.setText("5000")
                fiveKNominal.setCardBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.blue
                    )
                )
                fiveK.setTextColor(ContextCompat.getColor(applicationContext, R.color.white))
                tenKNominal.setCardBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.white
                    )
                )
                tenK.setTextColor(ContextCompat.getColor(applicationContext, R.color.blue))
                twentyKNominal.setCardBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.white
                    )
                )
                twentyK.setTextColor(ContextCompat.getColor(applicationContext, R.color.blue))
                thirtyfiveKNominal.setCardBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.white
                    )
                )
                thirtyfiveK.setTextColor(ContextCompat.getColor(applicationContext, R.color.blue))
                fiftyKNominal.setCardBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.white
                    )
                )
                fiftyK.setTextColor(ContextCompat.getColor(applicationContext, R.color.blue))
                oneHundredKNominal.setCardBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.white
                    )
                )
                oneHundredK.setTextColor(ContextCompat.getColor(applicationContext, R.color.blue))
            }

            tenKNominal.setOnClickListener {
                nominalInput.setText("10000")
                fiveKNominal.setCardBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.white
                    )
                )
                fiveK.setTextColor(ContextCompat.getColor(applicationContext, R.color.blue))
                tenKNominal.setCardBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.blue
                    )
                )
                tenK.setTextColor(ContextCompat.getColor(applicationContext, R.color.white))
                twentyKNominal.setCardBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.white
                    )
                )
                twentyK.setTextColor(ContextCompat.getColor(applicationContext, R.color.blue))
                thirtyfiveKNominal.setCardBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.white
                    )
                )
                thirtyfiveK.setTextColor(ContextCompat.getColor(applicationContext, R.color.blue))
                fiftyKNominal.setCardBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.white
                    )
                )
                fiftyK.setTextColor(ContextCompat.getColor(applicationContext, R.color.blue))
                oneHundredKNominal.setCardBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.white
                    )
                )
                oneHundredK.setTextColor(ContextCompat.getColor(applicationContext, R.color.blue))
            }

            twentyKNominal.setOnClickListener {
                nominalInput.setText("20000")
                fiveKNominal.setCardBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.white
                    )
                )
                fiveK.setTextColor(ContextCompat.getColor(applicationContext, R.color.blue))
                tenKNominal.setCardBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.white
                    )
                )
                tenK.setTextColor(ContextCompat.getColor(applicationContext, R.color.blue))
                twentyKNominal.setCardBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.blue
                    )
                )
                twentyK.setTextColor(ContextCompat.getColor(applicationContext, R.color.white))
                thirtyfiveKNominal.setCardBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.white
                    )
                )
                thirtyfiveK.setTextColor(ContextCompat.getColor(applicationContext, R.color.blue))
                fiftyKNominal.setCardBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.white
                    )
                )
                fiftyK.setTextColor(ContextCompat.getColor(applicationContext, R.color.blue))
                oneHundredKNominal.setCardBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.white
                    )
                )
                oneHundredK.setTextColor(ContextCompat.getColor(applicationContext, R.color.blue))
            }

            thirtyfiveKNominal.setOnClickListener {
                nominalInput.setText("35000")
                fiveKNominal.setCardBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.white
                    )
                )
                fiveK.setTextColor(ContextCompat.getColor(applicationContext, R.color.blue))
                tenKNominal.setCardBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.white
                    )
                )
                tenK.setTextColor(ContextCompat.getColor(applicationContext, R.color.blue))
                twentyKNominal.setCardBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.white
                    )
                )
                twentyK.setTextColor(ContextCompat.getColor(applicationContext, R.color.blue))
                thirtyfiveKNominal.setCardBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.blue
                    )
                )
                thirtyfiveK.setTextColor(ContextCompat.getColor(applicationContext, R.color.white))
                fiftyKNominal.setCardBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.white
                    )
                )
                fiftyK.setTextColor(ContextCompat.getColor(applicationContext, R.color.blue))
                oneHundredKNominal.setCardBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.white
                    )
                )
                oneHundredK.setTextColor(ContextCompat.getColor(applicationContext, R.color.blue))
            }

            fiftyKNominal.setOnClickListener {
                nominalInput.setText("50000")
                fiveKNominal.setCardBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.white
                    )
                )
                fiveK.setTextColor(ContextCompat.getColor(applicationContext, R.color.blue))
                tenKNominal.setCardBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.white
                    )
                )
                tenK.setTextColor(ContextCompat.getColor(applicationContext, R.color.blue))
                twentyKNominal.setCardBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.white
                    )
                )
                twentyK.setTextColor(ContextCompat.getColor(applicationContext, R.color.blue))
                thirtyfiveKNominal.setCardBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.white
                    )
                )
                thirtyfiveK.setTextColor(ContextCompat.getColor(applicationContext, R.color.blue))
                fiftyKNominal.setCardBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.blue
                    )
                )
                fiftyK.setTextColor(ContextCompat.getColor(applicationContext, R.color.white))
                oneHundredKNominal.setCardBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.white
                    )
                )
                oneHundredK.setTextColor(ContextCompat.getColor(applicationContext, R.color.blue))
            }

            oneHundredKNominal.setOnClickListener {
                nominalInput.setText("100000")
                fiveKNominal.setCardBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.white
                    )
                )
                fiveK.setTextColor(ContextCompat.getColor(applicationContext, R.color.blue))
                tenKNominal.setCardBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.white
                    )
                )
                tenK.setTextColor(ContextCompat.getColor(applicationContext, R.color.blue))
                twentyKNominal.setCardBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.white
                    )
                )
                twentyK.setTextColor(ContextCompat.getColor(applicationContext, R.color.blue))
                thirtyfiveKNominal.setCardBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.white
                    )
                )
                thirtyfiveK.setTextColor(ContextCompat.getColor(applicationContext, R.color.blue))
                fiftyKNominal.setCardBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.white
                    )
                )
                fiftyK.setTextColor(ContextCompat.getColor(applicationContext, R.color.blue))
                oneHundredKNominal.setCardBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.blue
                    )
                )
                oneHundredK.setTextColor(ContextCompat.getColor(applicationContext, R.color.white))
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