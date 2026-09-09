package com.hermeticvm.linkahest.data.models

import com.hermeticvm.linkahest.ui.screens.isRedirectingInstance
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DefaultInstancesTest {
    @Test
    fun nitterInstancesUseRedirectorAsDefault() {
        assertEquals(
            listOf(
                "xxcancel.com",
                "nitter.click",
                "nitter.meowing.monster",
                "nitter.xitter.cc"
            ),
            DefaultInstances.NITTER_INSTANCES
        )
    }

    @Test
    fun identifiesCurrentNitterRedirector() {
        assertTrue(isRedirectingInstance("https://xxcancel.com/"))
        assertFalse(isRedirectingInstance("twiiit.com"))
    }
}
