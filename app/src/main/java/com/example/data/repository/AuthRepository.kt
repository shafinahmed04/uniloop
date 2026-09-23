package com.example.data.repository

import com.example.data.local.dao.ReferralDao
import com.example.data.local.dao.UserDao
import com.example.data.local.entity.NotificationEntity
import com.example.data.local.entity.ProfileEntity
import com.example.data.local.entity.ReferralEntity
import com.example.data.local.entity.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.util.Locale
import java.util.UUID

sealed class AuthResult<out T> {
    data class Success<T>(val data: T) : AuthResult<T>()
    data class Error(val message: String) : AuthResult<Nothing>()
}

class AuthRepository(
    private val userDao: UserDao,
    private val referralDao: ReferralDao,
    private val sessionManager: UserSessionManager
) {
    fun getCurrentUserFlow(userId: Long): Flow<UserEntity?> = userDao.getUserByIdFlow(userId)
    fun getCurrentProfileFlow(userId: Long): Flow<ProfileEntity?> = userDao.getProfileByUserIdFlow(userId)

    suspend fun login(identifier: String, password: String): AuthResult<UserEntity> = withContext(Dispatchers.IO) {
        val trimmed = identifier.trim()
        if (trimmed.isEmpty()) {
            return@withContext AuthResult.Error("Please enter your email or username")
        }
        if (password.isEmpty()) {
            return@withContext AuthResult.Error("Please enter your password")
        }

        val user = if (trimmed.contains("@")) {
            userDao.getUserByEmail(trimmed)
        } else {
            userDao.getUserByUsername(trimmed)
        }

        if (user == null) {
            return@withContext AuthResult.Error("Account not found. Please check your credentials.")
        }

        if (user.passwordHash != password) {
            return@withContext AuthResult.Error("Invalid password. Please try again.")
        }

        sessionManager.setSession(user.id)
        AuthResult.Success(user)
    }

    suspend fun register(
        fullName: String,
        username: String,
        email: String,
        password: String
    ): AuthResult<UserEntity> = withContext(Dispatchers.IO) {
        val cleanName = fullName.trim()
        val cleanUsername = username.trim().lowercase(Locale.ROOT)
        val cleanEmail = email.trim().lowercase(Locale.ROOT)

        if (cleanName.length < 2) {
            return@withContext AuthResult.Error("Please enter your real full name (at least 2 letters)")
        }
        if (cleanUsername.length < 3) {
            return@withContext AuthResult.Error("Username must be at least 3 characters")
        }
        if (!cleanUsername.matches(Regex("^[a-zA-Z0-9_.]+$"))) {
            return@withContext AuthResult.Error("Username can only contain letters, numbers, underscores and dots")
        }
        if (!cleanEmail.contains("@") || !cleanEmail.contains(".")) {
            return@withContext AuthResult.Error("Please enter a valid email address")
        }
        if (password.length < 6) {
            return@withContext AuthResult.Error("Password must be at least 6 characters long")
        }

        val existingUserEmail = userDao.getUserByEmail(cleanEmail)
        if (existingUserEmail != null) {
            return@withContext AuthResult.Error("An account with this email already exists")
        }

        val existingUsername = userDao.getUserByUsername(cleanUsername)
        if (existingUsername != null) {
            return@withContext AuthResult.Error("Username @$cleanUsername is already taken. Try another!")
        }

        val newUser = UserEntity(
            fullName = cleanName,
            username = cleanUsername,
            email = cleanEmail,
            passwordHash = password,
            isVerified = false,
            isActive = true
        )

        val newId = userDao.insertUser(newUser)
        val createdUser = newUser.copy(id = newId)

        // Generate user's unique referral code (e.g., USERNAME + 3 random digits)
        val randomSuffix = (100..999).random()
        val refCode = (cleanUsername.take(6).uppercase() + randomSuffix)

        // Create empty profile waiting for onboarding
        val profile = ProfileEntity(
            userId = newId,
            referralCode = refCode,
            isProfileComplete = false
        )
        userDao.insertProfile(profile)

        sessionManager.setSession(newId)
        AuthResult.Success(createdUser)
    }

    suspend fun completeOnboarding(
        userId: Long,
        universityId: Long,
        universityName: String,
        universityShortName: String,
        department: String,
        batch: String,
        bio: String,
        avatarUrl: String,
        referralCodeInput: String?
    ): AuthResult<ProfileEntity> = withContext(Dispatchers.IO) {
        val user = userDao.getUserById(userId)
            ?: return@withContext AuthResult.Error("User not found")

        val currentProfile = userDao.getProfileByUserId(userId)
            ?: ProfileEntity(userId = userId)

        var cleanReferralInput = referralCodeInput?.trim()?.uppercase(Locale.ROOT)
        if (cleanReferralInput.isNullOrEmpty()) {
            cleanReferralInput = null
        }

        // Anti-abuse: Check if user tries self-referral
        if (cleanReferralInput != null && cleanReferralInput == currentProfile.referralCode) {
            return@withContext AuthResult.Error("You cannot use your own referral code!")
        }

        var verifiedReferrerProfile: ProfileEntity? = null
        if (cleanReferralInput != null) {
            verifiedReferrerProfile = userDao.getProfileByReferralCode(cleanReferralInput)
            if (verifiedReferrerProfile == null) {
                return@withContext AuthResult.Error("Referral code '$cleanReferralInput' is invalid. Leave blank if you don't have one.")
            }
        }

        val updatedProfile = currentProfile.copy(
            universityId = universityId,
            universityName = universityName,
            universityShortName = universityShortName,
            department = department,
            batch = batch,
            bio = bio.trim(),
            avatarUrl = avatarUrl,
            referredByCode = cleanReferralInput,
            isProfileComplete = true
        )

        userDao.updateProfile(updatedProfile)

        // If valid referral, reward referrer and record milestone
        if (verifiedReferrerProfile != null) {
            userDao.incrementReferralCount(verifiedReferrerProfile.userId)
            referralDao.insertReferral(
                ReferralEntity(
                    referrerUserId = verifiedReferrerProfile.userId,
                    referredUserId = userId,
                    referredUserName = user.fullName,
                    referralCode = cleanReferralInput!!,
                    status = "verified",
                    rewardGranted = true
                )
            )
        }

        AuthResult.Success(updatedProfile)
    }

    suspend fun requestPasswordReset(email: String): AuthResult<String> = withContext(Dispatchers.IO) {
        val cleanEmail = email.trim().lowercase(Locale.ROOT)
        if (!cleanEmail.contains("@")) {
            return@withContext AuthResult.Error("Please enter a valid email address")
        }
        val user = userDao.getUserByEmail(cleanEmail)
        if (user == null) {
            return@withContext AuthResult.Error("No account found with this email address")
        }
        // In simulation/dev, return success instructions
        AuthResult.Success("Password reset instructions have been sent to $cleanEmail. Check your inbox.")
    }

    fun logout() {
        sessionManager.clearSession()
    }
}
