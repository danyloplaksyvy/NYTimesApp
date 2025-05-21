package pro.danyloplaksyvyi.nytimesapp.features.main.data.repository.booksbylist

import pro.danyloplaksyvyi.nytimesapp.features.main.data.api.BooksApiService
import pro.danyloplaksyvyi.nytimesapp.features.main.domain.model.booksbylist.ListResponse
import pro.danyloplaksyvyi.nytimesapp.features.main.domain.model.booksbylist.ListResults
import pro.danyloplaksyvyi.nytimesapp.features.main.domain.model.overview.Book
import pro.danyloplaksyvyi.nytimesapp.features.room.data.dao.BookDao
import pro.danyloplaksyvyi.nytimesapp.features.room.data.dao.ListDao
import pro.danyloplaksyvyi.nytimesapp.features.room.data.dao.ListMetadataDao
import pro.danyloplaksyvyi.nytimesapp.features.room.data.entity.BookEntity
import pro.danyloplaksyvyi.nytimesapp.features.room.data.entity.ListMetadata

class BooksByListRepositoryImpl(
    private val api: BooksApiService,
    private val apiKey: String,
    private val bookDao: BookDao,
    private val listDao: ListDao,
    private val listMetadataDao: ListMetadataDao
) : BooksByListRepository {

    override suspend fun getBooksForList(date: String, listName: String): ListResponse {
        val listEntity = listDao.getListByNameEncoded(listName)
            ?: throw IllegalArgumentException("List not found for name: $listName")

        val metadata = listMetadataDao.getMetadataForList(listName, date)
        val currentTime = System.currentTimeMillis()
        val cacheDuration = 24 * 60 * 60 * 1000 // 24 hours in milliseconds

        if (metadata != null && currentTime - metadata.timestamp < cacheDuration) {
            // Cache is valid, retrieve from database
            val books = bookDao.getBooksForList(listId = listEntity.listId)
            return ListResponse(
                status = "OK",
                num_results = books.size,
                results = ListResults(
                    list_name = listName,
                    books = books.map { bookEntity ->
                        Book(
                            rank = bookEntity.rank,
                            title = bookEntity.title,
                            author = bookEntity.author,
                            publisher = bookEntity.publisher,
                            description = bookEntity.description,
                            book_image = bookEntity.bookImage,
                            amazon_product_url = bookEntity.amazonProductUrl,
                            primary_isbn13 = bookEntity.bookId // Use bookId instead
                        )
                    }
                )
            )
        } else {
            // Cache is invalid or missing, fetch and cache
            return fetchAndCacheBooksForList(date, listName)
        }
    }

    override suspend fun fetchAndCacheBooksForList(date: String, listName: String): ListResponse {
        val listEntity = listDao.getListByNameEncoded(listName)
            ?: throw IllegalArgumentException("List not found for name: $listName")

        val response = api.getBooksByList(date, listName, apiKey)

        // Cache books
        val booksToInsert = response.results.books.map { book ->
            BookEntity(
                bookId = book.primary_isbn13 ?: "${book.title}_${book.amazon_product_url}",
                listId = listEntity.listId,
                listNameEncoded = listName,
                rank = book.rank,
                title = book.title,
                author = book.author,
                publisher = book.publisher,
                description = book.description,
                bookImage = book.book_image,
                amazonProductUrl = book.amazon_product_url
            )
        }
        bookDao.insertBooks(booksToInsert)

        // Cache metadata
        listMetadataDao.insertMetadata(
            ListMetadata(
                listName = listName,
                publishedDate = date,
                timestamp = System.currentTimeMillis()
            )
        )

        return response
    }
}