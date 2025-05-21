package pro.danyloplaksyvyi.nytimesapp.features.main.data.repository.booksbylist

import pro.danyloplaksyvyi.nytimesapp.features.main.domain.model.booksbylist.ListResponse

interface BooksByListRepository {
    suspend fun getBooksForList(date: String, listName: String): ListResponse
    suspend fun fetchAndCacheBooksForList(date: String, listName: String): ListResponse
}