package com.hermeticvm.linkahest.data.repository

import java.net.HttpURLConnection
import java.net.URL

class InstanceAvailabilityRepository {
    fun isAvailable(instance: String, probePath: String): Boolean {
        val host = instance
            .trim()
            .removePrefix("https://")
            .removePrefix("http://")
            .trimEnd('/')
        if (host.isBlank()) return false

        var connection: HttpURLConnection? = null
        return try {
            connection = URL("https://$host$probePath").openConnection() as HttpURLConnection
            connection.instanceFollowRedirects = true
            connection.connectTimeout = TIMEOUT_MILLIS
            connection.readTimeout = TIMEOUT_MILLIS
            connection.setRequestProperty("User-Agent", "Linkahest instance check")
            connection.responseCode in 200..399
        } catch (_: Exception) {
            false
        } finally {
            connection?.disconnect()
        }
    }

    private companion object {
        const val TIMEOUT_MILLIS = 3_000
    }
}
