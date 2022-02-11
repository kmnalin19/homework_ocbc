package com.app.homework.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.app.homework.R
import com.app.homework.listners.UiEventInterface
import com.app.homework.util.hideKeyboard
import com.app.homework.viewModel.LoginViewModel

/**
 * login View extend UiEventInterface for override UI functions
 */
class LoginFragment : Fragment(),UiEventInterface {

    private val loginViewModel: LoginViewModel by activityViewModels()
    private var userNameEdtText : EditText? = null
    private var passwordEdtText : EditText? = null
    private var progressBar : ProgressBar? = null

    companion object {
        fun newInstance() = LoginFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.login_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUi(view)
        initLiveData()
    }

    override fun setUpUi(view : View) {

        val registerBtn : AppCompatButton = view.findViewById(R.id.register_btn)
        val loginBtn : AppCompatButton = view.findViewById(R.id.login_btn)
        userNameEdtText = view.findViewById(R.id.username_edtxt)
        passwordEdtText = view.findViewById(R.id.password_edtxt)
        progressBar = view.findViewById(R.id.loading)

        registerBtn.setOnClickListener {
            activity?.hideKeyboard(it)
            loginViewModel.setRegister()
        }
        loginBtn.setOnClickListener {
            activity?.hideKeyboard(it)
            val userName = userNameEdtText?.text.toString()
            val password = passwordEdtText?.text.toString()

            if (userName.isNotBlank() && password.isNotBlank()) {
                progressBar?.visibility = View.VISIBLE

                userNameEdtText?.text?.clear()
                passwordEdtText?.text?.clear()
                loginViewModel.doLogin(userName, password)
            }
            else{
                if (userName.isBlank())
                    userNameEdtText?.error = getString(R.string.enter_user_name)
                else
                    passwordEdtText?.error = getString(R.string.enter_password)
            }
        }
    }

    override fun initLiveData() {
        /**
         * for error show generic error msg and clear fields
         */
        loginViewModel.isError.observe(viewLifecycleOwner , Observer {
            userNameEdtText?.text?.clear()
            passwordEdtText?.text?.clear()
            Toast.makeText(activity,R.string.invalid_username,Toast.LENGTH_LONG).show()
        })
        /**
         * progressBar hide for false
         */
        loginViewModel.isLoading.observe(viewLifecycleOwner , Observer {
            if (it == false)
                progressBar?.visibility = View.GONE
        })
    }

}