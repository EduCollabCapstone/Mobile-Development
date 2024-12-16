package com.capstone.educollab1.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.educollab1.databinding.FragmentHomeBinding
import com.capstone.educollab1.ui.schedule.ScheduleActivity
import com.capstone.educollab1.ui.schedule.ScheduleAdapter
import com.capstone.educollab1.local.ScheduleDatabase
import com.capstone.educollab1.local.ScheduleEntity
import com.capstone.educollab1.ui.utils.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var sessionManager: SessionManager
    private lateinit var scheduleDatabase: ScheduleDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        sessionManager = SessionManager(requireContext())
        scheduleDatabase = ScheduleDatabase.getDatabase(requireContext())

        val username = sessionManager.getUsername()
        binding.tvUsername.text = username ?: "Nama Pengguna"

        binding.tvSeeAll.setOnClickListener {
            val intent = Intent(requireContext(), ScheduleActivity::class.java)
            startActivity(intent)
        }

        loadTodaySchedule()

        return binding.root
    }

    private fun loadTodaySchedule() {
        binding?.let { binding ->
            val calendar = Calendar.getInstance()
            val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

            val daysOfWeek = mapOf(
                Calendar.MONDAY to "Senin",
                Calendar.TUESDAY to "Selasa",
                Calendar.WEDNESDAY to "Rabu",
                Calendar.THURSDAY to "Kamis",
                Calendar.FRIDAY to "Jumat",
                Calendar.SATURDAY to "Sabtu",
                Calendar.SUNDAY to "Minggu"
            )

            val today = daysOfWeek[dayOfWeek]

            binding.tvScheduleTitle.text = "Jadwal Hari $today"

            GlobalScope.launch(Dispatchers.Main) {
                val schedules = scheduleDatabase.scheduleDao().getSchedulesByDay(today ?: "")
                if (schedules.isNotEmpty()) {
                    // Menampilkan RecyclerView
                    binding.rvSchedule.visibility = View.VISIBLE
                    binding.textHome.visibility = View.GONE

                    binding.rvSchedule.layoutManager = LinearLayoutManager(requireContext())
                    binding.rvSchedule.adapter = ScheduleAdapter(schedules)
                } else {
                    binding.rvSchedule.visibility = View.GONE
                    binding.textHome.visibility = View.VISIBLE
                    binding.textHome.text = "Tidak ada jadwal untuk hari ini."
                }
            }
        }
    }
}
