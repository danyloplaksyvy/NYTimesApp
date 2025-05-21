package pro.danyloplaksyvyi.nytimesapp.features.room.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pro.danyloplaksyvyi.nytimesapp.features.room.data.entity.ListEntity

@Dao
interface ListDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLists(lists: List<ListEntity>)

    @Query("SELECT * FROM lists WHERE publishedDate = :publishedDate")
    suspend fun getListsForDate(publishedDate: String): List<ListEntity>

    @Query("SELECT * FROM lists WHERE listNameEncoded = :listNameEncoded LIMIT 1")
    suspend fun getListByNameEncoded(listNameEncoded: String): ListEntity?
}