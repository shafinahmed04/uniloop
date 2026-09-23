package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entity.BadgeEntity
import com.example.data.local.entity.NotificationEntity
import com.example.data.local.entity.ReferralEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {
    @Query("SELECT * FROM notifications WHERE userId = :userId ORDER BY timestamp DESC")
    fun getNotificationsForUser(userId: Long): Flow<List<NotificationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: NotificationEntity)

    @Query("UPDATE notifications SET isRead = 1 WHERE userId = :userId")
    suspend fun markAllAsRead(userId: Long)

    @Query("SELECT COUNT(*) FROM notifications WHERE userId = :userId AND isRead = 0")
    fun getUnreadCount(userId: Long): Flow<Int>
}

@Dao
interface ReferralDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReferral(referral: ReferralEntity): Long

    @Query("SELECT * FROM referrals WHERE referrerUserId = :userId ORDER BY createdAt DESC")
    fun getReferralsByReferrer(userId: Long): Flow<List<ReferralEntity>>

    @Query("SELECT COUNT(*) FROM referrals WHERE referrerUserId = :userId AND status = 'verified'")
    fun getVerifiedReferralCount(userId: Long): Flow<Int>

    @Query("SELECT * FROM badges ORDER BY requiredReferrals ASC")
    fun getAllBadges(): Flow<List<BadgeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBadges(badges: List<BadgeEntity>)
}
