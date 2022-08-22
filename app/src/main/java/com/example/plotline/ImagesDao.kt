package com.example.plotline

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ImagesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addImageData(imageData: ImageData)

    @Query("SElect * from images")
    fun getImages(): Flow<List<ImageData>>
}