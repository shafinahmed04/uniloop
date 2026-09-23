package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.local.dao.NotificationDao
import com.example.data.local.dao.PostDao
import com.example.data.local.dao.ReferralDao
import com.example.data.local.dao.UniversityDao
import com.example.data.local.dao.UserDao
import com.example.data.local.entity.BadgeEntity
import com.example.data.local.entity.CommentEntity
import com.example.data.local.entity.DepartmentEntity
import com.example.data.local.entity.FollowEntity
import com.example.data.local.entity.NotificationEntity
import com.example.data.local.entity.PostEntity
import com.example.data.local.entity.PostLikeEntity
import com.example.data.local.entity.ProfileEntity
import com.example.data.local.entity.ReferralEntity
import com.example.data.local.entity.ReportEntity
import com.example.data.local.entity.UniversityEntity
import com.example.data.local.entity.UserEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        UserEntity::class,
        ProfileEntity::class,
        UniversityEntity::class,
        DepartmentEntity::class,
        PostEntity::class,
        CommentEntity::class,
        PostLikeEntity::class,
        FollowEntity::class,
        NotificationEntity::class,
        ReferralEntity::class,
        BadgeEntity::class,
        ReportEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class UniLoopDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun universityDao(): UniversityDao
    abstract fun postDao(): PostDao
    abstract fun notificationDao(): NotificationDao
    abstract fun referralDao(): ReferralDao

    companion object {
        @Volatile
        private var INSTANCE: UniLoopDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): UniLoopDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UniLoopDatabase::class.java,
                    "uniloop_database"
                )
                    .addCallback(UniLoopDatabaseCallback(scope))
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class UniLoopDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    populateInitialData(database)
                }
            }
        }

        private suspend fun populateInitialData(database: UniLoopDatabase) {
            val uniDao = database.universityDao()
            val refDao = database.referralDao()
            val userDao = database.userDao()
            val postDao = database.postDao()

            // Pre-seed Universities
            uniDao.insertUniversities(SeedData.universities)

            // Pre-seed Departments
            uniDao.insertDepartments(SeedData.departments)

            // Pre-seed Badges
            refDao.insertBadges(SeedData.badges)

            // Seed a starter campus ambassador / demo account for SMUCT
            val demoUserId = userDao.insertUser(
                UserEntity(
                    id = 1,
                    email = "shafin@smuct.edu.bd",
                    passwordHash = "password123",
                    username = "shafin_smuct",
                    fullName = "Shafin Ahmed",
                    role = "admin",
                    isVerified = true,
                    isActive = true
                )
            )

            userDao.insertProfile(
                ProfileEntity(
                    userId = demoUserId,
                    universityId = 1, // SMUCT
                    universityName = "Shanto-Mariam University of Creative Technology",
                    universityShortName = "SMUCT",
                    department = "CSE",
                    batch = "2024",
                    bio = "CS undergrad @ SMUCT 🎓 | Tech enthusiast & campus shutterbug 📸 | Welcome to UniLoop!",
                    avatarUrl = "avatar_1",
                    coverUrl = "cover_1",
                    referralCode = "SHAFIN123",
                    isProfileComplete = true,
                    followersCount = 142,
                    followingCount = 89,
                    postsCount = 3,
                    referralCount = 12,
                    badges = "early_member,theme_unlocked,profile_boost"
                )
            )

            // Seed initial welcoming campus moments
            postDao.insertPost(
                PostEntity(
                    id = 1,
                    authorId = demoUserId,
                    authorName = "Shafin Ahmed",
                    authorUsername = "shafin_smuct",
                    authorAvatarUrl = "avatar_1",
                    authorUniversity = "SMUCT",
                    authorDepartment = "CSE",
                    authorBatch = "2024",
                    universityId = 1,
                    content = "Welcome to UniLoop everyone! 🚀 Exciting to have a dedicated social network for our campus life in Bangladesh. Share your campus moments, memes, and connect with your batchmates! #UniLoop #SMUCT #CampusLife",
                    mediaUrls = "",
                    postType = "status",
                    audience = "public",
                    likesCount = 38,
                    commentsCount = 5,
                    sharesCount = 12,
                    hashtags = "#UniLoop,#SMUCT,#CampusLife",
                    timestamp = System.currentTimeMillis() - 3600000
                )
            )
        }
    }
}
