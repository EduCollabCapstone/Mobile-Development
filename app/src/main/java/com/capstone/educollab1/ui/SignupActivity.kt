package com.capstone.educollab1.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.capstone.educollab1.MainActivity
import com.capstone.educollab1.databinding.ActivitySignUpBinding
import com.capstone.educollab1.ui.remote.ApiConfig
import com.capstone.educollab1.ui.remote.UserRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        // Log the request data
        Log.d("SignUp", "Request Data: Username=${userRequest.username}, Email=${userRequest.email}, Password=${userRequest.password}")

        // Using coroutine to call the suspend function
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiConfig.apiService.registerUser(userRequest)

                if (response.isSuccessful && response.body() != null) {
                    val user = response.body()!!

                    // Log success response
                    Log.d("SignUp", "Response Success: $user")

                    // Show success message and redirect to MainActivity
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@SignUpActivity,
                            "Account created successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        redirectToMainActivity()
                    }
                } else {
                    // Handle error response
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = parseErrorMessage(errorBody)

                    // Log error response
                    Log.e("SignUp", "Response Error: Code ${response.code()}, Body: $errorBody")

                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@SignUpActivity, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                // Handle exception
                Log.e("SignUp", "Exception: ${e.message}", e)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@SignUpActivity, "Error: ${e.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    // Parse error message from server response
    private fun parseErrorMessage(errorBody: String?): String {
        return try {
            val jsonObject = JSONObject(errorBody ?: "")
            jsonObject.getString("message") // Adjust this key based on your server response
        } catch (e: Exception) {
            "Registration failed. Please try again."
        }
    }

    // Redirect to MainActivity
    private fun redirectToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
