package com.capstone.educollab1.ui.schedule

import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.capstone.educollab1.R
import com.capstone.educollab1.databinding.ActivityEditScheduleBinding
import com.capstone.educollab1.ui.remote.ApiConfig
import com.capstone.educollab1.ui.remote.ScheduleRequest
import com.capstone.educollab1.ui.remote.ScheduleResponse
import com.capstone.educollab1.ui.utils.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class EditScheduleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditScheduleBinding
    private lateinit var sessionManager: SessionManager

    private lateinit var daySpinner: Spinner
    private lateinit var subjectEditText: EditText
    private lateinit var startTimeText: TextView
    private lateinit var endTimeText: TextView

    private var scheduleId: Int? = null  // Change scheduleId to Int
    private var token: String? = null

    private var startHour: Int = 0
    private var startMinute: Int = 0
    private var endHour: Int = 0
    private var endMinute: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditScheduleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)
        token = sessionManager.getToken()

        daySpinner = findViewById(R.id.daySpinner)
        subjectEditText = findViewById(R.id.subjectEditText)
        startTimeText = findViewById(R.id.startTimeText)
        endTimeText = findViewById(R.id.endTimeText)

        // Inisialisasi dropdown untuk hari
        val days = arrayOf("Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, days)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        daySpinner.adapter = adapter

        // Mengecek apakah ada scheduleId untuk edit
        scheduleId = intent.getIntExtra("SCHEDULE_ID", -1)  // Ambil scheduleId sebagai Int
        if (scheduleId != -1) {
            binding.saveButton.text = "Update"
            loadScheduleDetails(scheduleId!!)
        } else {
            binding.saveButton.text = "Tambah"
        }

        // Action untuk tombol Simpan
        binding.saveButton.setOnClickListener {
            val day = daySpinner.selectedItem.toString()
            val subject = subjectEditText.text.toString()
            val period = "${startTimeText.text} - ${endTimeText.text}"

            if (scheduleId != -1) {
                updateSchedule(day, subject, period)
            } else {
                addSchedule(day, subject, period)
            }
        }

        // Time Picker untuk Start Time
        startTimeText.setOnClickListener {
            showTimePicker(true)
        }

        // Time Picker untuk End Time
        endTimeText.setOnClickListener {
            showTimePicker(false)
        }
    }

    private fun showTimePicker(isStartTime: Boolean) {
        val calendar = Calendar.getInstance()

        val timePickerDialog = TimePickerDialog(
            this,
            { _, hourOfDay, minute ->
                if (isStartTime) {
                    startHour = hourOfDay
                    startMinute = minute
                    startTimeText.text = formatTime(hourOfDay, minute)
                } else {
                    endHour = hourOfDay
                    endMinute = minute
                    endTimeText.text = formatTime(hourOfDay, minute)
                }
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        )
        timePickerDialog.show()
    }

    private fun formatTime(hourOfDay: Int, minute: Int): String {
        val hour = if (hourOfDay < 10) "0$hourOfDay" else hourOfDay.toString()
        val min = if (minute < 10) "0$minute" else minute.toString()
        return "$hour:$min"
    }

    private fun loadScheduleDetails(scheduleId: Int) {
        val username = sessionManager.getUsername() ?: return  // Mengambil username dari SessionManager
        val day = "Senin" // Gantilah dengan hari yang sesuai
        lifecycleScope.launch {
            try {
                // Panggil API untuk melihat jadwal berdasarkan username dan hari
                val response = ApiConfig.apiService.viewScheduleByDay(username, day)
                if (response.isSuccessful) {
                    val schedule = response.body()?.firstOrNull()
                    if (schedule != null) {
                        subjectEditText.setText(schedule.subject)
                        val times = schedule.period.split(" - ")
                        startTimeText.text = times[0]
                        endTimeText.text = times[1]
                        val dayIndex = resources.getStringArray(R.array.days).indexOf(schedule.day)
                        daySpinner.setSelection(dayIndex)
                    }
                } else {
                    Toast.makeText(this@EditScheduleActivity, "Gagal memuat data jadwal", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@EditScheduleActivity, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addSchedule(day: String, subject: String, period: String) {
        val username = sessionManager.getUsername() ?: "" // Mengambil username dari SessionManager
        val request = ScheduleRequest(scheduleId = null, username = username, day = day, subject = subject, period = period)

        token?.let {
            lifecycleScope.launch {
                try {
                    val response = ApiConfig.apiService.addSchedule("Bearer $it", request)
                    if (response.isSuccessful) {
                        Toast.makeText(this@EditScheduleActivity, "Jadwal berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@EditScheduleActivity, "Gagal menambah jadwal", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@EditScheduleActivity, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun updateSchedule(day: String, subject: String, period: String) {
        val username = sessionManager.getUsername() ?: "" // Mengambil username dari SessionManager
        val request = ScheduleRequest(scheduleId = scheduleId, username = username, day = day, subject = subject, period = period)

        token?.let {
            lifecycleScope.launch {
                try {
                    // Pastikan scheduleId diubah menjadi String jika API mengharapkannya dalam bentuk String
                    val scheduleIdString = scheduleId?.toString() ?: "0"  // Convert to String if necessary
                    val response = ApiConfig.apiService.updateSchedule(scheduleIdString, "Bearer $it", request)
                    if (response.isSuccessful) {
                        Toast.makeText(this@EditScheduleActivity, "Jadwal berhasil diperbarui", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@EditScheduleActivity, "Gagal memperbarui jadwal", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@EditScheduleActivity, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
