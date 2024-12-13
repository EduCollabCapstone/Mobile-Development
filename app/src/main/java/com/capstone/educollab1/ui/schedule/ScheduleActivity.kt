package com.capstone.educollab1.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.educollab1.databinding.ActivityScheduleBinding
import com.capstone.educollab1.ui.remote.ApiConfig
import com.capstone.educollab1.ui.remote.ScheduleResponse
import com.capstone.educollab1.ui.schedule.InsideScheduleActivity
import com.capstone.educollab1.ui.schedule.ScheduleAdapter
import com.capstone.educollab1.ui.utils.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ScheduleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScheduleBinding
    private lateinit var scheduleAdapter: ScheduleAdapter
    private val apiService = ApiConfig.apiService
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScheduleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)
        val username = sessionManager.getUsername()

        if (username.isNullOrEmpty()) {
            Toast.makeText(this, "Username tidak ditemukan. Silakan login ulang.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        setupRecyclerView()

        // FAB untuk membuka InsideScheduleActivity
        binding.fabAddSchedule.setOnClickListener {
            val intent = Intent(this, InsideScheduleActivity::class.java)
            startActivity(intent)
        }

        // Load jadwal berdasarkan username
        loadSchedulesByUsername(username)
    }

    private fun setupRecyclerView() {
        binding.scheduleRecyclerView.layoutManager = LinearLayoutManager(this)
        scheduleAdapter = ScheduleAdapter { schedule ->
            // Handle item click (placeholder jika diperlukan aksi tambahan)
            Toast.makeText(this, "Klik pada jadwal: ${schedule.scheduleId}", Toast.LENGTH_SHORT).show()
        }
        binding.scheduleRecyclerView.adapter = scheduleAdapter
    }

    private fun loadSchedulesByUsername(username: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.viewSchedule(username)
                if (response.isSuccessful) {
                    val schedules = response.body() ?: emptyList()
                    withContext(Dispatchers.Main) {
                        scheduleAdapter.submitList(schedules)
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@ScheduleActivity, "Gagal memuat jadwal: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@ScheduleActivity, "Terjadi kesalahan: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
