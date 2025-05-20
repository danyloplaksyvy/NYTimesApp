package pro.danyloplaksyvyi.nytimesapp.features.main.data.api

import pro.danyloplaksyvyi.nytimesapp.features.main.domain.model.booksbylist.ListResponse
import pro.danyloplaksyvyi.nytimesapp.features.main.domain.model.overview.OverviewResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BooksApiService {
    @GET("svc/books/v3/lists/overview.json")
    suspend fun getBooksOverview(
        @Query("published_date") publishedDate: String,
        @Query("api-key") apiKey: String
    ): OverviewResponse

    @GET("svc/books/v3/lists/{date}/{list}.json")
    suspend fun getBooksByList(
        @Path("date") date: String,
        @Path("list") listNameEncoded: String,
        @Query("api-key") apiKey: String
    ): ListResponse
}