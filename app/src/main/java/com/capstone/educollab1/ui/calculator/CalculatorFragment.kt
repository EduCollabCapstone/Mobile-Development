package com.capstone.educollab1.ui.calculator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.educollab1.R
import com.capstone.educollab1.response.CalculatorItem
import com.capstone.educollab1.ui.remote.ApiConfig
import com.capstone.educollab1.ui.remote.PredictionRequest
import com.capstone.educollab1.ui.remote.PredictionResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CalculatorFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CalculatorAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_calculator, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.rv_calculator)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val calculatorItems = listOf(
            CalculatorItem("Waktu Belajar"),
            CalculatorItem("Waktu Tidur"),
            CalculatorItem("Nilai Sebelumnya"),
            CalculatorItem("Banyak Latihan Soal"),
            CalculatorItem("Aktivitas Ektrakurikuler"),
            CalculatorItem("Performa Indeks", isResult = true) // Hasil prediksi
        )

        adapter = CalculatorAdapter(calculatorItems)
        recyclerView.adapter = adapter

        view.findViewById<Button>(R.id.btnPredict).setOnClickListener {
            makePrediction(calculatorItems)
        }
    }

    private fun makePrediction(calculatorItems: List<CalculatorItem>) {
        // Gunakan mlApiService untuk prediksi
        val mlApiService = ApiConfig.mlApiService

        val predictionRequest = PredictionRequest(
            Hours_Studied = calculatorItems[0].value.toFloatOrNull() ?: 0f,
            Sleep_Hours = calculatorItems[1].value.toFloatOrNull() ?: 0f,
            Previous_Scores = calculatorItems[2].value.toFloatOrNull() ?: 0f,
            Sample_Question_Papers_Practiced = calculatorItems[3].value.toFloatOrNull() ?: 0f,
            Extracurricular_Activities = calculatorItems[4].value.ifEmpty { "No" }
        )

        mlApiService.predictPerformance(predictionRequest).enqueue(object : Callback<PredictionResponse> {
            override fun onResponse(
                call: Call<PredictionResponse>,
                response: Response<PredictionResponse>
            ) {
                if (response.isSuccessful) {
                    // Pastikan nilai prediction dikonversi ke Float atau Double
                    val prediction = response.body()?.prediction?.toDoubleOrNull() ?: 0.0
                    val formattedPrediction = String.format("%.2f", prediction) // Format dengan 2 angka di belakang koma
                    calculatorItems[5].value = formattedPrediction
                    adapter.notifyItemChanged(5)
                } else {
                    Toast.makeText(requireContext(), "Prediction failed!", Toast.LENGTH_SHORT).show()
                }
            }


            override fun onFailure(call: Call<PredictionResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
