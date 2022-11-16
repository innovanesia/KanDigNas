package id.innovanesia.kandignas.frontend.activity.features

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import id.innovanesia.kandignas.backend.adapter.TransactionHistoryAdapter
import id.innovanesia.kandignas.databinding.ActivityTransactionHistoryBinding

class TransactionHistoryActivity : AppCompatActivity()
{
    private lateinit var binds: ActivityTransactionHistoryBinding
    /*private lateinit var adapter: TransactionHistoryAdapter*/
    private lateinit var sharedPreference: SharedPreferences
    /*private val keyUser = "key.user_name"*/

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binds = ActivityTransactionHistoryBinding.inflate(layoutInflater)
        setContentView(binds.root)

        sharedPreference = getSharedPreferences("KanDigNas", Context.MODE_PRIVATE)
        /*val username = sharedPreference.getString(keyUser, null)!!
        val activityType = intent.getStringExtra("ACTIVITY")!!*/

        binds.apply {
            setSupportActionBar(toolbar)
            toolbar.setNavigationOnClickListener {
                finish()
            }

            transactionHistoryLoading.visibility = View.VISIBLE

            initRV()

            swipeTransactionHistory.setOnRefreshListener {
                initRV()
            }
        }
    }

    private fun initRV()
    {
        binds.apply {
            /*when (activity)
            {
                "siswa" ->
                {

                }

                "kantin" ->
                {

                }

                "koperasi" ->
                {

                }
            }*/
            swipeTransactionHistory.isRefreshing = false
            transactionHistoryLoading.visibility = View.GONE
        }
    }
}