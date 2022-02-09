package com.app.homework.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.activityViewModels
import com.app.homework.R
import com.app.homework.listners.UiEventInterface
import com.app.homework.viewModel.LoginViewModel

class LoginFragment : Fragment(),UiEventInterface {

    private val loginViewModel: LoginViewModel by activityViewModels()
    private var userNameEdtText : EditText? = null
    private var passwordEdtText : EditText? = null

    companion object {
        fun newInstance() = LoginFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.login_fragment, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUi(view)
    }

    override fun setUpUi(view : View) {

        val registerBtn : AppCompatButton = view.findViewById(R.id.register_btn)
        val loginBtn : AppCompatButton = view.findViewById(R.id.login_btn)
        userNameEdtText = view.findViewById(R.id.username_edtxt)
        passwordEdtText = view.findViewById(R.id.password_edtxt)

        registerBtn.setOnClickListener {
            loginViewModel.setRegister()
        }
        loginBtn.setOnClickListener {

        }
    }

    override fun initLiveData() {

    }

}