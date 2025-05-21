package pro.danyloplaksyvyi.nytimesapp.features.main.data.repository.overview

import pro.danyloplaksyvyi.nytimesapp.features.main.domain.model.overview.OverviewResponse

interface OverviewRepository {
    suspend fun getOverview(publishedDate: String): OverviewResponse
    suspend fun fetchAndCacheOverview(publishedDate: String): OverviewResponse
}