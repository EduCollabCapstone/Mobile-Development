package com.capstone.educollab1.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.capstone.educollab1.MainActivity
import com.capstone.educollab1.databinding.ActivityLoginBinding
import com.capstone.educollab1.ui.remote.ApiConfig
import com.capstone.educollab1.ui.remote.LoginRequest
import com.capstone.educollab1.ui.remote.LoginResponse
import com.capstone.educollab1.ui.utils.SessionManager
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        if (sessionManager.isLoggedIn()) {
            redirectToMainActivity()
            return
        }

        binding.loginButton.setOnClickListener {
            performLogin()
        }

        binding.registerTextView.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun performLogin() {
        val username = binding.usernameEditText.text?.toString()?.trim() ?: ""
        val password = binding.passwordEditText.text?.toString()?.trim() ?: ""

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val loginRequest = LoginRequest(username, password)

        lifecycleScope.launch {
            try {
                val response = ApiConfig.apiService.loginUser(loginRequest)
                if (response.isSuccessful && response.body() != null) {
                    val loginResponse = response.body()!!

                    val token = loginResponse.token
                    if (token.isNullOrEmpty()) {
                        Toast.makeText(this@LoginActivity, "Token is empty", Toast.LENGTH_SHORT).show()
                        return@launch
                    }

                    sessionManager.saveUserData(username, token)

                    Toast.makeText(this@LoginActivity, "Login success", Toast.LENGTH_SHORT).show()

                    redirectToMainActivity()
                } else {
                    val errorBody = response.errorBody()?.string()
                    Toast.makeText(this@LoginActivity, "Login failed: $errorBody", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@LoginActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun redirectToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
