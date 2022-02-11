package com.app.homework.ui.transactions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.homework.R
import com.app.homework.ui.model.TransactionRecyclerItem
import com.app.homework.util.FormatUtil

class TransactionListAdapter(private val transactionList : List<TransactionRecyclerItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

        private val TYPE_SECTION = 0
        private val TYPE_ROW = 1

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)= when(viewType) {
            TYPE_SECTION -> { ViewHolderTitle(LayoutInflater.from(parent.context).inflate(R.layout.transaction_item_title, parent, false)) }
            else -> { ViewHolderRow(LayoutInflater.from(parent.context).inflate(R.layout.transaction_item_row, parent, false)) }
        }

        override fun getItemCount(): Int {
            return transactionList.size
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

            when(val item = transactionList[holder.adapterPosition]) {
                is TransactionRecyclerItem.TransactionRecyclerTitle -> (holder as ViewHolderTitle).bind(item)
                is TransactionRecyclerItem.TransactionRecyclerRow -> (holder as ViewHolderRow).bind(item)
            }
        }

        override fun getItemViewType(position: Int) = when(transactionList[position]) {
            is TransactionRecyclerItem.TransactionRecyclerTitle -> TYPE_SECTION
            is TransactionRecyclerItem.TransactionRecyclerRow -> TYPE_ROW
        }

        inner class ViewHolderTitle(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val headerTextView: TextView = itemView.findViewById(R.id.transaction_list_title_text)

            fun bind(item : TransactionRecyclerItem.TransactionRecyclerTitle){
                headerTextView.text = item.dateTitle
            }
        }

        inner class ViewHolderRow(itemView: View) : RecyclerView.ViewHolder(itemView)  {
            private val nameText : TextView = itemView.findViewById(R.id.transaction_row_name_text)
            private val amountText : TextView = itemView.findViewById(R.id.transaction_row_amount_text)
            private val accountNumberText : TextView = itemView.findViewById(R.id.transaction_row_account_text)

            fun bind(item : TransactionRecyclerItem.TransactionRecyclerRow){
                nameText.text = item.name
                amountText.text = FormatUtil.doubleToStringNoDecimal(item.amount)
                accountNumberText.text = item.accountNumber
            }
        }
    }