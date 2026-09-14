package com.hermeticvm.linkahest.domain.usecases

import com.hermeticvm.linkahest.data.models.UserSettings
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class HistoryRetentionObserverTest {

    @Test
    fun clearsHistoryWhenDisabled() = runTest {
        var clearCount = 0

        observeHistoryRetention(flowOf(UserSettings(historyEnabled = false))) {
            clearCount++
        }

        assertEquals(1, clearCount)
    }

    @Test
    fun keepsHistoryWhenEnabled() = runTest {
        var clearCount = 0

        observeHistoryRetention(flowOf(UserSettings(historyEnabled = true))) {
            clearCount++
        }

        assertEquals(0, clearCount)
    }

    @Test
    fun clearsHistoryWhenEnabledIsToggledOff() = runTest {
        var clearCount = 0

        observeHistoryRetention(
            flowOf(
                UserSettings(historyEnabled = true),
                UserSettings(historyEnabled = false)
            )
        ) {
            clearCount++
        }

        assertEquals(1, clearCount)
    }

    @Test
    fun doesNotClearRepeatedlyForDuplicateDisabledEmissions() = runTest {
        var clearCount = 0

        observeHistoryRetention(
            flowOf(
                UserSettings(historyEnabled = false),
                UserSettings(historyEnabled = false),
                UserSettings(historyEnabled = false)
            )
        ) {
            clearCount++
        }

        assertEquals(1, clearCount)
    }

    @Test
    fun clearsAgainAfterHistoryIsReenabledThenDisabled() = runTest {
        var clearCount = 0

        observeHistoryRetention(
            flowOf(
                UserSettings(historyEnabled = false),
                UserSettings(historyEnabled = true),
                UserSettings(historyEnabled = false)
            )
        ) {
            clearCount++
        }

        assertEquals(2, clearCount)
    }
}
