package com.app.homework.viewModel

import androidx.lifecycle.*
import com.app.homework.domain.ApiService
import com.app.homework.domain.MainRepository
import com.app.homework.domain.Response
import com.app.homework.domain.model.*
import com.app.homework.ui.model.TransactionRecyclerItem
import com.app.homework.usecases.AccountBalanceUseCase
import com.app.homework.usecases.TransferListUseCase
import com.app.homework.util.FoundTransferUtil
import kotlinx.coroutines.*

class TransactionsViewModel : ViewModel() {

    /**
     * keep token in view model, better encrypt and keep in local repo and
     */
    private var jwtToken : String = ""
        fun getJwtToken() = jwtToken

    private var accountHolderName : String = ""

    private var accountBalance : String = ""
                fun accountBalance() = accountBalance

    private val apiService: ApiService = ApiService.getInstance()
    private val mainRepository: MainRepository = MainRepository(apiService)

    private val accountBalanceUseCase = AccountBalanceUseCase(mainRepository)
    private val transactionsUseCase = TransferListUseCase(mainRepository)

    private val _transferFound : MutableLiveData<Boolean> = MutableLiveData()
    val transferFound : LiveData<Boolean>
        get() = _transferFound

    /**
     * live data for notify after when Transaction list api success
     */
    val isTransactionSuccess : LiveData<List<TransactionRecyclerItem>> get() = _isTransactionSuccess
    private val _isTransactionSuccess = MediatorLiveData<List<TransactionRecyclerItem>>()

    /**
     * live data for notify after when Account details list api success
     */
    val isAccountBalanceSuccess : LiveData<BalanceResponseModel> get() = _isAccountBalanceSuccess
    private val _isAccountBalanceSuccess = MediatorLiveData<BalanceResponseModel>()

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

    var job: Job? = null
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }

    private val balanceTrigger = MutableLiveData<Unit>()
    private val balanceEvent  : LiveData<Response<Any?>> = Transformations.switchMap(balanceTrigger){
        accountBalanceUseCase.executeCase(jwtToken)
    }

    private val transactionListTrigger = MutableLiveData<Unit>()
    private val transactionListEvent  : LiveData<Response<Any?>> = Transformations.switchMap(balanceTrigger){
        transactionsUseCase.executeCase(jwtToken)
    }

    init {
        _isAccountBalanceSuccess.addSource(balanceEvent){
            when(it) {
                is Response.SuccessResponse ->{
                    it.response?.let { it1 ->
                        val balanceResponseModel = it1 as BalanceResponseModel
                        accountBalance = balanceResponseModel.balance
                        _isAccountBalanceSuccess.postValue(balanceResponseModel)
                    }
                    _isLoading.postValue(false)
                }
                is Response.ErrorResponse ->{
                    _isError.postValue(it.error)
                    _isLoading.postValue(false)
                }
            }
        }
        _isTransactionSuccess.addSource(transactionListEvent){ it ->
            when(it) {
                is Response.SuccessResponse ->{
                    _isTransactionSuccess.postValue(FoundTransferUtil.getTransactionList((it.response as TransactionResponse).data))
                    _isLoading.postValue(false)
                }
                is Response.ErrorResponse ->{
                    _isError.postValue(it.error)
                    _isLoading.postValue(false)
                }
            }
        }
    }

    /**
     * call account detail api and handle isSuccessful and error (can move to UseCase from viewmodel if want to extend to clean architecture )
     */
    fun getAccountDetails() {
        balanceTrigger.postValue(Unit)
    }

    fun getTransactions(){
        transactionListTrigger.postValue(Unit)
    }

    private fun onError(message: String) {
        _isError.postValue(message)
    }

    fun setTransferFound(){
        _transferFound.postValue(true)
    }

    fun setJwtToken(token : String){
        jwtToken = token
    }


    fun setAccountHolderName(name : String){
        accountHolderName = name
    }

    fun getAccountHolderName() = accountHolderName

    /**
     * clear job and token
     */
    override fun onCleared() {
        super.onCleared()
        job?.cancel()
        jwtToken = ""
        accountBalanceUseCase.cancel()
        transactionsUseCase.cancel()
    }

}