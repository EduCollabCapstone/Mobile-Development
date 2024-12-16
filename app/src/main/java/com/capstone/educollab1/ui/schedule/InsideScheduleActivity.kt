package com.capstone.educollab1.ui.schedule

import InsideScheduleAdapter
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.educollab1.databinding.ActivityInsideScheduleBinding
import com.capstone.educollab1.local.ScheduleDao
import com.capstone.educollab1.local.ScheduleDatabase
import com.capstone.educollab1.local.ScheduleEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class InsideScheduleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInsideScheduleBinding
    private lateinit var insideScheduleAdapter: InsideScheduleAdapter
    private lateinit var scheduleDao: ScheduleDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInsideScheduleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = ScheduleDatabase.getDatabase(this)
        scheduleDao = database.scheduleDao()

        setupSpinner()
        setupRecyclerView()

        val selectedDay = intent.getStringExtra("SELECTED_DAY")
        selectedDay?.let {
            val days = listOf("Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu")
            val dayIndex = days.indexOf(it)
            binding.daySpinner.setSelection(dayIndex)
            loadSchedulesForDay(it) // Load jadwal sesuai dengan hari yang dipilih
        }

        binding.daySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedDay = parent.selectedItem.toString()
                loadSchedulesForDay(selectedDay)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        binding.addScheduleButton.setOnClickListener {
            val intent = Intent(this, EditScheduleActivity::class.java)
            startActivity(intent)
        }

        binding.saveScheduleButton.setOnClickListener {
            val intent = Intent(this, ScheduleActivity::class.java)
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
            onDeleteClick = { schedule -> deleteSchedule(schedule.id) }
        )
        binding.InsideScheduleRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.InsideScheduleRecyclerView.adapter = insideScheduleAdapter
    }

    private fun loadSchedulesForDay(day: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val schedules = scheduleDao.getSchedulesByDay(day)
            withContext(Dispatchers.Main) {
                insideScheduleAdapter.submitList(schedules)
            }
        }
    }

    private fun editSchedule(schedule: ScheduleEntity) {
        val intent = Intent(this, EditScheduleActivity::class.java).apply {
            putExtra("SCHEDULE_ID", schedule.id)
        }
        startActivity(intent)
    }

    private fun deleteSchedule(scheduleId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            scheduleDao.deleteScheduleById(scheduleId)
            withContext(Dispatchers.Main) {
                Toast.makeText(this@InsideScheduleActivity, "Jadwal berhasil dihapus", Toast.LENGTH_SHORT).show()
                val selectedDay = binding.daySpinner.selectedItem.toString()
                loadSchedulesForDay(selectedDay)
            }
        }
    }
}

