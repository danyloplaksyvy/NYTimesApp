package pro.danyloplaksyvyi.nytimesapp.features.room.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "overview_metadata")
data class OverviewMetadata(
    @PrimaryKey val publishedDate: String,
    val timestamp: Long
)
