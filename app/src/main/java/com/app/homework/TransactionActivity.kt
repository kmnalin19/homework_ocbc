package com.app.homework

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.app.homework.const.ApplicationConst
import com.app.homework.ui.LoginFragment
import com.app.homework.ui.SignUpFragment
import com.app.homework.ui.TransactionsFragment
import com.app.homework.util.addFragment
import com.app.homework.viewModel.LoginViewModel
import com.app.homework.viewModel.TransactionsViewModel

class TransactionActivity : AppCompatActivity() {

    private val transactionsViewModel: TransactionsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction)
        val extras = intent.extras
        if (extras != null) {
           val token = extras.getString(ApplicationConst.TOKEN_STRING).toString()
            transactionsViewModel.setJwtToken(token)
        }
        addFragment(TransactionsFragment.newInstance())
    }

    override fun onBackPressed() {

    }
}