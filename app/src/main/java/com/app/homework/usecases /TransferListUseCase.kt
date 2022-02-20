package com.app.homework.usecases

import androidx.lifecycle.MutableLiveData
import com.app.homework.domain.MainRepository
import com.app.homework.domain.Response
import com.app.homework.domain.model.TransferRequestModel
import com.app.homework.listners.CoroutineListener
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class TransferListUseCase(private val mainRepository: MainRepository) : CoroutineScope,CoroutineListener {

    var job: Job? = null
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    fun executeCase(jwtToken : String) : MutableLiveData<Response<Any?>> {
        val data = MutableLiveData<Response<Any?>>()
        job  = launch {

            val response = mainRepository.getTransactions(jwtToken)
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