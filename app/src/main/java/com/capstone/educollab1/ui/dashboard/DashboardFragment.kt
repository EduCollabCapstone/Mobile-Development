package com.capstone.educollab1.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.educollab1.databinding.FragmentDashboardBinding
import com.capstone.educollab1.ui.LoginActivity
import com.capstone.educollab1.ui.remote.ApiConfig
import com.capstone.educollab1.ui.remote.SentimentRequest
import com.capstone.educollab1.ui.remote.SentimentResponse
import com.capstone.educollab1.ui.utils.SessionManager
import com.google.android.material.tabs.TabLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

        setupUI()
        setupActions()

        return binding.root
    }

    private fun setupUI() {
        initializeData()
        setupRecyclerView()
    }

    private fun setupActions() {
        setupEditButton()
        setupTabLayout()
        setupLogoutButton()
        setupSubmitFeedbackButton()
    }

    private fun initializeData() {
        subjectDummy.apply {
            clear()
            addAll(
                listOf(
                    Subject("Matematika", 80),
                    Subject("Bahasa Indonesia", 85),
                    Subject("Bahasa Inggris", 90)
                )
            )
        }
        Log.d("DashboardFragment", "Data Dummy Initialized: $subjectDummy")
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
            adapter.notifyDataSetChanged()
            Toast.makeText(requireContext(), "Semua data berhasil diperbarui.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupTabLayout() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> updateSubjectData(
                        listOf(
                            Subject("Matematika", 80),
                            Subject("Bahasa Indonesia", 85),
                            Subject("Bahasa Inggris", 90)
                        )
                    )
                    1 -> updateSubjectData(
                        listOf(
                            Subject("Fisika", 75),
                            Subject("Kimia", 80)
                        )
                    )
                    2 -> updateSubjectData(
                        listOf(
                            Subject("Biologi", 85),
                            Subject("Geografi", 88)
                        )
                    )
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun updateSubjectData(data: List<Subject>) {
        subjectDummy.apply {
            clear()
            addAll(data)
        }
        adapter.notifyDataSetChanged()
        Log.d("DashboardFragment", "Subject Data Updated: $subjectDummy")
    }

    private fun setupLogoutButton() {
        binding.btnLogout.setOnClickListener {
            SessionManager(requireContext()).logout()
            startActivity(
                Intent(requireContext(), LoginActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
            )
        }
    }

    private fun setupSubmitFeedbackButton() {
        binding.btnSubmitFeedback.setOnClickListener {
            val feedbackText = binding.etFeedback.text.toString().trim()
            if (feedbackText.isBlank()) {
                Toast.makeText(requireContext(), "Feedback tidak boleh kosong!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val sentimentRequest = SentimentRequest(text = feedbackText)
            val apiService = ApiConfig.mlApiService

            Log.d("DashboardFragment", "Request Sent: $sentimentRequest")

            apiService.predictSentiment(sentimentRequest).enqueue(object : Callback<SentimentResponse> {
                override fun onResponse(
                    call: Call<SentimentResponse>,
                    response: Response<SentimentResponse>
                ) {
                    if (response.isSuccessful) {
                        val sentimentResponse = response.body()
                        val sentiment = sentimentResponse?.predictions?.getOrNull(0) ?: "Tidak ada hasil."
                        Log.d("DashboardFragment", "Sentiment: $sentiment")
                        binding.tvNlpResult.text = sentiment
                    } else {
                        Log.e("DashboardFragment", "Response Error: ${response.errorBody()?.string()}")
                        Toast.makeText(requireContext(), "Gagal mendapatkan hasil. Cek respon API.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<SentimentResponse>, t: Throwable) {
                    Log.e("DashboardFragment", "Request Failed: ${t.message}")
                    Toast.makeText(requireContext(), "Terjadi kesalahan: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }


    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
