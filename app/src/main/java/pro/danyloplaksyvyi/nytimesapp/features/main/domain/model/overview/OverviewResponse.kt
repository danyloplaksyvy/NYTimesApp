package pro.danyloplaksyvyi.nytimesapp.features.main.domain.model.overview

data class OverviewResponse(
    val status: String,
    val copyright: String,
    val num_results: Int,
    val results: Results
)

data class Results(
    val bestsellers_date: String,
    val previous_published_date: String,
    val published_date: String,
    val next_published_date: String,
    val lists: List<BookList>
)

data class BookList(
    val list_id: Int,
    val list_name: String,
    val list_name_encoded: String,
    val display_name: String,
    val updated: String,
    val books: List<Book>,
    val created_date: String? = null,
)

data class Book(
    val rank: Int,
    val rank_last_week: Int,
    val weeks_on_list: Int,
    val asterisk: Int,
    val dagger: Int,
    val primary_isbn13: String,
    val publisher: String,
    val description: String? = null,
    val title: String,
    val author: String,
    val contributor: String,
    val contributor_note: String,
    val book_image: String,
    val amazon_product_url: String,
    val age_group: String,
    val book_review_link: String,
    val sunday_review_link: String
)