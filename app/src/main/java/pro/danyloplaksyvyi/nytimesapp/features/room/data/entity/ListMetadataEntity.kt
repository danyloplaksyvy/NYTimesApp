package pro.danyloplaksyvyi.nytimesapp.features.room.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "list_metadata")
data class ListMetadata(
    @PrimaryKey val listName: String,
    val publishedDate: String,
    val timestamp: Long
)