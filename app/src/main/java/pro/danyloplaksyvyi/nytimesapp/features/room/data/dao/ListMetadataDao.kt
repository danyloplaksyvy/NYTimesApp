package pro.danyloplaksyvyi.nytimesapp.features.room.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pro.danyloplaksyvyi.nytimesapp.features.room.data.entity.ListMetadata

@Dao
interface ListMetadataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMetadata(metadata: ListMetadata)

    @Query("SELECT * FROM list_metadata WHERE listName = :listName AND publishedDate = :publishedDate")
    suspend fun getMetadataForList(listName: String, publishedDate: String): ListMetadata?
}