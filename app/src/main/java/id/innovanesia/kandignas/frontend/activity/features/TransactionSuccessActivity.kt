package id.innovanesia.kandignas.frontend.activity.features

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import id.innovanesia.kandignas.R
import id.innovanesia.kandignas.backend.models.Users
import id.innovanesia.kandignas.databinding.ActivityTransactionSuccessBinding
import java.text.DecimalFormat
import java.text.NumberFormat

class TransactionSuccessActivity : AppCompatActivity()
{
    private lateinit var binds: ActivityTransactionSuccessBinding

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
        binds = ActivityTransactionSuccessBinding.inflate(layoutInflater)
        setContentView(binds.root)

        val status = intent.getStringExtra(STATUS)!!
        val user: Users = if (Build.VERSION.SDK_INT >= 33)
            intent.getParcelableExtra(TARGET, Users::class.java)!!
        else
            intent.getParcelableExtra(TARGET)!!
        val amount = intent.getIntExtra(AMOUNT, 0)

        binds.apply {
            nameContent.text = user.fullname
            val format: NumberFormat = DecimalFormat("#,###")
            amountContent.text = "Rp " + format.format(amount)
            if (status == "success")
            {
                Glide.with(this@TransactionSuccessActivity)
                    .load(R.drawable.success_alert)
                    .into(successIllustration)
                transactionStatus.text = "Transaksi Sukses!"
                if (user.nik != "")
                {
                    idTextDesc.text = "NIK"
                    idContent.text = user.nik
                }
                else
                {
                    idTextDesc.text = "NISN"
                    idContent.text = user.nisn
                }
            }
            else if (status == "failed")
            {
                Glide.with(this@TransactionSuccessActivity)
                    .load(R.drawable.failed_alert)
                    .into(successIllustration)
                transactionStatus.text = "Transaksi Gagal!"
                seeTransactionHistoryButton.visibility = View.GONE
                if (user.nik != "")
                {
                    idTextDesc.text = "NIK"
                    idContent.text = user.nik
                }
                else
                {
                    idTextDesc.text = "NISN"
                    idContent.text = user.nisn
                }
            }

            okButton.setOnClickListener {
                finish()
            }

            seeTransactionHistoryButton.setOnClickListener {
                startActivity(
                    Intent(
                        this@TransactionSuccessActivity,
                        TransactionHistoryActivity::class.java
                    )
                )
                finish()
            }
        }
    }
}