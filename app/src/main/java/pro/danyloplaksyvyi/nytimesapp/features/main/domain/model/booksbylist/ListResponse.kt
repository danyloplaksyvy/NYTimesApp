package pro.danyloplaksyvyi.nytimesapp.features.main.domain.model.booksbylist

import pro.danyloplaksyvyi.nytimesapp.features.main.domain.model.overview.Book

data class ListResponse(
    val status: String,
    val copyright: String,
    val num_results: Int,
    val results: ListResults
)

data class ListResults(
    val list_name: String,
    val list_name_encoded: String,
    val display_name: String,
    val bestsellers_date: String,
    val previous_published_date: String?,
    val published_date: String,
    val next_published_date: String?,
    val normal_list_ends_at: Int,
    val updated: String,
    val books: List<Book>,
)