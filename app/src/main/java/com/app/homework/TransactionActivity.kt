package com.app.homework

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.app.homework.const.ApplicationConst
import com.app.homework.listners.UiEventInterface
import com.app.homework.ui.transfer.TransferFoundFragment
import com.app.homework.ui.transactions.TransactionsFragment
import com.app.homework.util.addFragment
import com.app.homework.viewModel.TransactionsViewModel

class TransactionActivity : AppCompatActivity(),UiEventInterface {

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
        initLiveData()
    }

    override fun setUpUi(view: View) {}

    override fun initLiveData() {
        transactionsViewModel.transferFound.observe(this, Observer {
            addFragment(TransferFoundFragment.newInstance())
        })
    }


    override fun onBackPressed() {
        val fragmentManager = supportFragmentManager

        val fragments = fragmentManager.backStackEntryCount
        if (fragments > 1) {
            super.onBackPressed()
        }
        else
            finish()
    }
}