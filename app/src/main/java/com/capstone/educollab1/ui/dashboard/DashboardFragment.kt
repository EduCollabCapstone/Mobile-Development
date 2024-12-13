package com.capstone.educollab1.ui.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.educollab1.databinding.FragmentDashboardBinding
import com.google.android.material.tabs.TabLayout

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: DashboardAdapter
    private val subjectDummy = mutableListOf<Subject>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)

        initializeData() // Inisialisasi data dummy awal
        setupRecyclerView()
        setupEditButton()
        setupTabLayout()

        return binding.root
    }

    private fun initializeData() {
        subjectDummy.clear()
        subjectDummy.addAll(
            listOf(
                Subject("Matematika", 80),
                Subject("Bahasa Indonesia", 85),
                Subject("Bahasa Inggris", 90)
            )
        )
        Log.d("DashboardFragment", "Data Dummy: $subjectDummy")
    }

    private fun setupRecyclerView() {
        adapter = DashboardAdapter(subjectDummy) { item, newValue ->
            item.score = newValue
            Log.d("DashboardFragment", "Updated ${item.name} Score to $newValue")
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }

    private fun setupEditButton() {
        binding.ivEditAll.setOnClickListener {
            adapter.notifyDataSetChanged() // Refresh data
        }
    }

    private fun setupTabLayout() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        subjectDummy.clear()
                        subjectDummy.addAll(
                            listOf(
                                Subject("Matematika", 80),
                                Subject("Bahasa Indonesia", 85),
                                Subject("Bahasa Inggris", 90)
                            )
                        )
                    }
                    1 -> {
                        subjectDummy.clear()
                        subjectDummy.addAll(
                            listOf(
                                Subject("Fisika", 75),
                                Subject("Kimia", 80)
                            )
                        )
                    }
                    2 -> {
                        subjectDummy.clear()
                        subjectDummy.addAll(
                            listOf(
                                Subject("Biologi", 85),
                                Subject("Geografi", 88)
                            )
                        )
                    }
                }
                adapter.notifyDataSetChanged() // Perbarui RecyclerView saat tab berubah
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
