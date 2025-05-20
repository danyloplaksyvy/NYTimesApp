package pro.danyloplaksyvyi.nytimesapp.features.main.data.repository.booksbylist

import pro.danyloplaksyvyi.nytimesapp.features.main.domain.model.booksbylist.ListResponse

interface BooksByListRepository {
    suspend fun fetchList(date: String, listName: String): ListResponse
}