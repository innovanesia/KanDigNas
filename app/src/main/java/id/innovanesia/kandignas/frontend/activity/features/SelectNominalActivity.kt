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
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import id.innovanesia.kandignas.R
import id.innovanesia.kandignas.backend.models.Transactions
import id.innovanesia.kandignas.databinding.ActivitySelectNominalBinding
import id.innovanesia.kandignas.databinding.TransferConfirmationDialogBinding
import id.innovanesia.kandignas.frontend.activity.koperasi.KoperasiMenuActivity
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class SelectNominalActivity : AppCompatActivity()
{
    private lateinit var binds: ActivitySelectNominalBinding
    private lateinit var sharedPreference: SharedPreferences
    private val keyUser = "key.user_name"
    private val db = FirebaseFirestore.getInstance()

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
        var userbalance: Int? = null
        var targetbalance: Int? = null

        sharedPreference = getSharedPreferences("KanDigNas", Context.MODE_PRIVATE)
        val user = sharedPreference.getString(keyUser, null)!!

        binds.apply {
            setSupportActionBar(toolbar)
            toolbar.setNavigationOnClickListener {
                finish()
            }

            if (activity == "siswa")
            {
                db.collection("users").document(user).get()
                    .addOnSuccessListener {
                        userbalance = it.data?.get("balance").toString().toInt()
                        val format: NumberFormat = DecimalFormat("#,###")
                        yourBalanceField.text = "Rp ${format.format(userbalance!!.toInt())}"
                    }
            }
            else
            {
                dividerInfo.visibility = View.GONE
                yourBalanceContent.visibility = View.GONE
            }

            db.collection("users").document(username).get()
                .addOnSuccessListener {
                    targetbalance = it.data?.get("balance").toString().toInt()
                    namaField.text = it.data?.get("fullname").toString()
                    usernameField.text = it.data?.get("username").toString()
                    if (it.data?.get("account_type").toString() == "siswa")
                        typeField.text = "Siswa"
                    else if (it.data?.get("account_type").toString() == "umum")
                        typeField.text = "Umum"
                }
                .addOnFailureListener {
                    onBackPressedDispatcher.onBackPressed()
                    Toast.makeText(
                        this@SelectNominalActivity,
                        "QR Code salah!",
                        Toast.LENGTH_SHORT
                    ).show()
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
                            db.collection("users").document(username).get()
                                .addOnSuccessListener {
                                    nameTargetField.text = it.data?.get("fullname").toString()
                                    if (it.data?.get("nik").toString() != "")
                                    {
                                        idTarget.text = "NIK"
                                        idTargetField.text = it.data?.get("nik").toString()
                                    }
                                    else
                                    {
                                        idTarget.text = "NISN"
                                        idTargetField.text = it.data?.get("nisn").toString()
                                    }
                                    amountTargetField.text = nominalInput.text.toString()
                                }

                            cancelButtonDialog.setOnClickListener {
                                dialog.dismiss()
                            }

                            okButtonDialog.setOnClickListener {
                                if (userbalance == 0 || userbalance!! < nominalInput.text.toString()
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
                                    db.collection("users").document(username)
                                        .update(
                                            "balance",
                                            targetbalance?.plus(nominalInput.text.toString().toInt())
                                        )

                                    if (activity == "koperasi")
                                    {
                                        val date = SimpleDateFormat("MMddyyyy-HH:mm:ss")
                                        val currentDate = date.format(Date())
                                        db.collection("users").document(user)
                                            .collection("transactions").document(currentDate)
                                            .set(
                                                Transactions(
                                                    "Dana Keluar - Transfer Antar Pengguna",
                                                    nominalInput.text.toString().toInt(),
                                                    "out",
                                                    FieldValue.serverTimestamp()
                                                )
                                            )
                                            .addOnSuccessListener {
                                                db.collection("users").document(username)
                                                    .collection("transactions").document(currentDate)
                                                    .set(
                                                        Transactions(
                                                            "Dana Masuk - Transfer Antar Pengguna",
                                                            nominalInput.text.toString().toInt(),
                                                            "in",
                                                            FieldValue.serverTimestamp()
                                                        )
                                                    )
                                                Toast.makeText(
                                                    this@SelectNominalActivity,
                                                    "Transfer sukses!",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                startActivity(
                                                    Intent(
                                                        this@SelectNominalActivity,
                                                        TransactionSuccessActivity::class.java
                                                    ).also {
                                                        it.putExtra("STATUS", "success")
                                                        it.putExtra("ACTIVITY", activity)
                                                        it.putExtra("TARGET_USER", username)
                                                        it.putExtra(
                                                            "AMOUNT",
                                                            nominalInput.text.toString()
                                                        )
                                                    }
                                                )
                                                finish()
                                            }
                                        Toast.makeText(
                                            this@SelectNominalActivity,
                                            "Saldo sukses ditambahkan!",
                                            Toast.LENGTH_SHORT
                                        ).show()
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
                                        db.collection("users").document(user)
                                            .update(
                                                "balance",
                                                userbalance?.minus(nominalInput.text.toString().toInt())
                                            )
                                            .addOnSuccessListener {
                                                val date = SimpleDateFormat("MMddyyyy-HH:mm:ss")
                                                val currentDate = date.format(Date())
                                                db.collection("users").document(user)
                                                    .collection("transactions").document(currentDate)
                                                    .set(
                                                        Transactions(
                                                            "Dana Keluar - Transfer Antar Pengguna",
                                                            nominalInput.text.toString().toInt(),
                                                            "out",
                                                            FieldValue.serverTimestamp()
                                                        )
                                                    )
                                                    .addOnSuccessListener {
                                                        db.collection("users").document(username)
                                                            .collection("transactions").document(currentDate)
                                                            .set(
                                                                Transactions(
                                                                    "Dana Masuk - Transfer Antar Pengguna",
                                                                    nominalInput.text.toString().toInt(),
                                                                    "in",
                                                                    FieldValue.serverTimestamp()
                                                                )
                                                            )
                                                        Toast.makeText(
                                                            this@SelectNominalActivity,
                                                            "Transfer sukses!",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                        startActivity(
                                                            Intent(
                                                                this@SelectNominalActivity,
                                                                TransactionSuccessActivity::class.java
                                                            ).also {
                                                                it.putExtra("STATUS", "success")
                                                                it.putExtra("ACTIVITY", activity)
                                                                it.putExtra("TARGET_USER", username)
                                                                it.putExtra(
                                                                    "AMOUNT",
                                                                    nominalInput.text.toString()
                                                                )
                                                            }
                                                        )
                                                        finish()
                                                    }
                                            }
                                            .addOnFailureListener {
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