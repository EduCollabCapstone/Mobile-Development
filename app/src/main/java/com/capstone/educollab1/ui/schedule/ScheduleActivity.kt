package com.capstone.educollab1.ui.schedule

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.educollab1.R
import com.capstone.educollab1.local.ScheduleDatabase
import com.capstone.educollab1.local.ScheduleEntity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ScheduleActivity : AppCompatActivity() {

    private lateinit var scheduleDatabase: ScheduleDatabase
    private lateinit var fabAddSchedule: FloatingActionButton

    private lateinit var mondayTextView: TextView
    private lateinit var tuesdayTextView: TextView
    private lateinit var wednesdayTextView: TextView
    private lateinit var thursdayTextView: TextView
    private lateinit var fridayTextView: TextView
    private lateinit var saturdayTextView: TextView
    private lateinit var sundayTextView: TextView

    private lateinit var mondayRecyclerView: RecyclerView
    private lateinit var tuesdayRecyclerView: RecyclerView
    private lateinit var wednesdayRecyclerView: RecyclerView
    private lateinit var thursdayRecyclerView: RecyclerView
    private lateinit var fridayRecyclerView: RecyclerView
    private lateinit var saturdayRecyclerView: RecyclerView
    private lateinit var sundayRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule)

        scheduleDatabase = ScheduleDatabase.getDatabase(this)

        fabAddSchedule = findViewById(R.id.fabAddSchedule)

        mondayTextView = findViewById(R.id.tvMonday)
        tuesdayTextView = findViewById(R.id.tvTuesday)
        wednesdayTextView = findViewById(R.id.tvWednesday)
        thursdayTextView = findViewById(R.id.tvThursday)
        fridayTextView = findViewById(R.id.tvFriday)
        saturdayTextView = findViewById(R.id.tvSaturday)
        sundayTextView = findViewById(R.id.tvSunday)

        mondayRecyclerView = findViewById(R.id.rvMonday)
        tuesdayRecyclerView = findViewById(R.id.rvTuesday)
        wednesdayRecyclerView = findViewById(R.id.rvWednesday)
        thursdayRecyclerView = findViewById(R.id.rvThursday)
        fridayRecyclerView = findViewById(R.id.rvFriday)
        saturdayRecyclerView = findViewById(R.id.rvSaturday)
        sundayRecyclerView = findViewById(R.id.rvSunday)

        fabAddSchedule.setOnClickListener {
            val intent = Intent(this, InsideScheduleActivity::class.java)
            startActivity(intent)
        }

        loadSchedulesForEachDay()
    }

    private fun loadSchedulesForEachDay() {
        val recyclerViews = mapOf(
            "Senin" to Pair(mondayTextView, mondayRecyclerView),
            "Selasa" to Pair(tuesdayTextView, tuesdayRecyclerView),
            "Rabu" to Pair(wednesdayTextView, wednesdayRecyclerView),
            "Kamis" to Pair(thursdayTextView, thursdayRecyclerView),
            "Jumat" to Pair(fridayTextView, fridayRecyclerView),
            "Sabtu" to Pair(saturdayTextView, saturdayRecyclerView),
            "Minggu" to Pair(sundayTextView, sundayRecyclerView)
        )

        recyclerViews.forEach { (day, views) ->
            val textView = views.first
            val recyclerView = views.second

            GlobalScope.launch(Dispatchers.Main) {
                val schedules = scheduleDatabase.scheduleDao().getSchedulesByDay(day)
                recyclerView.layoutManager = LinearLayoutManager(this@ScheduleActivity)
                recyclerView.adapter = ScheduleAdapter(schedules)

                if (schedules.isEmpty()) {
                    textView.visibility = View.GONE
                } else {
                    textView.visibility = View.VISIBLE
                }
            }
        }
    }
}
