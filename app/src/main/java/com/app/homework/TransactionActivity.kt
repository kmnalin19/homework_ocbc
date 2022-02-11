package com.app.homework

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.app.homework.const.ApplicationConst
import com.app.homework.ui.TransferMoneyFragment
import com.app.homework.ui.transactions.TransactionsFragment
import com.app.homework.util.addFragment
import com.app.homework.viewModel.TransactionsViewModel

class TransactionActivity : AppCompatActivity() {

    private val transactionsViewModel: TransactionsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction)
        val extras = intent.extras
        if (extras != null) {
            val token = extras.getString(ApplicationConst.TOKEN_STRING).toString()
            val name = extras.getString(ApplicationConst.HOLDER_NAME).toString()
            transactionsViewModel.setJwtToken(token)
            transactionsViewModel.setAccountHolderName(name)
        }
        addFragment(TransactionsFragment.newInstance())
    }

    override fun onBackPressed() {

    }
}