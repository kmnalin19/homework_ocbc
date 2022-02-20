package com.app.homework.usecases

import androidx.lifecycle.MutableLiveData
import com.app.homework.domain.MainRepository
import com.app.homework.domain.Response
import com.app.homework.domain.model.LoginRequest
import com.app.homework.listners.CoroutineListener
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class PayeeListUseCase (private val mainRepository: MainRepository) : CoroutineScope,
    CoroutineListener {

    var job: Job? = null
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    /**
     * call account detail api and handle isSuccessful and error
     */
    fun executeCase(token : String) : MutableLiveData<Response<Any?>> {
        val data = MutableLiveData<Response<Any?>>()
        job  = launch {

            val response = mainRepository.getPayeeList(token)
            val dataValue = when {
                response.isSuccessful -> {
                    Response.SuccessResponse(response.body())
                }
                else -> {
                    Response.ErrorResponse(response.message())
                }
            }
            data.postValue(dataValue)
        }
        return data
    }

    override fun cancel() {
        if (coroutineContext.isActive)
            job?.cancel()
    }

}