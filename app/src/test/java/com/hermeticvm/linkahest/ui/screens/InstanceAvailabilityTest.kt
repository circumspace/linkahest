package com.hermeticvm.linkahest.ui.screens

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class InstanceAvailabilityTest {
    @Test
    fun normalizesInstanceInput() {
        assertEquals("example.com/path", normalizeInstance("https://example.com/path/"))
        assertEquals("example.com", normalizeInstance(" http://example.com "))
    }

    @Test
    fun identifiesRedirectingInstances() {
        assertTrue(isRedirectingInstance("farside.link/nitter"))
        assertTrue(isRedirectingInstance("https://twiiit.com"))
        assertTrue(isRedirectingInstance("redirect.invidious.io"))
        assertFalse(isRedirectingInstance("nitter.net"))
    }
}
