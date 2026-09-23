package com.example.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.SeedData
import com.example.data.local.UniLoopDatabase
import com.example.data.local.entity.DepartmentEntity
import com.example.data.local.entity.ProfileEntity
import com.example.data.local.entity.UniversityEntity
import com.example.data.local.entity.UserEntity
import com.example.data.repository.AuthRepository
import com.example.data.repository.AuthResult
import com.example.data.repository.UniversityRepository
import com.example.data.repository.UserSessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val database = UniLoopDatabase.getDatabase(application, viewModelScope)
    val sessionManager = UserSessionManager(application)
    val authRepository = AuthRepository(database.userDao(), database.referralDao(), sessionManager)
    val universityRepository = UniversityRepository(database.universityDao())

    // Current Session State
    val currentUserId: StateFlow<Long?> = sessionManager.currentUserId

    val currentUser: StateFlow<UserEntity?> = currentUserId.flatMapLatest { id ->
        if (id != null) authRepository.getCurrentUserFlow(id) else flowOf(null)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val currentProfile: StateFlow<ProfileEntity?> = currentUserId.flatMapLatest { id ->
        if (id != null) authRepository.getCurrentProfileFlow(id) else flowOf(null)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Login Form State
    var loginIdentifier = MutableStateFlow("")
    var loginPassword = MutableStateFlow("")
    private val _loginError = MutableStateFlow<String?>(null)
    val loginError: StateFlow<String?> = _loginError.asStateFlow()
    private val _isLoggingIn = MutableStateFlow(false)
    val isLoggingIn: StateFlow<Boolean> = _isLoggingIn.asStateFlow()

    // Register Form State
    var regFullName = MutableStateFlow("")
    var regUsername = MutableStateFlow("")
    var regEmail = MutableStateFlow("")
    var regPassword = MutableStateFlow("")
    private val _regError = MutableStateFlow<String?>(null)
    val regError: StateFlow<String?> = _regError.asStateFlow()
    private val _isRegistering = MutableStateFlow(false)
    val isRegistering: StateFlow<Boolean> = _isRegistering.asStateFlow()

    // Password Reset State
    var resetEmail = MutableStateFlow("")
    private val _resetMessage = MutableStateFlow<String?>(null)
    val resetMessage: StateFlow<String?> = _resetMessage.asStateFlow()
    private val _resetError = MutableStateFlow<String?>(null)
    val resetError: StateFlow<String?> = _resetError.asStateFlow()
    private val _isResetting = MutableStateFlow(false)
    val isResetting: StateFlow<Boolean> = _isResetting.asStateFlow()

    // Onboarding Wizard State
    private val _onboardingStep = MutableStateFlow(1) // 1: Uni, 2: Dept/Batch, 3: Avatar/Bio, 4: Referral, 5: Badge
    val onboardingStep: StateFlow<Int> = _onboardingStep.asStateFlow()

    val universitySearchQuery = MutableStateFlow("")
    val allUniversities: StateFlow<List<UniversityEntity>> = universityRepository.universities
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val filteredUniversities: StateFlow<List<UniversityEntity>> = combine(
        allUniversities,
        universitySearchQuery
    ) { unis, query ->
        if (query.isBlank()) unis
        else unis.filter {
            it.name.contains(query, ignoreCase = true) ||
            it.shortName.contains(query, ignoreCase = true) ||
            it.location.contains(query, ignoreCase = true)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allDepartments: StateFlow<List<DepartmentEntity>> = universityRepository.departments
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val selectedUniversity = MutableStateFlow<UniversityEntity?>(null)
    val selectedDepartment = MutableStateFlow("CSE")
    val selectedBatch = MutableStateFlow("2024")
    val userBio = MutableStateFlow("")
    val selectedAvatarId = MutableStateFlow("avatar_1")
    val referralCodeInput = MutableStateFlow("")

    private val _onboardingError = MutableStateFlow<String?>(null)
    val onboardingError: StateFlow<String?> = _onboardingError.asStateFlow()

    private val _isOnboardingSubmitting = MutableStateFlow(false)
    val isOnboardingSubmitting: StateFlow<Boolean> = _isOnboardingSubmitting.asStateFlow()

    fun login(onSuccess: () -> Unit) {
        _loginError.value = null
        _isLoggingIn.value = true
        viewModelScope.launch {
            val result = authRepository.login(loginIdentifier.value, loginPassword.value)
            _isLoggingIn.value = false
            when (result) {
                is AuthResult.Success -> {
                    onSuccess()
                }
                is AuthResult.Error -> {
                    _loginError.value = result.message
                }
            }
        }
    }

    fun quickDemoLogin(onSuccess: () -> Unit) {
        loginIdentifier.value = "shafin_smuct"
        loginPassword.value = "password123"
        login(onSuccess)
    }

    fun register(onSuccess: () -> Unit) {
        _regError.value = null
        _isRegistering.value = true
        viewModelScope.launch {
            val result = authRepository.register(
                fullName = regFullName.value,
                username = regUsername.value,
                email = regEmail.value,
                password = regPassword.value
            )
            _isRegistering.value = false
            when (result) {
                is AuthResult.Success -> {
                    // Reset onboarding wizard for new user
                    _onboardingStep.value = 1
                    selectedUniversity.value = null
                    selectedDepartment.value = "CSE"
                    selectedBatch.value = "2024"
                    userBio.value = "Hey! I'm on UniLoop 👋"
                    selectedAvatarId.value = "avatar_1"
                    referralCodeInput.value = ""
                    onSuccess()
                }
                is AuthResult.Error -> {
                    _regError.value = result.message
                }
            }
        }
    }

    fun requestPasswordReset() {
        _resetError.value = null
        _resetMessage.value = null
        _isResetting.value = true
        viewModelScope.launch {
            val result = authRepository.requestPasswordReset(resetEmail.value)
            _isResetting.value = false
            when (result) {
                is AuthResult.Success -> {
                    _resetMessage.value = result.data
                }
                is AuthResult.Error -> {
                    _resetError.value = result.message
                }
            }
        }
    }

    fun nextOnboardingStep() {
        _onboardingError.value = null
        when (_onboardingStep.value) {
            1 -> {
                if (selectedUniversity.value == null) {
                    _onboardingError.value = "Please select your university to continue"
                    return
                }
                _onboardingStep.value = 2
            }
            2 -> {
                if (selectedDepartment.value.isBlank()) {
                    _onboardingError.value = "Please select your department"
                    return
                }
                _onboardingStep.value = 3
            }
            3 -> {
                _onboardingStep.value = 4
            }
            4 -> {
                submitOnboarding {
                    _onboardingStep.value = 5 // Show welcome badge!
                }
            }
        }
    }

    fun prevOnboardingStep() {
        _onboardingError.value = null
        if (_onboardingStep.value > 1 && _onboardingStep.value < 5) {
            _onboardingStep.value -= 1
        }
    }

    fun submitOnboarding(onSuccess: () -> Unit) {
        val userId = currentUserId.value ?: return
        val uni = selectedUniversity.value ?: return

        _onboardingError.value = null
        _isOnboardingSubmitting.value = true

        viewModelScope.launch {
            val result = authRepository.completeOnboarding(
                userId = userId,
                universityId = uni.id,
                universityName = uni.name,
                universityShortName = uni.shortName,
                department = selectedDepartment.value,
                batch = selectedBatch.value,
                bio = userBio.value,
                avatarUrl = selectedAvatarId.value,
                referralCodeInput = referralCodeInput.value.ifBlank { null }
            )
            _isOnboardingSubmitting.value = false
            when (result) {
                is AuthResult.Success -> {
                    onSuccess()
                }
                is AuthResult.Error -> {
                    _onboardingError.value = result.message
                }
            }
        }
    }

    fun logout() {
        authRepository.logout()
        _onboardingStep.value = 1
    }
}
