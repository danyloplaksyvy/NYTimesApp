package pro.danyloplaksyvyi.nytimesapp.features.main.domain.model.booksbylist

import pro.danyloplaksyvyi.nytimesapp.features.main.domain.model.overview.Book

data class ListResponse(
    val status: String,
    val num_results: Int,
    val results: ListResults
)

data class ListResults(
    val list_name: String,
    val books: List<Book>,
)