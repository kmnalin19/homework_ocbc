package com.app.homework.ui.transfer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.app.homework.R
import com.app.homework.listners.UiEventInterface
import com.app.homework.viewModel.TransactionsViewModel

import com.app.homework.ui.custom.ThousandSeparatorTextWatcher
import com.app.homework.ui.disableButton
import com.app.homework.ui.enableButton
import com.app.homework.ui.model.PayeeUiModel
import com.app.homework.viewModel.FundTransferViewModel


class TransferFoundFragment : Fragment(), UiEventInterface,ThousandSeparatorTextWatcher.AmountFormatTextWatcherListener {

    private var progressBar : ProgressBar? = null
    private val transactionsViewModel: TransactionsViewModel by activityViewModels()
    private val fundTransferViewModel: FundTransferViewModel by viewModels()

    private lateinit var ui: ArrayAdapter<PayeeUiModel>

    private lateinit var amountEdtText : EditText
    private lateinit var  descEdtText : EditText
    private lateinit var  payeeSpinner : Spinner
    private lateinit var registerBtn : AppCompatButton

    companion object {
        fun newInstance() = TransferFoundFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.transfer_money_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUi(view)
        initLiveData()
        fundTransferViewModel.setJwtToken(transactionsViewModel.getJwtToken())
        progressBar?.visibility = View.VISIBLE
        fundTransferViewModel.getPayeeList()
    }

    override fun setUpUi(view: View) {

        progressBar = view.findViewById(R.id.loading)
        val titleImage : ImageView = view.findViewById(R.id.title_image)
        val titleText : TextView = view.findViewById(R.id.title_text)
        payeeSpinner  = view.findViewById(R.id.found_transfer_payee_list_spinner)
        registerBtn = view.findViewById(R.id.register_btn)

        registerBtn.disableButton()
        amountEdtText = view.findViewById(R.id.found_transfer_amount_edtxt)
        descEdtText = view.findViewById(R.id.found_transfer_desc_edtxt)
        amountEdtText.addTextChangedListener(ThousandSeparatorTextWatcher(amountEdtText,transactionsViewModel.accountBalance(),this))

        titleText.setText(R.string.transfer_title)
        titleImage.visibility = View.VISIBLE
        titleImage.setOnClickListener {
            activity?.onBackPressed()
        }
        registerBtn.setOnClickListener {
            progressBar?.visibility = View.VISIBLE
            fundTransferViewModel.setTransferRequestModel(
                amountEdtText.text.toString(),
                descEdtText.text.toString())
        }
        payeeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                val selectedObject = payeeSpinner.selectedItem as PayeeUiModel
                fundTransferViewModel.setPayeeUiModel(selectedObject)
            }
        }
    }

    override fun initLiveData() {

        fundTransferViewModel.isPayeeListSuccess.observe(viewLifecycleOwner, Observer {
            context?.let {it1 ->
                progressBar?.visibility = View.GONE
                ui = ArrayAdapter<PayeeUiModel>(it1, android.R.layout.simple_list_item_1, it)
                payeeSpinner.adapter = ui
            }
        })
        fundTransferViewModel.isFoundTransferSuccess.observe(viewLifecycleOwner, Observer {
            progressBar?.visibility = View.GONE
            activity?.onBackPressed()
        })

        fundTransferViewModel.isLoading.observe(viewLifecycleOwner , Observer {
            if (it == false)
                progressBar?.visibility = View.GONE
        })
    }

    override fun showInputError() {
        registerBtn.disableButton()
        amountEdtText.error = getString(R.string.amount_insufficient_error)
    }

    override fun showInvalidInputError() {
        registerBtn.disableButton()
        Toast.makeText(activity,getString(R.string.amount_validation_error),Toast.LENGTH_LONG).show()
        amountEdtText.setText("")
        amountEdtText.hint = "0.00"
    }

    override fun showTransferButton() {
        registerBtn.enableButton()
    }
}