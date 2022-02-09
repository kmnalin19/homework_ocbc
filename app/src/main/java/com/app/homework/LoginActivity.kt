package com.app.homework

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.app.homework.ui.LoginFragment
import com.app.homework.util.addFragment
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.app.homework.listners.UiEventInterface
import com.app.homework.ui.SignUpFragment
import com.app.homework.viewModel.LoginViewModel

class LoginActivity : AppCompatActivity(),UiEventInterface {

    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //add page fragment
        initLiveData()
        addFragment(LoginFragment.newInstance())
    }

    override fun setUpUi(view : View) {}

    override fun initLiveData() {
        loginViewModel.isRegister.observe(this, Observer {
            addFragment(SignUpFragment.newInstance())
        })
    }


}