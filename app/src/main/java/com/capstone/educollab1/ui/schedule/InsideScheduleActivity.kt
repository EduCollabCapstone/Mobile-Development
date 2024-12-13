package com.capstone.educollab1.ui.schedule

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.educollab1.databinding.ActivityInsideScheduleBinding
import com.capstone.educollab1.ui.remote.ApiConfig
import com.capstone.educollab1.ui.remote.ScheduleResponse
import com.capstone.educollab1.ui.utils.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class InsideScheduleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInsideScheduleBinding
    private lateinit var insideScheduleAdapter: InsideScheduleAdapter
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInsideScheduleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)
        val username = sessionManager.getUsername()
        val token = sessionManager.getToken()

        if (username.isNullOrEmpty() || token.isNullOrEmpty()) {
            Toast.makeText(this, "Anda belum login, silakan login kembali", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        setupSpinner()
        setupRecyclerView()

        // Handle spinner item selection
        binding.daySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View?, position: Int, id: Long) {
                val selectedDay = parentView.selectedItem?.toString()
                if (selectedDay != null) {
                    Log.d("InsideScheduleActivity", "Selected day: $selectedDay")
                    loadSchedulesForDay(username, selectedDay)
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {}
        }

        // Load schedules for user on activity start
        loadSchedulesForUser(username)

        // Handle add schedule button click
        binding.addScheduleButton.setOnClickListener {
            val intent = Intent(this, EditScheduleActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupSpinner() {
        val days = listOf("Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, days)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.daySpinner.adapter = adapter
    }

    private fun setupRecyclerView() {
        insideScheduleAdapter = InsideScheduleAdapter(
            onEditClick = { schedule -> editSchedule(schedule) },
            onDeleteClick = { schedule -> deleteSchedule(schedule.scheduleId ?: 0) }  // Handle as Int, not String
        )
        binding.InsideScheduleRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.InsideScheduleRecyclerView.adapter = insideScheduleAdapter
    }

    private fun loadSchedulesForUser(username: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiConfig.apiService.viewScheduleByDay(username, "senin") // Default to "senin"
                Log.d("InsideScheduleActivity", "API Response: ${response.body()}")

                if (response.isSuccessful) {
                    val schedules = response.body() ?: emptyList()
                    withContext(Dispatchers.Main) {
                        insideScheduleAdapter.submitList(schedules)
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@InsideScheduleActivity, "Gagal memuat jadwal", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@InsideScheduleActivity, "Terjadi kesalahan: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun loadSchedulesForDay(username: String, day: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiConfig.apiService.viewScheduleByDay(username, day)
                Log.d("InsideScheduleActivity", "API Response: ${response.body()}")

                if (response.isSuccessful) {
                    val schedules = response.body() ?: emptyList()
                    withContext(Dispatchers.Main) {
                        insideScheduleAdapter.submitList(schedules)
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@InsideScheduleActivity, "Gagal memuat jadwal untuk $day", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@InsideScheduleActivity, "Terjadi kesalahan: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun editSchedule(schedule: ScheduleResponse) {
        val intent = Intent(this, EditScheduleActivity::class.java).apply {
            putExtra("SCHEDULE_ID", schedule.scheduleId)
        }
        startActivity(intent)
    }

    private fun deleteSchedule(scheduleId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response: Response<Void> = ApiConfig.apiService.deleteSchedule(scheduleId)

                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@InsideScheduleActivity, "Jadwal berhasil dihapus", Toast.LENGTH_SHORT).show()
                        loadSchedulesForUser(sessionManager.getUsername() ?: "")
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@InsideScheduleActivity, "Gagal menghapus jadwal", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@InsideScheduleActivity, "Terjadi kesalahan: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
