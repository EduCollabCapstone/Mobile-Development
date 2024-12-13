package com.capstone.educollab1.ui.remote

import com.capstone.educollab1.ui.remote.PredictionRequest
import com.capstone.educollab1.ui.remote.PredictionResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface MLApiService {

    @POST("predict_performance")
    fun predictPerformance(
        @Body predictionRequest: PredictionRequest
    ): Call<PredictionResponse>
}
