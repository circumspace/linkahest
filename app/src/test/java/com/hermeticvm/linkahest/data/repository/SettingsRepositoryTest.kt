package com.hermeticvm.linkahest.data.repository

import org.junit.Assert.assertEquals
import org.junit.Test

class SettingsRepositoryTest {
    @Test
    fun migratesRemovedBuiltInNitterInstancesToCurrentDefault() {
        listOf(
            null,
            "farside.link/nitter",
            "twiiit.com",
            "nitter.tiekoetter.com",
            "nitter.privacyredirect.com",
            "nitter.net",
            "nitter.space"
        ).forEach { instance ->
            assertEquals("xxcancel.com", migrateNitterInstance(instance))
        }
    }

    @Test
    fun preservesOtherNitterInstances() {
        assertEquals("nitter.example.com", migrateNitterInstance("nitter.example.com"))
    }
}
