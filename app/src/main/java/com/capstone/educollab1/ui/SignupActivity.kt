package com.capstone.educollab1.ui

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.capstone.educollab1.MainActivity
import com.capstone.educollab1.databinding.ActivitySignUpBinding
import com.capstone.educollab1.ui.remote.ApiConfig
import com.capstone.educollab1.ui.remote.UserRequest
import com.capstone.educollab1.ui.remote.UserResponse
import com.capstone.educollab1.ui.utils.SessionManager
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        // Cek jika pengguna sudah login, langsung arahkan ke MainActivity
        if (sessionManager.isLoggedIn()) {
            redirectToMainActivity()
            return
        }

        // Handle register button click
        binding.btnRegister.setOnClickListener { registerUser() }

        // Navigate to login screen
        binding.tvLogintext.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun registerUser() {
        val username = binding.etUsername.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        // Validate inputs
        if (username.isEmpty()) {
            Toast.makeText(this, "Username is required", Toast.LENGTH_SHORT).show()
            return
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Valid email is required", Toast.LENGTH_SHORT).show()
            return
        }

        if (password.isEmpty() || password.length < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
            return
        }

        val userRequest = UserRequest(username, password, email)

        // Using coroutine to call the suspend function
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiConfig.apiService.registerUser(userRequest)

                if (response.isSuccessful && response.body() != null) {
                    val user = response.body()!!

                    // Save user data in session
                    withContext(Dispatchers.Main) {
                        sessionManager.saveUserData(user.username, user.token)

                        // Show success message
                        Toast.makeText(
                            this@SignUpActivity,
                            "Account created successfully",
                            Toast.LENGTH_SHORT
                        ).show()

                        // Redirect to MainActivity
                        redirectToMainActivity()
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        val errorBody = response.errorBody()?.string()
                        Toast.makeText(
                            this@SignUpActivity,
                            "Registration failed: $errorBody",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@SignUpActivity, "Error: ${e.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    // Fungsi untuk mengarahkan pengguna ke MainActivity
    private fun redirectToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
