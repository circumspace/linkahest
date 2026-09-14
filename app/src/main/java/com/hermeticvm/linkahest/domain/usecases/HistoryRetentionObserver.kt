package com.hermeticvm.linkahest.domain.usecases

import com.hermeticvm.linkahest.data.models.UserSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

internal suspend fun observeHistoryRetention(
    settings: Flow<UserSettings>,
    clearHistory: suspend () -> Unit
) {
    settings
        .map { it.historyEnabled }
        .distinctUntilChanged()
        .collect { enabled ->
            if (!enabled) {
                clearHistory()
            }
        }
}
