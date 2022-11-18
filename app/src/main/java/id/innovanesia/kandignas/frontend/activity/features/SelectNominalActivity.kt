package id.innovanesia.kandignas.frontend.activity.features

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Rect
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialog
import id.innovanesia.kandignas.R
import id.innovanesia.kandignas.backend.api.InitAPI
import id.innovanesia.kandignas.backend.models.Users
import id.innovanesia.kandignas.backend.response.AccountResponse
import id.innovanesia.kandignas.backend.response.TransactionFlowResponse
import id.innovanesia.kandignas.databinding.ActivitySelectNominalBinding
import id.innovanesia.kandignas.databinding.TransferConfirmationDialogBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

class SelectNominalActivity : AppCompatActivity()
{
    private lateinit var binds: ActivitySelectNominalBinding
    private lateinit var sharedPreference: SharedPreferences
    private val keyToken = "key.token"

    companion object
    {
        private const val ACTIVITY = "ACTIVITY"
        private const val TOKEN = "TOKEN"
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binds = ActivitySelectNominalBinding.inflate(layoutInflater)
        setContentView(binds.root)

        val activity = intent.getStringExtra(ACTIVITY)!!
        val targetToken = intent.getStringExtra(TOKEN)!!

        sharedPreference = getSharedPreferences("KanDigNas", Context.MODE_PRIVATE)
        val userToken = sharedPreference.getString(keyToken, null)!!
        var target: Users? = null
        var user: Users? = null

        binds.apply {
            setSupportActionBar(toolbar)

            toolbar.setNavigationOnClickListener {
                finish()
            }

            selectNominalLoading.visibility = View.VISIBLE

            InitAPI.api.getAccount("Bearer $targetToken")
                .enqueue(object : Callback<AccountResponse>
                {
                    override fun onResponse(
                        call: Call<AccountResponse>,
                        response: Response<AccountResponse>
                    )
                    {
                        target = response.body()?.user!!
                        namaField.text = target?.fullname
                        usernameField.text = target?.username
                        typeField.text = target?.type
                        selectNominalLoading.visibility = View.GONE
                    }

                    override fun onFailure(call: Call<AccountResponse>, t: Throwable)
                    {
                        t.printStackTrace()
                        Toast.makeText(
                            this@SelectNominalActivity,
                            "Terjadi kesalahan, mohon coba kembali.",
                            Toast.LENGTH_SHORT
                        ).show()
                        startActivity(Intent(this@SelectNominalActivity, ScanQRActivity::class.java)
                            .also {
                                it.putExtra("ACTIVITY", activity)
                            })
                        finish()
                    }
                })

            InitAPI.api.getAccount("Bearer $userToken")
                .enqueue(object : Callback<AccountResponse>
                {
                    override fun onResponse(
                        call: Call<AccountResponse>,
                        response: Response<AccountResponse>
                    )
                    {
                        user = response.body()?.user!!
                        if (activity == "siswa")
                        {
                            val format: NumberFormat = DecimalFormat("#,###")
                            val balance = format.format(user?.balance.toString().toInt())
                            yourBalanceField.text = "Rp $balance"

                            if (user?.balance == 0)
                                yourBalanceField.setTextColor(
                                    ContextCompat.getColor(
                                        this@SelectNominalActivity,
                                        R.color.red
                                    )
                                )
                            else
                                yourBalanceField.setTextColor(
                                    ContextCompat.getColor(
                                        this@SelectNominalActivity,
                                        R.color.blue
                                    )
                                )
                            selectNominalLoading.visibility = View.GONE
                        }
                        else
                        {
                            dividerInfo.visibility = View.GONE
                            yourBalanceContent.visibility = View.GONE
                        }
                    }

                    override fun onFailure(call: Call<AccountResponse>, t: Throwable)
                    {
                        t.printStackTrace()
                        Toast.makeText(
                            this@SelectNominalActivity,
                            "Terjadi kesalahan, mohon coba kembali.",
                            Toast.LENGTH_SHORT
                        ).show()
                        startActivity(Intent(this@SelectNominalActivity, ScanQRActivity::class.java)
                            .also {
                                it.putExtra("ACTIVITY", activity)
                            })
                        finish()
                    }
                })

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
                            nameTargetField.text = target?.fullname
                            if (target?.nik != "")
                            {
                                idTarget.text = "NIK"
                                idTargetField.text = target?.nik
                            }
                            else
                            {
                                idTarget.text = "NISN"
                                idTargetField.text = target?.nisn
                            }
                            val format: NumberFormat = DecimalFormat("#,###")
                            amountTargetField.text =
                                "Rp " + format.format(nominalInput.text.toString().toInt())

                            cancelButtonDialog.setOnClickListener {
                                dialog.dismiss()
                            }

                            okButtonDialog.setOnClickListener {
                                if (user?.type != "koperasi" &&
                                    (user?.balance == 0 || user?.balance!! < nominalInput.text.toString()
                                        .toInt())
                                )
                                {
                                    transactionStatus(
                                        target!!,
                                        nominalInput.text.toString().toInt(),
                                        "failed"
                                    )
                                    dialog.dismiss()
                                }
                                else
                                {
                                    if ((user?.type == "siswa" || user?.type == "umum")
                                        && (target?.type == "siswa" || target?.type == "umum")
                                    )
                                    {
                                        InitAPI.api.transferTransaction(
                                            "Bearer $userToken",
                                            target!!.id,
                                            nominalInput.text.toString().toInt()
                                        ).enqueue(object : Callback<TransactionFlowResponse>
                                        {
                                            override fun onResponse(
                                                call: Call<TransactionFlowResponse>,
                                                response: Response<TransactionFlowResponse>
                                            )
                                            {
                                                transactionStatus(
                                                    target!!,
                                                    nominalInput.text.toString().toInt(),
                                                    "success"
                                                )
                                                dialog.dismiss()
                                            }

                                            override fun onFailure(
                                                call: Call<TransactionFlowResponse>,
                                                t: Throwable
                                            )
                                            {
                                                t.printStackTrace()
                                                transactionStatus(
                                                    target!!,
                                                    nominalInput.text.toString().toInt(),
                                                    "failed"
                                                )
                                                dialog.dismiss()
                                            }
                                        })
                                    }
                                    else if ((user?.type == "siswa" || user?.type == "umum")
                                        && target?.type == "kantin"
                                    )
                                    {
                                        InitAPI.api.paymentTransaction(
                                            "Bearer $userToken",
                                            target!!.id,
                                            nominalInput.text.toString().toInt()
                                        ).enqueue(object : Callback<TransactionFlowResponse>
                                        {
                                            override fun onResponse(
                                                call: Call<TransactionFlowResponse>,
                                                response: Response<TransactionFlowResponse>
                                            )
                                            {
                                                transactionStatus(
                                                    target!!,
                                                    nominalInput.text.toString().toInt(),
                                                    "success"
                                                )
                                                dialog.dismiss()
                                            }

                                            override fun onFailure(
                                                call: Call<TransactionFlowResponse>,
                                                t: Throwable
                                            )
                                            {
                                                t.printStackTrace()
                                                transactionStatus(
                                                    target!!,
                                                    nominalInput.text.toString().toInt(),
                                                    "failed"
                                                )
                                                dialog.dismiss()
                                            }
                                        })
                                    }
                                    else if (user?.type == "koperasi" &&
                                        (target?.type == "siswa" || target?.type == "umum")
                                    )
                                    {
                                        InitAPI.api.topupTransaction(
                                            "Bearer $userToken",
                                            target!!.id,
                                            nominalInput.text.toString().toInt()
                                        ).enqueue(object : Callback<TransactionFlowResponse>
                                        {
                                            override fun onResponse(
                                                call: Call<TransactionFlowResponse>,
                                                response: Response<TransactionFlowResponse>
                                            )
                                            {
                                                transactionStatus(
                                                    target!!,
                                                    nominalInput.text.toString().toInt(),
                                                    "success"
                                                )
                                                dialog.dismiss()
                                            }

                                            override fun onFailure(
                                                call: Call<TransactionFlowResponse>,
                                                t: Throwable
                                            )
                                            {
                                                t.printStackTrace()
                                                transactionStatus(
                                                    target!!,
                                                    nominalInput.text.toString().toInt(),
                                                    "failed"
                                                )
                                                dialog.dismiss()
                                            }
                                        })
                                    }
                                    else if (user?.type == "koperasi" && target?.type == "kantin")
                                    {
                                        if (target?.balance == 0 || target?.balance!! < nominalInput.text.toString().toInt())
                                        {
                                            transactionStatus(
                                                target!!,
                                                nominalInput.text.toString().toInt(),
                                                "failed"
                                            )
                                            dialog.dismiss()
                                        }
                                        else
                                        {
                                            InitAPI.api.withdrawTransaction(
                                                "Bearer $userToken",
                                                target!!.id,
                                                nominalInput.text.toString().toInt()
                                            ).enqueue(object : Callback<TransactionFlowResponse>
                                            {
                                                override fun onResponse(
                                                    call: Call<TransactionFlowResponse>,
                                                    response: Response<TransactionFlowResponse>
                                                )
                                                {
                                                    Log.e("Withdraw Response", response.body().toString())
                                                    transactionStatus(
                                                        target!!,
                                                        nominalInput.text.toString().toInt(),
                                                        "success"
                                                    )
                                                    dialog.dismiss()
                                                }

                                                override fun onFailure(
                                                    call: Call<TransactionFlowResponse>,
                                                    t: Throwable
                                                )
                                                {
                                                    t.printStackTrace()
                                                    transactionStatus(
                                                        target!!,
                                                        nominalInput.text.toString().toInt(),
                                                        "failed"
                                                    )
                                                    dialog.dismiss()
                                                }
                                            })
                                        }
                                    }
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

    private fun transactionStatus(target: Users, nominal: Int, status: String)
    {
        binds.apply {
            startActivity(
                Intent(
                    this@SelectNominalActivity,
                    TransactionStatusActivity::class.java
                ).also {
                    it.putExtra("STATUS", status)
                    it.putExtra("TARGET", target)
                    it.putExtra("AMOUNT", nominal)
                }
            )
            finish()
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