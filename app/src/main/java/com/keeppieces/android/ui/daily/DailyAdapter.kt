package com.keeppieces.android.ui.daily

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.keeppieces.android.R
import com.keeppieces.android.extension.inflate
import com.keeppieces.android.logic.data.Bill


class DailyAdapter(private val bills: List<Bill>) : RecyclerView.Adapter<DailyAdapter.DailyItemViewHolder>() {
    override fun getItemCount(): Int = bills.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyItemViewHolder {

        return DailyItemViewHolder(parent.inflate(R.layout.item_detail))
    }

    override fun onBindViewHolder(holder: DailyItemViewHolder, position: Int) {
        holder.bind(bills[position])
    }

    class DailyItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val billSecondary: TextView = view.findViewById(R.id.billType)
        private val billAmount: TextView = view.findViewById(R.id.billAmount)
        private val billDate: TextView = view.findViewById(R.id.billDate)
        private val billAccount: TextView = view.findViewById(R.id.billAccount)
        private  val billType: TextView = view.findViewById(R.id.billType)

        fun bind(model: Bill) {
            billSecondary.text = model.secondaryCategory
            billAmount.text = model.amount.toString()
            billDate.text = model.date
            billAccount.text = model.account
            billType.text = "￥"
        }
    }
}