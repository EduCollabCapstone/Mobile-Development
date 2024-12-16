package com.capstone.educollab1.ui.remote

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface MLApiService {

    @POST("predict_performance")
    fun predictPerformance(
        @Body predictionRequest: PredictionRequest
    ): Call<PredictionResponse>

    @POST("/predict_sentiment")
    fun predictSentiment(@Body request: SentimentRequest): Call<SentimentResponse>
}