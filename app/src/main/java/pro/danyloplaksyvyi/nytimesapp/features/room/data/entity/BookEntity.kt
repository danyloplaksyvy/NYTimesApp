package pro.danyloplaksyvyi.nytimesapp.features.room.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "books",
    foreignKeys = [
        ForeignKey(
            entity = ListEntity::class,
            parentColumns = ["listId"],
            childColumns = ["listId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["listId"], name = "index_books_listId")]
)
data class BookEntity(
    @PrimaryKey val bookId: String,
    val listId: Int,
    val listNameEncoded: String,
    val rank: Int,
    val title: String,
    val author: String,
    val publisher: String,
    val description: String?,
    val bookImage: String,
    val amazonProductUrl: String
)   