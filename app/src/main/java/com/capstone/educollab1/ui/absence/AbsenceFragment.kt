package com.capstone.educollab1.ui.absence

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.educollab1.databinding.FragmentAbsenceBinding
import com.capstone.educollab1.ui.remote.ApiConfig
import com.capstone.educollab1.ui.remote.ResponseAbsences
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AbsenceFragment : Fragment() {

    private var _binding: FragmentAbsenceBinding? = null
    private val binding get() = _binding!!

    private lateinit var rvAbsence: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        ViewModelProvider(this).get(AbsenceViewModel::class.java)

        _binding = FragmentAbsenceBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getAbsence()
        binding.fabAddAbsence.setOnClickListener {
            // Navigasi ke AddAbsenceActivity menggunakan Intent
            val intent = Intent(requireContext(), AddAbsenceActivity::class.java)
            startActivity(intent)
        }

        rvAbsence = binding.rvAbsence
        rvAbsence.setHasFixedSize(true)
        rvAbsence.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun getAbsence() {
        lifecycleScope.launch {
            val client = ApiConfig.apiService.getAbsence()

            client.enqueue(object : Callback<ResponseAbsences> {
                override fun onResponse(
                    call: Call<ResponseAbsences>,
                    response: Response<ResponseAbsences>
                ) {
                    val response = response.body()?.responseAbsences

                    rvAbsence.adapter = AbsenceAdapter(response)
                }

                override fun onFailure(call: Call<ResponseAbsences>, t: Throwable) {
                    Snackbar.make(binding.root, t.message.toString(), Snackbar.LENGTH_SHORT).show()
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
