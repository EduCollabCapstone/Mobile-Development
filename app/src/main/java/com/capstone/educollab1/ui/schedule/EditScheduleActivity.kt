package com.capstone.educollab1.ui.schedule

import android.app.TimePickerDialog
import android.content.Intent
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
import com.capstone.educollab1.local.ScheduleDatabase
import com.capstone.educollab1.local.ScheduleEntity
import com.capstone.educollab1.ui.utils.SessionManager
import kotlinx.coroutines.launch
import java.util.*

class EditScheduleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditScheduleBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var scheduleDatabase: ScheduleDatabase

    private lateinit var daySpinner: Spinner
    private lateinit var subjectEditText: EditText
    private lateinit var startTimeText: TextView
    private lateinit var endTimeText: TextView

    private var scheduleId: Int? = null

    private var startHour: Int = 0
    private var startMinute: Int = 0
    private var endHour: Int = 0
    private var endMinute: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditScheduleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        scheduleDatabase = ScheduleDatabase.getDatabase(this)

        sessionManager = SessionManager(this)

        daySpinner = findViewById(R.id.daySpinner)
        subjectEditText = findViewById(R.id.subjectEditText)
        startTimeText = findViewById(R.id.startTimeText)
        endTimeText = findViewById(R.id.endTimeText)

        val days = arrayOf("Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, days)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        daySpinner.adapter = adapter

        scheduleId = intent.getIntExtra("SCHEDULE_ID", -1)
        if (scheduleId != -1) {
            binding.saveButton.text = "Update"
            loadScheduleDetails(scheduleId!!)
        } else {
            binding.saveButton.text = "Tambah"
        }

        binding.saveButton.setOnClickListener {
            val day = daySpinner.selectedItem.toString()
            val subject = subjectEditText.text.toString()
            val period = "${startTimeText.text} - ${endTimeText.text}"

            if (scheduleId != -1) {
                // Update jadwal di database
                updateSchedule(day, subject, period)
            } else {
                addSchedule(day, subject, period)
            }
        }

        startTimeText.setOnClickListener {
            showTimePicker(true)
        }

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
        lifecycleScope.launch {
            try {
                val schedule = scheduleDatabase.scheduleDao().getScheduleById(scheduleId)
                if (schedule != null) {
                    subjectEditText.setText(schedule.subject)
                    val times = schedule.time.split(" - ")
                    startTimeText.text = times[0]
                    endTimeText.text = times[1]
                    val dayIndex = resources.getStringArray(R.array.days).indexOf(schedule.day)
                    daySpinner.setSelection(dayIndex)
                }
            } catch (e: Exception) {
                Toast.makeText(this@EditScheduleActivity, "Terjadi kesalahan", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun addSchedule(day: String, subject: String, period: String) {
        val scheduleEntity = ScheduleEntity(day = day, subject = subject, time = period)

        lifecycleScope.launch {
            try {
                // Simpan data jadwal ke Room Database
                scheduleDatabase.scheduleDao().insertSchedule(scheduleEntity)
                Toast.makeText(
                    this@EditScheduleActivity,
                    "Jadwal berhasil ditambahkan",
                    Toast.LENGTH_SHORT
                ).show()

                val intent = Intent(this@EditScheduleActivity, InsideScheduleActivity::class.java)
                intent.putExtra("SELECTED_DAY", day)
                startActivity(intent)
                finish()

            } catch (e: Exception) {
                Toast.makeText(
                    this@EditScheduleActivity,
                    "Gagal menambah jadwal",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun updateSchedule(day: String, subject: String, period: String) {
        val scheduleEntity =
            ScheduleEntity(id = scheduleId ?: 0, day = day, subject = subject, time = period)

        lifecycleScope.launch {
            try {
                scheduleDatabase.scheduleDao().updateSchedule(scheduleEntity)
                Toast.makeText(
                    this@EditScheduleActivity,
                    "Jadwal berhasil diperbarui",
                    Toast.LENGTH_SHORT
                ).show()


                val intent = Intent(this@EditScheduleActivity, InsideScheduleActivity::class.java)
                intent.putExtra("SELECTED_DAY", day)
                startActivity(intent)
                finish()

            } catch (e: Exception) {
                Toast.makeText(
                    this@EditScheduleActivity,
                    "Gagal memperbarui jadwal",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
