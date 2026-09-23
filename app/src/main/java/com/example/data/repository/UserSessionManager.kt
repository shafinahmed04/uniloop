package com.example.data.repository

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserSessionManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("uniloop_session", Context.MODE_PRIVATE)

    private val _currentUserId = MutableStateFlow<Long?>(getCurrentUserIdFromPrefs())
    val currentUserId: StateFlow<Long?> = _currentUserId.asStateFlow()

    private fun getCurrentUserIdFromPrefs(): Long? {
        val id = prefs.getLong(KEY_USER_ID, -1L)
        return if (id != -1L) id else null
    }

    fun setSession(userId: Long) {
        prefs.edit().putLong(KEY_USER_ID, userId).apply()
        _currentUserId.value = userId
    }

    fun clearSession() {
        prefs.edit().remove(KEY_USER_ID).apply()
        _currentUserId.value = null
    }

    fun isLoggedIn(): Boolean = _currentUserId.value != null

    companion object {
        private const val KEY_USER_ID = "current_user_id"
    }
}
