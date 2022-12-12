package id.innovanesia.kandignas.frontend.activity.features

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import id.innovanesia.kandignas.backend.adapter.TransactionHistoryAdapter
import id.innovanesia.kandignas.backend.api.InitAPI
import id.innovanesia.kandignas.backend.models.TransactionHistory
import id.innovanesia.kandignas.backend.response.TransactionResponse
import id.innovanesia.kandignas.databinding.ActivityTransactionHistoryBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TransactionHistoryActivity : AppCompatActivity()
{
    private lateinit var binds: ActivityTransactionHistoryBinding
    private lateinit var adapter: TransactionHistoryAdapter
    private lateinit var sharedPreference: SharedPreferences
    private val keyToken = "key.token"

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binds = ActivityTransactionHistoryBinding.inflate(layoutInflater)
        setContentView(binds.root)

        sharedPreference = getSharedPreferences("KanDigNas", Context.MODE_PRIVATE)
        val token = sharedPreference.getString(keyToken, null)!!

        binds.apply {
            setSupportActionBar(toolbar)
            toolbar.setNavigationOnClickListener {
                finish()
            }

            transactionHistoryLoading.visibility = View.VISIBLE

            initRV(token)

            swipeTransactionHistory.setOnRefreshListener {
                initRV(token)
            }
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true)
        {
            override fun handleOnBackPressed()
            {
                finish()
            }
        })
    }

    private fun initRV(token: String)
    {
        binds.apply {
            InitAPI.api.getTransaction("Bearer $token").enqueue(object : Callback<TransactionResponse>
                {
                    override fun onResponse(
                        call: Call<TransactionResponse>,
                        response: Response<TransactionResponse>
                    )
                    {
                        val result = response.body()?.transactions
                        val data: ArrayList<TransactionHistory> = ArrayList()
                        for (i in 0 until result?.size!!)
                        {
                            data.add(
                                TransactionHistory(
                                    result[i].amount,
                                    result[i].description,
                                    result[i].time,
                                    result[i].status
                                )
                            )
                        }
                        adapter = TransactionHistoryAdapter(this@TransactionHistoryActivity, data)
                        transactionRv.adapter = adapter
                        val layout = LinearLayoutManager(this@TransactionHistoryActivity)
                        layout.reverseLayout = true
                        layout.stackFromEnd = true
                        transactionRv.layoutManager = layout
                        if (adapter.itemCount == 0)
                        {
                            noTransactionIllustration.visibility = View.VISIBLE
                            noTransactionText.visibility = View.VISIBLE
                        }
                        else
                        {
                            noTransactionIllustration.visibility = View.GONE
                            noTransactionText.visibility = View.GONE
                        }
                        transactionHistoryLoading.visibility = View.GONE
                        swipeTransactionHistory.isRefreshing = false
                    }

                    override fun onFailure(call: Call<TransactionResponse>, t: Throwable)
                    {
                        Toast.makeText(
                            this@TransactionHistoryActivity,
                            "Mohon periksa kembali koneksi!",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                        transactionHistoryLoading.visibility = View.GONE
                        swipeTransactionHistory.isRefreshing = false
                    }
                })
        }
    }
}