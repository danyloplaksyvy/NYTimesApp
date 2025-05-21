package pro.danyloplaksyvyi.nytimesapp.features.room.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pro.danyloplaksyvyi.nytimesapp.features.room.data.entity.BookEntity

@Dao
interface BookDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooks(books: List<BookEntity>)

    @Query("SELECT * FROM books WHERE listId = :listId")
    suspend fun getBooksForList(listId: Int): List<BookEntity>
}