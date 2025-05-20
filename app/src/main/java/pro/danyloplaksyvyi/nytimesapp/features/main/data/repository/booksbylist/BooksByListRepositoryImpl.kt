package pro.danyloplaksyvyi.nytimesapp.features.main.data.repository.booksbylist

import pro.danyloplaksyvyi.nytimesapp.features.main.data.api.BooksApiService
import pro.danyloplaksyvyi.nytimesapp.features.main.domain.model.booksbylist.ListResponse

class BooksByListRepositoryImpl(
    private val api: BooksApiService,
    private val apiKey: String
) : BooksByListRepository {
    override suspend fun fetchList(date: String, listName: String): ListResponse =
        api.getBooksByList(date, listName, apiKey)
}