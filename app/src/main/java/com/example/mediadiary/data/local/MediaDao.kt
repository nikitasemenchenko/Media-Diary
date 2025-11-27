package com.example.mediadiary.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.mediadiary.data.remote.model.MediaItem
import com.example.mediadiary.data.remote.model.MovieStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface MediaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: MediaItem)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIgnore(item: MediaItem): Long

    @Update
    suspend fun update(item: MediaItem)

    @Delete
    suspend fun delete(item: MediaItem)

    @Query("SELECT * from media_items WHERE id = :id")
    suspend fun findById(id: Int): MediaItem?

    @Query("SELECT * from media_items ORDER BY addedAt DESC")
    fun getAllItems(): Flow<List<MediaItem>>

    @Query("SELECT * from media_items WHERE watchStatus = :status ORDER BY addedAt DESC")
    fun getItemsByStatus(status: MovieStatus): Flow<List<MediaItem>>

    @Query(
        """
    SELECT 
      COUNT(*) AS total,
      SUM(CASE WHEN watchStatus = 'WATCHED' THEN 1 ELSE 0 END) AS watched,
      SUM(CASE WHEN watchStatus = 'WATCHING' THEN 1 ELSE 0 END) AS watching,
      SUM(CASE WHEN watchStatus = 'WANT_TO_WATCH' THEN 1 ELSE 0 END) AS wantToWatch
    FROM media_items
"""
    )
    fun getCollectionStats(): Flow<MediaStats>


    @Query("SELECT type, COUNT(*) as count FROM media_items GROUP BY type ORDER BY count DESC")
    fun getTypes(): Flow<List<TypeCount>>

    @Query("DELETE FROM media_items WHERE id IN (:ids)")
    suspend fun deleteByIds(ids: List<Int>)
}

data class MediaStats(
    val total: Int,
    val watched: Int,
    val watching: Int,
    val wantToWatch: Int
)

data class TypeCount(
    val type: String,
    val count: Int
)
