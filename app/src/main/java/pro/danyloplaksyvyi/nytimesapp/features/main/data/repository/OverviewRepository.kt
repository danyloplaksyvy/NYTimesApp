package pro.danyloplaksyvyi.nytimesapp.features.main.data.repository

import pro.danyloplaksyvyi.nytimesapp.features.main.domain.model.OverviewResponse

interface OverviewRepository {
    suspend fun fetchOverview(publishedDate: String): OverviewResponse
}