package id.innovanesia.kandignas.frontend.activity.features

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import id.innovanesia.kandignas.R
import id.innovanesia.kandignas.backend.api.InitAPI
import id.innovanesia.kandignas.backend.models.Users
import id.innovanesia.kandignas.backend.response.AccountResponse
import id.innovanesia.kandignas.databinding.ActivityTransactionStatusBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import java.text.NumberFormat

class TransactionStatusActivity : AppCompatActivity()
{
    private lateinit var binds: ActivityTransactionStatusBinding
    private lateinit var sharedPreference: SharedPreferences
    private val keyToken = "key.token"

    companion object
    {
        const val STATUS = "STATUS"
        const val TARGET = "TARGET"
        const val AMOUNT = "AMOUNT"
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binds = ActivityTransactionStatusBinding.inflate(layoutInflater)
        setContentView(binds.root)

        sharedPreference = getSharedPreferences("KanDigNas", Context.MODE_PRIVATE)

        val token = sharedPreference.getString(keyToken, null)!!
        val status = intent.getStringExtra(STATUS)!!

        val user: Users = if (Build.VERSION.SDK_INT >= 33)
            intent.getParcelableExtra(TARGET, Users::class.java)!!
        else
            intent.getParcelableExtra(TARGET)!!

        val amount = intent.getIntExtra(AMOUNT, 0)

        binds.apply {
            receiverNameContent.text = user.fullname
            getSenderData(token)
            val format: NumberFormat = DecimalFormat("#,###")
            amountContent.text = "Rp " + format.format(amount)
            if (status == "success")
            {
                Glide.with(this@TransactionStatusActivity)
                    .load(R.drawable.success_alert)
                    .into(successIllustration)
                transactionStatus.text = "Transaksi Sukses!"
                if (user.nik != "")
                {
                    receiverIdTextDesc.text = "NIK"
                    receiverIdContent.text = user.nik
                }
                else
                {
                    receiverIdTextDesc.text = "NISN"
                    receiverIdContent.text = user.nisn
                }
            }
            else if (status == "failed")
            {
                Glide.with(this@TransactionStatusActivity)
                    .load(R.drawable.failed_alert)
                    .into(successIllustration)
                transactionStatus.text = "Transaksi Gagal!"
                seeTransactionHistoryButton.visibility = View.GONE
                if (user.nik != "")
                {
                    receiverIdTextDesc.text = "NIK"
                    receiverIdContent.text = user.nik
                }
                else
                {
                    receiverIdTextDesc.text = "NISN"
                    receiverIdContent.text = user.nisn
                }
            }

            okButton.setOnClickListener {
                finish()
            }

            seeTransactionHistoryButton.setOnClickListener {
                startActivity(
                    Intent(
                        this@TransactionStatusActivity,
                        TransactionHistoryActivity::class.java
                    )
                )
                finish()
            }
        }
    }

    private fun getSenderData(token: String)
    {
        binds.apply {
            InitAPI.api.getAccount("Bearer $token")
                .enqueue(object : Callback<AccountResponse>
                {
                    @SuppressLint("SetTextI18n")
                    override fun onResponse(
                        call: Call<AccountResponse>,
                        response: Response<AccountResponse>
                    )
                    {
                        senderNameContent.text = response.body()!!.user.fullname
                        if (response.body()!!.user.nik != "")
                        {
                            senderIdTextDesc.text = "NIK"
                            senderIdContent.text = response.body()!!.user.nik
                        }
                        else
                        {
                            senderIdTextDesc.text = "NISN"
                            senderIdContent.text = response.body()!!.user.nisn
                        }
                    }

                    override fun onFailure(call: Call<AccountResponse>, t: Throwable)
                    {
                        Toast.makeText(
                            this@TransactionStatusActivity,
                            "Gagal mendapatkan data. Mohon periksa koneksi internet!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        }
    }
}