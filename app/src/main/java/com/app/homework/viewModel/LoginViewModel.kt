package com.app.homework.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.homework.domain.ApiService
import com.app.homework.domain.MainRepository
import com.app.homework.domain.model.LoginRequest
import com.app.homework.domain.model.LoginResponse
import com.app.homework.usecases.LoginUseCase
import com.app.homework.usecases.PayeeListUseCase
import com.app.homework.util.CoroutineDispatcherProvider
import kotlinx.coroutines.*

class LoginViewModel() : ViewModel() {

    private val apiService: ApiService = ApiService.getInstance()
    private val mainRepository: MainRepository = MainRepository(apiService)
    private val payeeListUseCase = LoginUseCase(mainRepository, CoroutineDispatcherProvider())
    /**
     * live data for notify after when redirect to Register screen anywhere in the app
     */
    private val _isRegister : MutableLiveData<Boolean> = MutableLiveData()
    val isRegister : LiveData<Boolean>
        get() = _isRegister

    /**
     * live data for notify after when redirect to Login screen anywhere in the app
     */
    private val _isLoginSuccess : MutableLiveData<LoginResponse> = MutableLiveData()
    val isLoginSuccess : LiveData<LoginResponse>
        get() = _isLoginSuccess

    /**
     * live data for notify when Error and handle in UI Fragment or Activity
     */
    private val _isError : MutableLiveData<String> = MutableLiveData()
    val isError : LiveData<String>
        get() = _isError


    /**
     * live data for handle Loading and notify for UI
     */
    private val _isLoading : MutableLiveData<Boolean> = MutableLiveData()
    val isLoading : LiveData<Boolean>
        get() = _isLoading

    /**
     * setRegister will for listen user navigate to Signup
     */
    fun setRegister(){
        _isRegister.postValue(true)
    }

    var job: Job? = null
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }

    /**
     * call login api and handle isSuccessful and error (can move to UseCase from viewmodel if want to extend to clean architecture )
     */
    fun doLogin(userName : String,password : String) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = mainRepository.doLogin(LoginRequest(userName,password))
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    _isLoginSuccess.postValue(response.body())
                    _isLoading.postValue(false)
                } else {
                    _isError.postValue(response.message())
                    _isLoading.postValue(false)
                }
            }
        }

    }

    private fun onError(message: String) {
        _isError.postValue(message)
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}