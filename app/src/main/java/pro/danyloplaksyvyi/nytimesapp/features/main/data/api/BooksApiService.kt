package pro.danyloplaksyvyi.nytimesapp.features.main.data.api

import pro.danyloplaksyvyi.nytimesapp.features.main.domain.model.OverviewResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface BooksApiService {
    @GET("svc/books/v3/lists/overview.json")
    suspend fun getBooksOverview(
        @Query("published_date") publishedDate: String,
        @Query("api-key") apiKey: String
    ): OverviewResponse
}