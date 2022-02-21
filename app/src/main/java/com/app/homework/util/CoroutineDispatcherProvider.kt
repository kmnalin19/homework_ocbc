package com.app.homework.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

open class CoroutineDispatcherProvider {
    open val io:CoroutineDispatcher by lazy { Dispatchers.IO }
    open val main:CoroutineDispatcher by lazy { Dispatchers.Main }
}