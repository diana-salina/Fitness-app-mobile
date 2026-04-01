package ru.nsu.salina.data.remote

import retrofit2.http.GET
import retrofit2.http.Query
import ru.nsu.salina.data.remote.dto.ContraindicationDto
import ru.nsu.salina.data.remote.dto.DayPlanDto

interface ApiService {
    @GET("contraindications")
    suspend fun getContraindications(): List<ContraindicationDto>

    @GET("training-plan")
    suspend fun getTrainingPlan(
        @Query("contraindication_ids") contraindicationIds: List<Int>
    ): List<DayPlanDto>
}
