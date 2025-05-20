package pro.danyloplaksyvyi.nytimesapp.features.main.data.repository.overview

import pro.danyloplaksyvyi.nytimesapp.features.main.data.api.BooksApiService
import pro.danyloplaksyvyi.nytimesapp.features.main.domain.model.overview.OverviewResponse

class OverviewRepositoryImpl(
    private val api: BooksApiService,
    private val apiKey: String
) : OverviewRepository {

    override suspend fun fetchOverview(publishedDate: String): OverviewResponse {
        return api.getBooksOverview(publishedDate, apiKey)
    }
}