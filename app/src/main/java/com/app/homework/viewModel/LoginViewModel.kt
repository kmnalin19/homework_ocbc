package com.app.homework.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {


    /**
     * live data for notify after when redirect to Register screen anywhere in the app
     */
    private val _isRegister : MutableLiveData<Boolean> = MutableLiveData()
    val isRegister : LiveData<Boolean>
        get() = _isRegister


    /**
     * live data for notify after when redirect to Login screen anywhere in the app
     */
    private val _isLoginSuccess : MutableLiveData<Boolean> = MutableLiveData()
    val isLoginSuccess : LiveData<Boolean>
        get() = _isLoginSuccess


    fun setRegister(){
        _isRegister.postValue(true)
    }

}