package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entity.CommentEntity
import com.example.data.local.entity.FollowEntity
import com.example.data.local.entity.PostEntity
import com.example.data.local.entity.PostLikeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PostDao {
    @Query("SELECT * FROM posts ORDER BY timestamp DESC")
    fun getAllPosts(): Flow<List<PostEntity>>

    @Query("SELECT * FROM posts WHERE universityId = :universityId ORDER BY timestamp DESC")
    fun getCampusPosts(universityId: Long): Flow<List<PostEntity>>

    @Query("SELECT * FROM posts WHERE authorId = :authorId ORDER BY timestamp DESC")
    fun getUserPosts(authorId: Long): Flow<List<PostEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: PostEntity): Long

    @Query("UPDATE posts SET likesCount = likesCount + 1 WHERE id = :postId")
    suspend fun incrementLikes(postId: Long)

    @Query("UPDATE posts SET commentsCount = commentsCount + 1 WHERE id = :postId")
    suspend fun incrementComments(postId: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLike(like: PostLikeEntity)

    @Query("DELETE FROM post_likes WHERE postId = :postId AND userId = :userId")
    suspend fun deleteLike(postId: Long, userId: Long)

    @Query("SELECT EXISTS(SELECT 1 FROM post_likes WHERE postId = :postId AND userId = :userId)")
    suspend fun hasUserLikedPost(postId: Long, userId: Long): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComment(comment: CommentEntity): Long

    @Query("SELECT * FROM comments WHERE postId = :postId ORDER BY timestamp ASC")
    fun getCommentsForPost(postId: Long): Flow<List<CommentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFollow(follow: FollowEntity)

    @Query("DELETE FROM follows WHERE followerId = :followerId AND followingId = :followingId")
    suspend fun deleteFollow(followerId: Long, followingId: Long)

    @Query("SELECT EXISTS(SELECT 1 FROM follows WHERE followerId = :followerId AND followingId = :followingId)")
    suspend fun isFollowing(followerId: Long, followingId: Long): Boolean
}
