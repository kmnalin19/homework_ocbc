package com.app.homework

import com.app.homework.util.CoroutineDispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineDispatcher


fun getTestCoroutineDispatcher() : CoroutineDispatcherProvider{
    return TestCoroutineContextProvider()
}

class TestCoroutineContextProvider() : CoroutineDispatcherProvider() {
    override val io: CoroutineDispatcher =  TestCoroutineDispatcher()
    override val main: CoroutineDispatcher = TestCoroutineDispatcher()
}