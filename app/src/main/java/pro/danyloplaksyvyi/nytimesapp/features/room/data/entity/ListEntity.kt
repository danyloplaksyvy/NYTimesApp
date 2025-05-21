package pro.danyloplaksyvyi.nytimesapp.features.room.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lists")
data class ListEntity(
    @PrimaryKey val listId: Int,
    val listName: String,
    val listNameEncoded: String,
    val publishedDate: String
)