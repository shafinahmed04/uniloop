package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.DepartmentEntity
import com.example.data.local.entity.UniversityEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UniversityDao {
    // ---------------------------------------------------------
    // University Queries
    // ---------------------------------------------------------
    @Query("SELECT * FROM universities ORDER BY isPopular DESC, studentCount DESC, name ASC")
    fun getAllUniversitiesFlow(): Flow<List<UniversityEntity>>

    @Query("SELECT * FROM universities ORDER BY isPopular DESC, studentCount DESC, name ASC")
    suspend fun getAllUniversitiesList(): List<UniversityEntity>

    @Query("SELECT * FROM universities WHERE id = :id LIMIT 1")
    suspend fun getUniversityById(id: Long): UniversityEntity?

    @Query("SELECT * FROM universities WHERE id = :id LIMIT 1")
    fun getUniversityByIdFlow(id: Long): Flow<UniversityEntity?>

    @Query("SELECT * FROM universities WHERE UPPER(shortName) = UPPER(:shortName) LIMIT 1")
    suspend fun getUniversityByShortName(shortName: String): UniversityEntity?

    @Query("SELECT * FROM universities WHERE name LIKE '%' || :query || '%' OR shortName LIKE '%' || :query || '%' OR location LIKE '%' || :query || '%'")
    fun searchUniversities(query: String): Flow<List<UniversityEntity>>

    @Query("SELECT * FROM universities WHERE type = :type ORDER BY studentCount DESC, name ASC")
    fun getUniversitiesByTypeFlow(type: String): Flow<List<UniversityEntity>>

    @Query("SELECT * FROM universities ORDER BY studentCount DESC LIMIT :limit")
    fun getTopCampusesFlow(limit: Int): Flow<List<UniversityEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUniversities(universities: List<UniversityEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUniversity(university: UniversityEntity): Long

    @Update
    suspend fun updateUniversity(university: UniversityEntity)

    @Query("UPDATE universities SET studentCount = studentCount + 1 WHERE id = :id")
    suspend fun incrementStudentCount(id: Long)

    @Query("UPDATE universities SET postsCount = postsCount + 1 WHERE id = :id")
    suspend fun incrementPostsCount(id: Long)

    @Query("SELECT COUNT(*) FROM universities")
    suspend fun getUniversityCount(): Int

    @Query("SELECT COUNT(*) FROM universities")
    fun getUniversityCountFlow(): Flow<Int>

    // ---------------------------------------------------------
    // Department Queries
    // ---------------------------------------------------------
    @Query("SELECT * FROM departments ORDER BY code ASC")
    fun getAllDepartmentsFlow(): Flow<List<DepartmentEntity>>

    @Query("SELECT * FROM departments ORDER BY code ASC")
    suspend fun getAllDepartmentsList(): List<DepartmentEntity>

    @Query("SELECT * FROM departments WHERE code = :code LIMIT 1")
    suspend fun getDepartmentByCode(code: String): DepartmentEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDepartments(departments: List<DepartmentEntity>)
}
