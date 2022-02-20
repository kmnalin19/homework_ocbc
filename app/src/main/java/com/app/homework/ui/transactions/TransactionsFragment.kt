package com.app.homework.ui.transactions

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.app.homework.R
import com.app.homework.listners.UiEventInterface
import com.app.homework.util.FormatUtil
import com.app.homework.viewModel.TransactionsViewModel
import java.lang.NumberFormatException

class TransactionsFragment : Fragment(), UiEventInterface {

    private val transactionsViewModel: TransactionsViewModel by activityViewModels()
    private var progressBar : ProgressBar? = null
    private var accountHolderNumberText : TextView? = null
    private var accountBalanceText : TextView? = null
    private lateinit var recyclerView : RecyclerView

    companion object {
        fun newInstance() = TransactionsFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.transactions_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUi(view)
        initLiveData()
        getAccountBalance()
        getTransactionList()
    }

    override fun onResume() {
        super.onResume()
        getAccountBalance()
    }

    private fun getAccountBalance(){
        transactionsViewModel.getAccountDetails()
        progressBar?.visibility = View.VISIBLE
    }

    private fun getTransactionList(){
        transactionsViewModel.getTransactions()
        progressBar?.visibility = View.VISIBLE
    }

    override fun setUpUi(view: View) {
        progressBar = view.findViewById(R.id.loading)
        val accountHolderNameText : TextView = view.findViewById(R.id.account_holder_name_text)
        recyclerView = view.findViewById(R.id.collect_details_recycleview)
        accountHolderNumberText = view.findViewById(R.id.account_number_text)
        accountBalanceText = view.findViewById(R.id.amount_you_have_text)


        val logoutBtn : TextView = view.findViewById(R.id.logout_btn)
        val transferBtn : TextView = view.findViewById(R.id.transfer_btn)
        logoutBtn.setOnClickListener {
            activity?.finish()
        }
        transferBtn.setOnClickListener {
            transactionsViewModel.setTransferFound()
        }
        accountHolderNameText.text = transactionsViewModel.getAccountHolderName()
    }

    override fun initLiveData() {
        transactionsViewModel.isAccountBalanceSuccess.observe(viewLifecycleOwner , Observer {
            accountHolderNumberText?.text =  it.accountNo
            try {
                accountBalanceText?.text = FormatUtil.doubleToStringNoDecimal(it.balance.toDouble())
            } catch (e: NumberFormatException) {
                accountBalanceText?.text = "0.00"
            }
        })
        transactionsViewModel.isTransactionSuccess.observe(viewLifecycleOwner , Observer {
            recyclerView.adapter = TransactionListAdapter(it)
        })
        transactionsViewModel.isError.observe(viewLifecycleOwner , Observer {
            //Toast.makeText(activity,"SomeThing went wrong....", Toast.LENGTH_LONG).show()
            progressBar?.visibility = View.GONE
        })
        transactionsViewModel.isLoading.observe(viewLifecycleOwner , Observer {
            if (it == false)
                progressBar?.visibility = View.GONE
        })
    }
}