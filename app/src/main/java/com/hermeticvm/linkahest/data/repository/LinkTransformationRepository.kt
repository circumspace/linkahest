package com.hermeticvm.linkahest.data.repository

import com.hermeticvm.linkahest.data.database.LinkTransformationDao
import com.hermeticvm.linkahest.data.models.LinkTransformation
import kotlinx.coroutines.flow.Flow

class LinkTransformationRepository(
    private val dao: LinkTransformationDao
) {
    
    fun getAllTransformations(): Flow<List<LinkTransformation>> {
        return dao.getAllTransformations()
    }
    
    fun getTransformationsByType(type: String): Flow<List<LinkTransformation>> {
        return dao.getTransformationsByType(type)
    }
    
    suspend fun saveTransformation(transformation: LinkTransformation) {
        dao.insertTransformation(transformation)
    }
    
    suspend fun deleteTransformation(transformation: LinkTransformation) {
        dao.deleteTransformation(transformation)
    }
    
    suspend fun clearAllTransformations() {
        dao.deleteAllTransformations()
    }
    
    suspend fun getTransformationCount(): Int {
        return dao.getTransformationCount()
    }
}