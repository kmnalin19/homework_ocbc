package com.app.homework.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.app.homework.R
import com.app.homework.listners.UiEventInterface
import com.app.homework.util.hideKeyboard
import com.app.homework.viewModel.SignUpViewModel

class SignUpFragment : Fragment(), UiEventInterface {

    private val signUpViewModel: SignUpViewModel by viewModels()
    private var progressBar : ProgressBar? = null

    private var userNameEdtText : EditText? = null
    private var passwordEdtText : EditText? = null
    private var confPasswordEdtText : EditText? = null
    private var registerBtn : AppCompatButton? = null

    companion object {
        fun newInstance() = SignUpFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.sign_up_fragment_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUi(view)
        initLiveData()
    }

    override fun setUpUi(view: View) {

        progressBar = view.findViewById(R.id.loading)
        val titleImage : ImageView = view.findViewById(R.id.title_image)
        val titleText : TextView = view.findViewById(R.id.title_text)

         userNameEdtText  = view.findViewById(R.id.username_edtxt)
         passwordEdtText  = view.findViewById(R.id.password_edtxt)
         confPasswordEdtText  = view.findViewById(R.id.conform_password_edtxt)
         registerBtn  = view.findViewById(R.id.register_btn)
        userNameEdtText?.addTextChangedListener(textWatcher)
        passwordEdtText?.addTextChangedListener(textWatcher)
        confPasswordEdtText?.addTextChangedListener(textWatcher)
        registerBtn?.disableButton()
        registerBtn?.setOnClickListener {
            activity?.hideKeyboard(it)

            val userName = userNameEdtText?.text.toString()
            val password = passwordEdtText?.text.toString()

                if (isFormValidated()) {
                    progressBar?.visibility = View.VISIBLE
                    signUpViewModel.doSignUp(userName,password)
                    cleanText()
                }
                else{
                    confPasswordEdtText?.error = getString(R.string.confirm_password_not_match)
                }
        }

        titleText.setText(R.string.sign_up)
        titleImage.visibility = View.VISIBLE
        titleImage.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            if (isFormValidated()){
                registerBtn?.enableButton()
            }
            else
                registerBtn?.disableButton()
        }
    }

    fun isFormValidated() : Boolean{

        val userName = userNameEdtText?.text.toString()
        val password = passwordEdtText?.text.toString()
        val confPassword = confPasswordEdtText?.text.toString()

        if (userName.isNotBlank() && password.isNotBlank() && confPassword.isNotBlank()) {
            return password == confPassword
        }
        return false
    }

    override fun initLiveData() {
        signUpViewModel.isError.observe(viewLifecycleOwner , Observer {
            cleanText()
            progressBar?.visibility = View.VISIBLE
            Toast.makeText(activity,R.string.invalid_username, Toast.LENGTH_LONG).show()
        })
        signUpViewModel.isLoading.observe(viewLifecycleOwner , Observer {
            if (it == false)
                progressBar?.visibility = View.GONE
        })
        signUpViewModel.isSignUpSuccess.observe(viewLifecycleOwner , Observer {
            Toast.makeText(activity,"Please Login", Toast.LENGTH_LONG).show()
            progressBar?.visibility = View.VISIBLE
            activity?.onBackPressed()
        })
    }

    private fun cleanText(){
        userNameEdtText?.text?.clear()
        passwordEdtText?.text?.clear()
        confPasswordEdtText?.text?.clear()
    }
}