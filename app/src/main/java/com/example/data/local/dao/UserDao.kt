package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.data.local.entity.ProfileEntity
import com.example.data.local.entity.UserEntity
import com.example.data.local.entity.UserWithProfile
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    // ---------------------------------------------------------
    // User Operations
    // ---------------------------------------------------------
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertUser(user: UserEntity): Long

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("SELECT * FROM users WHERE LOWER(email) = LOWER(:email) LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?

    @Query("SELECT * FROM users WHERE LOWER(username) = LOWER(:username) LIMIT 1")
    suspend fun getUserByUsername(username: String): UserEntity?

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    suspend fun getUserById(id: Long): UserEntity?

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    fun getUserByIdFlow(id: Long): Flow<UserEntity?>

    @Query("SELECT * FROM users ORDER BY createdAt DESC")
    fun getAllUsersFlow(): Flow<List<UserEntity>>

    @Query("UPDATE users SET isVerified = :isVerified WHERE id = :userId")
    suspend fun updateUserVerificationStatus(userId: Long, isVerified: Boolean)

    // ---------------------------------------------------------
    // Profile Operations
    // ---------------------------------------------------------
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: ProfileEntity)

    @Update
    suspend fun updateProfile(profile: ProfileEntity)

    @Query("SELECT * FROM profiles WHERE userId = :userId LIMIT 1")
    suspend fun getProfileByUserId(userId: Long): ProfileEntity?

    @Query("SELECT * FROM profiles WHERE userId = :userId LIMIT 1")
    fun getProfileByUserIdFlow(userId: Long): Flow<ProfileEntity?>

    @Query("SELECT * FROM profiles WHERE UPPER(referralCode) = UPPER(:code) LIMIT 1")
    suspend fun getProfileByReferralCode(code: String): ProfileEntity?

    @Query("UPDATE profiles SET referralCount = referralCount + 1 WHERE userId = :userId")
    suspend fun incrementReferralCount(userId: Long)

    @Query("UPDATE profiles SET postsCount = postsCount + 1 WHERE userId = :userId")
    suspend fun incrementPostsCount(userId: Long)

    @Query("UPDATE profiles SET followersCount = followersCount + :delta WHERE userId = :userId")
    suspend fun updateFollowersCount(userId: Long, delta: Int)

    @Query("UPDATE profiles SET followingCount = followingCount + :delta WHERE userId = :userId")
    suspend fun updateFollowingCount(userId: Long, delta: Int)

    @Query("SELECT * FROM profiles ORDER BY referralCount DESC")
    fun getReferralLeaderboardProfiles(): Flow<List<ProfileEntity>>

    @Query("SELECT * FROM profiles WHERE universityId = :universityId ORDER BY referralCount DESC")
    fun getCampusReferralLeaderboard(universityId: Long): Flow<List<ProfileEntity>>

    @Query("SELECT * FROM profiles WHERE universityId = :universityId AND department = :department")
    fun getStudentsByDepartmentFlow(universityId: Long, department: String): Flow<List<ProfileEntity>>

    // ---------------------------------------------------------
    // Relational Queries (User with Profile)
    // ---------------------------------------------------------
    @Transaction
    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    suspend fun getUserWithProfile(id: Long): UserWithProfile?

    @Transaction
    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    fun getUserWithProfileFlow(id: Long): Flow<UserWithProfile?>

    @Transaction
    @Query("SELECT * FROM users WHERE LOWER(username) = LOWER(:username) LIMIT 1")
    suspend fun getUserWithProfileByUsername(username: String): UserWithProfile?

    @Transaction
    @Query("SELECT * FROM users WHERE LOWER(email) = LOWER(:email) LIMIT 1")
    suspend fun getUserWithProfileByEmail(email: String): UserWithProfile?

    @Transaction
    @Query("SELECT * FROM users ORDER BY createdAt DESC")
    fun getAllUsersWithProfileFlow(): Flow<List<UserWithProfile>>
}
