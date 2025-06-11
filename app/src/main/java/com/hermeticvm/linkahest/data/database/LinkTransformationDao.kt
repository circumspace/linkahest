package com.hermeticvm.linkahest.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Delete
import com.hermeticvm.linkahest.data.models.LinkTransformation
import kotlinx.coroutines.flow.Flow

@Dao
interface LinkTransformationDao {
    
    @Query("SELECT * FROM link_transformations ORDER BY timestamp DESC")
    fun getAllTransformations(): Flow<List<LinkTransformation>>
    
    @Query("SELECT * FROM link_transformations WHERE transformationType = :type ORDER BY timestamp DESC")
    fun getTransformationsByType(type: String): Flow<List<LinkTransformation>>
    
    @Insert
    suspend fun insertTransformation(transformation: LinkTransformation)
    
    @Delete
    suspend fun deleteTransformation(transformation: LinkTransformation)
    
    @Query("DELETE FROM link_transformations")
    suspend fun deleteAllTransformations()
    
    @Query("SELECT COUNT(*) FROM link_transformations")
    suspend fun getTransformationCount(): Int
}