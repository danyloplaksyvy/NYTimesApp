package pro.danyloplaksyvyi.nytimesapp.features.main.data.repository.overview

import pro.danyloplaksyvyi.nytimesapp.features.main.data.api.BooksApiService
import pro.danyloplaksyvyi.nytimesapp.features.main.domain.model.overview.Book
import pro.danyloplaksyvyi.nytimesapp.features.main.domain.model.overview.BookList
import pro.danyloplaksyvyi.nytimesapp.features.main.domain.model.overview.OverviewResponse
import pro.danyloplaksyvyi.nytimesapp.features.main.domain.model.overview.Results
import pro.danyloplaksyvyi.nytimesapp.features.room.data.dao.BookDao
import pro.danyloplaksyvyi.nytimesapp.features.room.data.dao.ListDao
import pro.danyloplaksyvyi.nytimesapp.features.room.data.dao.OverviewMetadataDao
import pro.danyloplaksyvyi.nytimesapp.features.room.data.entity.BookEntity
import pro.danyloplaksyvyi.nytimesapp.features.room.data.entity.ListEntity
import pro.danyloplaksyvyi.nytimesapp.features.room.data.entity.OverviewMetadata

class OverviewRepositoryImpl(
    private val api: BooksApiService,
    private val apiKey: String,
    private val listDao: ListDao,
    private val bookDao: BookDao,
    private val metadataDao: OverviewMetadataDao
) : OverviewRepository {

    override suspend fun getOverview(publishedDate: String): OverviewResponse {
        val metadata = metadataDao.getMetadataForDate(publishedDate)
        val currentTime = System.currentTimeMillis()
        val cacheDuration = 24 * 60 * 60 * 1000 // 24 hours in milliseconds

        if (metadata != null && currentTime - metadata.timestamp < cacheDuration) {
            // Cache is valid, retrieve from database
            val lists = listDao.getListsForDate(publishedDate)
            val books = lists.map { list -> bookDao.getBooksForList(list.listId) }.flatten()

            // Map entities back to OverviewResponse
            return OverviewResponse(
                status = "OK",
                num_results = books.size,
                results = Results(
                    bestsellers_date = publishedDate,
                    published_date = publishedDate,
                    next_published_date = "",
                    lists = lists.map { listEntity ->
                        BookList(
                            list_id = listEntity.listId,
                            list_name = listEntity.listName,
                            list_name_encoded = listEntity.listNameEncoded,
                            books = books.filter { it.listId == listEntity.listId }.map { bookEntity ->
                                Book(
                                    rank = bookEntity.rank,
                                    title = bookEntity.title,
                                    author = bookEntity.author,
                                    publisher = bookEntity.publisher,
                                    description = bookEntity.description,
                                    book_image = bookEntity.bookImage,
                                    amazon_product_url = bookEntity.amazonProductUrl
                                )
                            }
                        )
                    }
                )
            )
        } else {
            // Cache is invalid or missing, fetch from API and cache
            return fetchAndCacheOverview(publishedDate)
        }
    }

    override suspend fun fetchAndCacheOverview(publishedDate: String): OverviewResponse {
        val response = api.getBooksOverview(publishedDate, apiKey)

        // Cache lists
        val lists = response.results.lists.map { list ->
            ListEntity(
                listId = list.list_id,
                listName = list.list_name,
                listNameEncoded = list.list_name_encoded,
                publishedDate = publishedDate
            )
        }
        listDao.insertLists(lists)

        // Cache books
        val books = response.results.lists.flatMap { list ->
            list.books.map { book ->
                BookEntity(
                    bookId = book.primary_isbn13 ?: "${list.list_id}_${book.rank}", // Ensure unique ID
                    listId = list.list_id,
                    rank = book.rank,
                    title = book.title,
                    author = book.author,
                    publisher = book.publisher,
                    description = book.description,
                    bookImage = book.book_image,
                    amazonProductUrl = book.amazon_product_url,
                    listNameEncoded = list.list_name_encoded
                )
            }
        }
        bookDao.insertBooks(books)

        // Cache metadata
        metadataDao.insertMetadata(
            OverviewMetadata(
                publishedDate = publishedDate,
                timestamp = System.currentTimeMillis()
            )
        )

        return response
    }
}