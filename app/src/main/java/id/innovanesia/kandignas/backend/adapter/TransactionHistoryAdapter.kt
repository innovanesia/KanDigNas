package id.innovanesia.kandignas.backend.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import id.innovanesia.kandignas.R
import id.innovanesia.kandignas.backend.models.TransactionHistory
import id.innovanesia.kandignas.databinding.RvItemTransactionHistoryBinding
import java.text.DecimalFormat
import java.text.NumberFormat

class TransactionHistoryAdapter(val context: Context, private val transaction: ArrayList<TransactionHistory>) :
    RecyclerView.Adapter<TransactionHistoryAdapter.ViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.rv_item_transaction_history, parent, false)
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        holder.bind.apply {
            val format: NumberFormat = DecimalFormat("#,###")
            transactionName.text = transaction[position].description
            if (transaction[position].status == "in")
            {
                transactionAmount.setTextColor(ContextCompat.getColor(context, R.color.green))
                transactionAmount.text = "+Rp ${format.format(transaction[position].amount)}"
            }
            else if (transaction[position].status == "out")
            {
                transactionAmount.setTextColor(ContextCompat.getColor(context, R.color.red))
                transactionAmount.text = "-Rp ${format.format(transaction[position].amount)}"
            }
            transactionDate.text = transaction[position].time
        }
    }

    override fun getItemCount(): Int = transaction.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
    {
        val bind = RvItemTransactionHistoryBinding.bind(view)
    }
}