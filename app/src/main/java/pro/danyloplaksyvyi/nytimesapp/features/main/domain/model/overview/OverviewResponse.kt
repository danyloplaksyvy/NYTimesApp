package pro.danyloplaksyvyi.nytimesapp.features.main.domain.model.overview

data class OverviewResponse(
    val status: String,
    val num_results: Int,
    val results: Results
)

data class Results(
    val bestsellers_date: String,
    val published_date: String,
    val next_published_date: String,
    val lists: List<BookList>
)

data class BookList(
    val list_id: Int,
    val list_name: String,
    val list_name_encoded: String,
    val books: List<Book>,
)

data class Book(
    val primary_isbn13: String? = null,
    val rank: Int,
    val publisher: String,
    val description: String? = null,
    val title: String,
    val author: String,
    val book_image: String,
    val amazon_product_url: String,
)