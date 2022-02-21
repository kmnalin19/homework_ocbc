package com.app.homework

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.app.homework.domain.MainRepository
import com.app.homework.domain.model.PayeeResponseModel
import com.app.homework.usecases.PayeeListUseCase
import com.app.homework.util.FormatUtil
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

class PayeeListViewModelTest {

    @get:Rule
    val instantTaskRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    val testCoroutineDispatcher = TestCoroutineDispatcher()

    val mainRepository = mockk<MainRepository>()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutinRuleTest = CoroutineRuleTest()

    @ExperimentalCoroutinesApi
    @Test
    fun `use case succeeds with data`() = mainCoroutinRuleTest.runBlockingTest {
        val payeeListUseCase = PayeeListUseCase(mainRepository,getTestCoroutineDispatcher())

        val slot : Response<PayeeResponseModel> = mockk()
        coEvery {
            mainRepository.getPayeeList(
                any()
            )
        } returns slot

        testCoroutineDispatcher.pauseDispatcher()
        val result = payeeListUseCase.executeCase("")
        testCoroutineDispatcher.resumeDispatcher()
        Assert.assertNotNull(result.value)
    }


    @ExperimentalCoroutinesApi
    @After
    fun tearDownAndClear(){
        testCoroutineDispatcher.cleanupTestCoroutines()
        clearAllMocks()
    }

    @Test
    fun doubleToStringNoDecimalTest() {
        Assert.assertEquals("SGD 19,000.45", FormatUtil.doubleToStringNoDecimal(19000.45))
        Assert.assertNotEquals("19,000.45", FormatUtil.doubleToStringNoDecimal(19000.45))
        Assert.assertEquals("19,000.45", FormatUtil.doubleToStringWithoutCurrency(19000.45))
        Assert.assertNotEquals("SGD 19,000.45", FormatUtil.doubleToStringWithoutCurrency(19000.45))
    }
}
