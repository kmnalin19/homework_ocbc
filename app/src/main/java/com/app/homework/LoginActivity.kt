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
import android.content.Intent
import com.app.homework.const.ApplicationConst


class LoginActivity : AppCompatActivity(),UiEventInterface {

    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //add Login fragment
        initLiveData()
        addFragment(LoginFragment.newInstance())
    }

    override fun setUpUi(view : View) {}

    override fun initLiveData() {
        loginViewModel.isRegister.observe(this, Observer {
            addFragment(SignUpFragment.newInstance())
        })
        loginViewModel.isLoginSuccess.observe(this , Observer {

            val i = Intent(this, TransactionActivity::class.java)
            i.putExtra(ApplicationConst.TOKEN_STRING, it.token)
            startActivity(i)
        })
    }


}