package com.app.homework

import com.app.homework.util.FormatUtil
import org.junit.Test

import org.junit.Assert.*


class UtilTest {

    @Test
    fun doubleToStringNoDecimalTest() {
        assertEquals("SGD 19,000.45", FormatUtil.doubleToStringNoDecimal(19000.45))
        assertNotEquals("19,000.45", FormatUtil.doubleToStringNoDecimal(19000.45))
        assertEquals("19,000.45", FormatUtil.doubleToStringWithoutCurrency(19000.45))
        assertNotEquals("SGD 19,000.45", FormatUtil.doubleToStringWithoutCurrency(19000.45))
    }
}