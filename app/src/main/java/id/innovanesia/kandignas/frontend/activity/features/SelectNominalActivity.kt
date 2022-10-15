package id.innovanesia.kandignas.frontend.activity.features

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import id.innovanesia.kandignas.R
import id.innovanesia.kandignas.databinding.ActivitySelectNominalBinding
import id.innovanesia.kandignas.frontend.activity.koperasi.KoperasiMenuActivity
import id.innovanesia.kandignas.frontend.activity.siswa.SiswaMenuActivity

class SelectNominalActivity : AppCompatActivity()
{
    private lateinit var binds: ActivitySelectNominalBinding

    companion object
    {
        private const val API = "API"
        private const val ACTIVITY = "ACTIVITY"
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binds = ActivitySelectNominalBinding.inflate(layoutInflater)
        setContentView(binds.root)

        val userApi = intent.getStringExtra(API)
        val activity = intent.getStringExtra(ACTIVITY)

        Log.d("User API", userApi.toString())
        Log.d("Activity type", activity.toString())

        binds.apply {
            setSupportActionBar(toolbar)
            toolbar.setNavigationOnClickListener {
                finish()
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
                        if (activity == "koperasi")
                        {
                            startActivity(
                                Intent(
                                    this@SelectNominalActivity,
                                    KoperasiMenuActivity::class.java
                                )
                            )
                            finish()
                        }
                        else if (activity == "siswa")
                        {
                            startActivity(
                                Intent(
                                    this@SelectNominalActivity,
                                    SiswaMenuActivity::class.java
                                )
                            )
                            finish()
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
    private fun setNominalButton()
    {
        binds.apply {
            fiveKNominal.setOnClickListener {
                nominalInput.setText("5,000")
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
                nominalInput.setText("10,000")
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
                nominalInput.setText("20,000")
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
                nominalInput.setText("35,000")
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
                nominalInput.setText("50,000")
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
                nominalInput.setText("100,000")
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
}