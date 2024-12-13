package com.capstone.educollab1.ui.utils

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    companion object {
        private const val PREFS_NAME = "user_prefs"
        private const val KEY_USERNAME = "username"
        private const val KEY_TOKEN = "token"
    }

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    // Menyimpan username dan token
    fun saveUserData(username: String, token: String) {
        sharedPreferences.edit().apply {
            putString(KEY_USERNAME, username)  // Menyimpan username
            putString(KEY_TOKEN, token)       // Menyimpan token
            apply()
        }
    }

    // Mendapatkan username
    fun getUsername(): String? {
        return sharedPreferences.getString(KEY_USERNAME, null)
    }

    // Mendapatkan token
    fun getToken(): String? {
        return sharedPreferences.getString(KEY_TOKEN, null)
    }

    // Mengecek apakah pengguna sudah login berdasarkan token
    fun isLoggedIn(): Boolean {
        return !getToken().isNullOrEmpty() // True jika token ada dan tidak kosong
    }

    // Logout dan hapus data username dan token
    fun logout() {
        sharedPreferences.edit().apply {
            remove(KEY_USERNAME)
            remove(KEY_TOKEN)
            apply()
        }
    }

    // Menghapus semua data di SharedPreferences
    fun clearAllData() {
        sharedPreferences.edit().apply {
            clear()
            apply()
        }
    }
}
