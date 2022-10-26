package id.innovanesia.kandignas.frontend.activity.features

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import id.innovanesia.kandignas.R
import id.innovanesia.kandignas.databinding.ActivityTransactionSuccessBinding
import id.innovanesia.kandignas.frontend.activity.koperasi.KoperasiMenuActivity
import id.innovanesia.kandignas.frontend.activity.siswa.SiswaMenuActivity

class TransactionSuccessActivity : AppCompatActivity()
{
    private lateinit var binds: ActivityTransactionSuccessBinding
    private val db = FirebaseFirestore.getInstance()

    companion object
    {
        const val STATUS = "STATUS"
        const val ACTIVITY = "ACTIVITY"
        const val TARGET_USER = "TARGET_USER"
        const val AMOUNT = "AMOUNT"
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binds = ActivityTransactionSuccessBinding.inflate(layoutInflater)
        setContentView(binds.root)

        val status = intent.getStringExtra(STATUS)!!
        val activity = intent.getStringExtra(ACTIVITY)!!
        val user = intent.getStringExtra(TARGET_USER)!!
        val amount = intent.getStringExtra(AMOUNT)!!

        binds.apply {
            if (status == "success")
            {
                Glide.with(this@TransactionSuccessActivity)
                    .load(R.drawable.success_alert)
                    .into(successIllustration)
                transactionStatus.text = "Transaksi Sukses!"
                db.collection("users").document(user).get()
                    .addOnSuccessListener {
                        nameContent.text = it.data?.get("fullname").toString()
                        if (it.data?.get("account_type").toString() == "umum")
                        {
                            idTextDesc.text = "NIK"
                            idContent.text = it.data?.get("nik").toString()
                        }
                        else
                        {
                            idTextDesc.text = "NISN"
                            idContent.text = it.data?.get("nisn").toString()
                        }
                        amountContent.text = "Rp ${amount.format("#,###")}"
                    }
            }
            else if (status == "failed")
            {
                Glide.with(this@TransactionSuccessActivity)
                    .load(R.drawable.failed_alert)
                    .into(successIllustration)
                transactionStatus.text = "Transaksi Gagal!"
                db.collection("users").document(user).get()
                    .addOnSuccessListener {
                        nameContent.text = it.data?.get("fullname").toString()
                        if (it.data?.get("account_type").toString() == "umum")
                        {
                            idTextDesc.text = "NIK"
                            idContent.text = it.data?.get("nik").toString()
                        }
                        else
                        {
                            idTextDesc.text = "NISN"
                            idContent.text = it.data?.get("nisn").toString()
                        }
                        amountContent.text = "Rp ${amount.format("#,###")}"
                    }
                seeTransactionHistoryButton.visibility = View.GONE
            }

            okButton.setOnClickListener {
                if (activity == "siswa")
                {
                    startActivity(
                        Intent(
                            this@TransactionSuccessActivity,
                            SiswaMenuActivity::class.java
                        )
                    )
                    finish()
                }
                else if (activity == "koperasi")
                {
                    startActivity(
                        Intent(
                            this@TransactionSuccessActivity,
                            KoperasiMenuActivity::class.java
                        )
                    )
                    finish()
                }
            }

            seeTransactionHistoryButton.setOnClickListener {
                startActivity(
                    Intent(
                        this@TransactionSuccessActivity,
                        TransactionHistoryActivity::class.java
                    ).also {
                        it.putExtra("ACTIVITY", activity)
                    }
                )
                finish()
            }
        }
    }
}