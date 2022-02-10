package com.app.homework.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.app.homework.R
import com.app.homework.listners.UiEventInterface
import com.app.homework.viewModel.SignUpViewModel
import com.app.homework.viewModel.TransactionsViewModel

class TransactionsFragment : Fragment(), UiEventInterface {

    private val transactionsViewModel: TransactionsViewModel by viewModels()
    private var progressBar : ProgressBar? = null
    private lateinit var recyclerView : RecyclerView

    companion object {
        fun newInstance() = TransactionsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.transactions_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUi(view)
        initLiveData()
    }


    override fun setUpUi(view: View) {
        progressBar = view.findViewById(R.id.loading)
        val logoutBtn : TextView = view.findViewById(R.id.logout_btn)
        logoutBtn.setOnClickListener {
            activity?.finish()
        }
    }

    override fun initLiveData() {
        transactionsViewModel.isError.observe(viewLifecycleOwner , Observer {
            Toast.makeText(activity,R.string.invalid_username, Toast.LENGTH_LONG).show()
        })
        transactionsViewModel.isLoading.observe(viewLifecycleOwner , Observer {
            if (it == false)
                progressBar?.visibility = View.GONE
        })
    }

}