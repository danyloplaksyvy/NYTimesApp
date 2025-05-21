package pro.danyloplaksyvyi.nytimesapp.features.room.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pro.danyloplaksyvyi.nytimesapp.features.room.data.entity.OverviewMetadata

@Dao
interface OverviewMetadataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMetadata(metadata: OverviewMetadata)

    @Query("SELECT * FROM overview_metadata WHERE publishedDate = :publishedDate")
    suspend fun getMetadataForDate(publishedDate: String): OverviewMetadata?
}
