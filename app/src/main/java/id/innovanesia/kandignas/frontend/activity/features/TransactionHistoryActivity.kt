package id.innovanesia.kandignas.frontend.activity.features

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import id.innovanesia.kandignas.backend.adapter.TransactionHistoryAdapter
import id.innovanesia.kandignas.backend.models.TransactionHistory
import id.innovanesia.kandignas.databinding.ActivityTransactionHistoryBinding

class TransactionHistoryActivity : AppCompatActivity()
{
    private lateinit var binds: ActivityTransactionHistoryBinding
    private lateinit var adapter: TransactionHistoryAdapter
    private lateinit var sharedPreference: SharedPreferences
    private val keyUser = "key.user_name"
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binds = ActivityTransactionHistoryBinding.inflate(layoutInflater)
        setContentView(binds.root)

        sharedPreference = getSharedPreferences("KanDigNas", Context.MODE_PRIVATE)
        val username = sharedPreference.getString(keyUser, null)!!
        val activityType = intent.getStringExtra("ACTIVITY")!!

        binds.apply {
            setSupportActionBar(toolbar)
            toolbar.setNavigationOnClickListener {
                onBackPressed()
            }

            transactionHistoryLoading.visibility = View.VISIBLE

            initRV(username, activityType)

            swipeTransactionHistory.setOnRefreshListener {
                initRV(username, activityType)
            }
        }
    }

    private fun initRV(username: String, activity: String)
    {
        binds.apply {
            when (activity)
            {
                "siswa" ->
                {
                    db.collection("users")
                        .document(username).collection("transactions").get()
                        .addOnCompleteListener {
                            if (it.isSuccessful)
                            {
                                if (it.result.documents.size == 0)
                                {
                                    noTransactionIllustration.visibility = View.VISIBLE
                                    noTransactionText.visibility = View.VISIBLE
                                }
                                else
                                {
                                    noTransactionIllustration.visibility = View.GONE
                                    noTransactionText.visibility = View.GONE
                                }
                                val transaction = ArrayList<TransactionHistory>()
                                for (docs in it.result)
                                {
                                    transaction.add(
                                        TransactionHistory(
                                            docs.data["name"].toString(),
                                            docs.data["amount"].toString().toInt(),
                                            docs.data["cash_flow"].toString(),
                                            docs.data["transaction_date"] as Timestamp
                                        )
                                    )
                                }
                                adapter = TransactionHistoryAdapter(
                                    this@TransactionHistoryActivity,
                                    transaction
                                )
                                val layoutManager = LinearLayoutManager(this@TransactionHistoryActivity)
                                layoutManager.reverseLayout = true
                                layoutManager.stackFromEnd = true
                                transactionRv.layoutManager = layoutManager
                                transactionRv.adapter = adapter
                            }
                        }
                }

                "kantin" ->
                {
                    db.collection("kantin")
                        .document(username).collection("transactions").get()
                        .addOnCompleteListener {
                            if (it.isSuccessful)
                            {
                                if (it.result.documents.size == 0)
                                {
                                    noTransactionIllustration.visibility = View.VISIBLE
                                    noTransactionText.visibility = View.VISIBLE
                                }
                                else
                                {
                                    noTransactionIllustration.visibility = View.GONE
                                    noTransactionText.visibility = View.GONE
                                }
                                val transaction = ArrayList<TransactionHistory>()
                                for (docs in it.result)
                                {
                                    transaction.add(
                                        TransactionHistory(
                                            docs.data["name"].toString(),
                                            docs.data["amount"].toString().toInt(),
                                            docs.data["cash_flow"].toString(),
                                            docs.data["transaction_date"] as Timestamp
                                        )
                                    )
                                }
                                adapter = TransactionHistoryAdapter(
                                    this@TransactionHistoryActivity,
                                    transaction
                                )
                                val layoutManager = LinearLayoutManager(this@TransactionHistoryActivity)
                                layoutManager.reverseLayout = true
                                layoutManager.stackFromEnd = true
                                transactionRv.layoutManager = layoutManager
                                transactionRv.adapter = adapter
                            }
                        }
                }

                "koperasi" ->
                {
                    db.collection("koperasi")
                        .document(username).collection("transactions").get()
                        .addOnCompleteListener {
                            if (it.isSuccessful)
                            {
                                if (it.result.documents.size == 0)
                                {
                                    noTransactionIllustration.visibility = View.VISIBLE
                                    noTransactionText.visibility = View.VISIBLE
                                }
                                else
                                {
                                    noTransactionIllustration.visibility = View.GONE
                                    noTransactionText.visibility = View.GONE
                                }
                                val transaction = ArrayList<TransactionHistory>()
                                for (docs in it.result)
                                {
                                    transaction.add(
                                        TransactionHistory(
                                            docs.data["name"].toString(),
                                            docs.data["amount"].toString().toInt(),
                                            docs.data["cash_flow"].toString(),
                                            docs.data["transaction_date"] as Timestamp
                                        )
                                    )
                                }
                                adapter = TransactionHistoryAdapter(
                                    this@TransactionHistoryActivity,
                                    transaction
                                )
                                val layoutManager = LinearLayoutManager(this@TransactionHistoryActivity)
                                layoutManager.reverseLayout = true
                                layoutManager.stackFromEnd = true
                                transactionRv.layoutManager = layoutManager
                                transactionRv.adapter = adapter
                            }
                        }
                }
            }
            swipeTransactionHistory.isRefreshing = false
            transactionHistoryLoading.visibility = View.GONE
        }
    }
}