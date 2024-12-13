package com.capstone.educollab1.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.capstone.educollab1.databinding.FragmentHomeBinding
import com.capstone.educollab1.ui.ScheduleActivity
import com.capstone.educollab1.ui.utils.SessionManager

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        sessionManager = SessionManager(requireContext())

        val username = sessionManager.getUsername()
        binding.tvUsername.text = username ?: "Nama Pengguna"

        // Set up onClickListener for "Lihat Semua" TextView to open ScheduleActivity
        binding.tvSeeAll.setOnClickListener {
            val intent = Intent(requireContext(), ScheduleActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

