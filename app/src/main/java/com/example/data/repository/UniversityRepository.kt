package com.example.data.repository

import com.example.data.local.dao.UniversityDao
import com.example.data.local.entity.DepartmentEntity
import com.example.data.local.entity.UniversityEntity
import kotlinx.coroutines.flow.Flow

class UniversityRepository(private val universityDao: UniversityDao) {
    val universities: Flow<List<UniversityEntity>> = universityDao.getAllUniversitiesFlow()
    val departments: Flow<List<DepartmentEntity>> = universityDao.getAllDepartmentsFlow()

    fun search(query: String): Flow<List<UniversityEntity>> {
        return universityDao.searchUniversities(query)
    }

    suspend fun getUniversityById(id: Long): UniversityEntity? {
        return universityDao.getUniversityById(id)
    }
}
