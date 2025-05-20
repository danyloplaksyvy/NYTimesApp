package pro.danyloplaksyvyi.nytimesapp.features.main.data.repository

import pro.danyloplaksyvyi.nytimesapp.features.main.data.api.BooksApiService
import pro.danyloplaksyvyi.nytimesapp.features.main.domain.model.OverviewResponse

class OverviewRepositoryImpl(
    private val api: BooksApiService,
    private val apiKey: String
) : OverviewRepository {

    override suspend fun fetchOverview(date: String): OverviewResponse {
        return api.getBooksOverview(date, apiKey)
    }
}