package com.example.ui.auth

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.SeedData
import com.example.data.local.entity.UniversityEntity
import com.example.ui.components.AvatarPickerRow
import com.example.ui.components.CampusBadge
import com.example.ui.components.UserAvatarView
import com.example.ui.theme.UniAmberTertiary
import com.example.ui.theme.UniIndigoLight
import com.example.ui.theme.UniIndigoPrimary
import com.example.ui.theme.UniTealSecondary

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun OnboardingScreen(
    viewModel: AuthViewModel,
    onOnboardingCompleted: () -> Unit,
    modifier: Modifier = Modifier
) {
    val step by viewModel.onboardingStep.collectAsStateWithLifecycle()
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val currentProfile by viewModel.currentProfile.collectAsStateWithLifecycle()
    val error by viewModel.onboardingError.collectAsStateWithLifecycle()
    val isSubmitting by viewModel.isOnboardingSubmitting.collectAsStateWithLifecycle()

    val selectedUni by viewModel.selectedUniversity.collectAsStateWithLifecycle()
    val selectedDept by viewModel.selectedDepartment.collectAsStateWithLifecycle()
    val selectedBatch by viewModel.selectedBatch.collectAsStateWithLifecycle()
    val userBio by viewModel.userBio.collectAsStateWithLifecycle()
    val selectedAvatarId by viewModel.selectedAvatarId.collectAsStateWithLifecycle()
    val referralInput by viewModel.referralCodeInput.collectAsStateWithLifecycle()

    val filteredUnis by viewModel.filteredUniversities.collectAsStateWithLifecycle()
    val departments by viewModel.allDepartments.collectAsStateWithLifecycle()
    val searchQuery by viewModel.universitySearchQuery.collectAsStateWithLifecycle()

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            // Header with back button & step progress
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (step in 2..4) {
                    IconButton(
                        onClick = { viewModel.prevOnboardingStep() },
                        modifier = Modifier.testTag("onboarding_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Previous step"
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.size(48.dp))
                }

                Text(
                    text = "Campus Onboarding",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "$step/5",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(end = 12.dp)
                )
            }

            LinearProgressIndicator(
                progress = { step / 5f },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )

            if (error != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = error!!,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            AnimatedContent(
                targetState = step,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                label = "OnboardingWizard"
            ) { targetStep ->
                when (targetStep) {
                    1 -> StepUniversitySelection(
                        searchQuery = searchQuery,
                        onSearchChange = { viewModel.universitySearchQuery.value = it },
                        universities = filteredUnis,
                        selectedUniversity = selectedUni,
                        onSelectUniversity = { viewModel.selectedUniversity.value = it }
                    )
                    2 -> StepDepartmentAndBatch(
                        departments = departments,
                        selectedDepartment = selectedDept,
                        onSelectDepartment = { viewModel.selectedDepartment.value = it },
                        selectedBatch = selectedBatch,
                        onSelectBatch = { viewModel.selectedBatch.value = it },
                        universityShortName = selectedUni?.shortName ?: ""
                    )
                    3 -> StepAvatarAndBio(
                        selectedAvatarId = selectedAvatarId,
                        onSelectAvatar = { viewModel.selectedAvatarId.value = it },
                        bio = userBio,
                        onBioChange = { viewModel.userBio.value = it },
                        fullName = currentUser?.fullName ?: "Student"
                    )
                    4 -> StepReferralCode(
                        referralInput = referralInput,
                        onReferralChange = { viewModel.referralCodeInput.value = it }
                    )
                    5 -> StepWelcomeBadge(
                        fullName = currentUser?.fullName ?: "Student",
                        username = currentUser?.username ?: "student",
                        universityName = selectedUni?.name ?: "University",
                        universityShortName = selectedUni?.shortName ?: "CAMPUS",
                        department = selectedDept,
                        batch = selectedBatch,
                        avatarId = selectedAvatarId,
                        referralCode = currentProfile?.referralCode ?: (currentUser?.username?.uppercase() ?: "LOOP"),
                        onEnterCampus = onOnboardingCompleted
                    )
                }
            }

            // Bottom action button
            if (step < 5) {
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = { viewModel.nextOnboardingStep() },
                    enabled = !isSubmitting,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("onboarding_continue_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    if (isSubmitting) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text(
                            text = if (step == 4) "Finish & Mint Badge 🎓" else "Continue →",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StepUniversitySelection(
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    universities: List<UniversityEntity>,
    selectedUniversity: UniversityEntity?,
    onSelectUniversity: (UniversityEntity) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Select Your University 🎓",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Choose your campus across Bangladesh to connect with batchmates",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
        )

        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchChange,
            placeholder = { Text("Search universities (e.g. SMUCT, DU, BUET...)") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("university_search_input"),
            shape = RoundedCornerShape(14.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("university_list"),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(universities, key = { it.id }) { uni ->
                val isSelected = selectedUniversity?.id == uni.id
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { onSelectUniversity(uni) }
                        .testTag("university_card_${uni.shortName}"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) {
                            MaterialTheme.colorScheme.primaryContainer
                        } else {
                            MaterialTheme.colorScheme.surface
                        }
                    ),
                    border = if (isSelected) {
                        CardDefaults.outlinedCardBorder().copy(
                            brush = Brush.horizontalGradient(
                                listOf(UniIndigoPrimary, UniTealSecondary)
                            ),
                            width = 2.dp
                        )
                    } else CardDefaults.outlinedCardBorder()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = uni.logoEmoji,
                            fontSize = 28.sp,
                            modifier = Modifier.padding(end = 14.dp)
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = uni.shortName,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(MaterialTheme.colorScheme.surfaceVariant)
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = uni.type,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontSize = 10.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                            Text(
                                text = uni.name,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                            Text(
                                text = "📍 ${uni.location}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.outline,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }

                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Selected",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun StepDepartmentAndBatch(
    departments: List<com.example.data.local.entity.DepartmentEntity>,
    selectedDepartment: String,
    onSelectDepartment: (String) -> Unit,
    selectedBatch: String,
    onSelectBatch: (String) -> Unit,
    universityShortName: String
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Department & Batch 📚",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Set your major and graduation year at $universityShortName",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
        )

        Text(
            text = "Select Department",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(8.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            departments.forEach { dept ->
                val isSelected = selectedDepartment == dept.code
                FilterChip(
                    selected = isSelected,
                    onClick = { onSelectDepartment(dept.code) },
                    label = { Text("${dept.iconEmoji} ${dept.code}") },
                    shape = RoundedCornerShape(12.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    modifier = Modifier.testTag("dept_chip_${dept.code}")
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Select Batch / Year",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(8.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            SeedData.batches.forEach { batchYear ->
                val isSelected = selectedBatch == batchYear
                FilterChip(
                    selected = isSelected,
                    onClick = { onSelectBatch(batchYear) },
                    label = { Text("Batch $batchYear") },
                    shape = RoundedCornerShape(12.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.secondary,
                        selectedLabelColor = MaterialTheme.colorScheme.onSecondary
                    ),
                    modifier = Modifier.testTag("batch_chip_$batchYear")
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Live badge preview!
        Text(
            text = "Your Identity Badge Preview",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 6.dp)
        )
        CampusBadge(
            department = selectedDepartment,
            universityShortName = universityShortName,
            batch = selectedBatch,
            isHighlighted = true
        )
    }
}

@Composable
private fun StepAvatarAndBio(
    selectedAvatarId: String,
    onSelectAvatar: (String) -> Unit,
    bio: String,
    onBioChange: (String) -> Unit,
    fullName: String
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Express Yourself ✨",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Choose your campus persona and write a friendly bio",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
        )

        // Selected Avatar Large Preview
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            UserAvatarView(
                avatarId = selectedAvatarId,
                name = fullName,
                size = 84.dp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        AvatarPickerRow(
            selectedAvatarId = selectedAvatarId,
            onSelect = onSelectAvatar
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Campus Bio",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(6.dp))

        OutlinedTextField(
            value = bio,
            onValueChange = { if (it.length <= 150) onBioChange(it) },
            placeholder = { Text("What's your vibe? Interests, campus clubs, coffee spots...") },
            supportingText = { Text("${bio.length}/150 characters") },
            minLines = 3,
            maxLines = 4,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("onboarding_bio_input"),
            shape = RoundedCornerShape(14.dp)
        )
    }
}

@Composable
private fun StepReferralCode(
    referralInput: String,
    onReferralChange: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Got an Invite? 🎟️",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Were you invited by a friend or campus ambassador?",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
            )
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.ConfirmationNumber,
                    contentDescription = null,
                    tint = UniAmberTertiary,
                    modifier = Modifier.size(36.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Bonus: Early Member Badge 🏅",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Entering a valid friend's code links you to your campus network and unlocks early member rewards.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = referralInput,
            onValueChange = { onReferralChange(it.uppercase()) },
            label = { Text("Referral Code (Optional)") },
            placeholder = { Text("e.g. SHAFIN123") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("referral_code_input"),
            shape = RoundedCornerShape(14.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Tip: Try code 'SHAFIN123' to test referral verification with demo student Shafin Ahmed @ SMUCT!",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun StepWelcomeBadge(
    fullName: String,
    username: String,
    universityName: String,
    universityShortName: String,
    department: String,
    batch: String,
    avatarId: String,
    referralCode: String,
    onEnterCampus: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Celebration,
            contentDescription = null,
            tint = UniAmberTertiary,
            modifier = Modifier.size(56.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "You're In the Loop!",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Black,
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = "Welcome to the $universityShortName campus community",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Physical Student Digital Pass Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.linearGradient(
                            listOf(
                                UniIndigoPrimary.copy(alpha = 0.08f),
                                UniTealSecondary.copy(alpha = 0.08f)
                            )
                        )
                    )
                    .padding(24.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    UserAvatarView(
                        avatarId = avatarId,
                        name = fullName,
                        size = 72.dp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = fullName,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "@$username",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    CampusBadge(
                        department = department,
                        universityShortName = universityShortName,
                        batch = batch,
                        isHighlighted = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = universityName,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Personal Referral Link Box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "🔗 uniloop.app/join/$referralCode",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        Button(
            onClick = onEnterCampus,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("enter_campus_feed_button"),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text(
                text = "Enter Campus Feed 🚀",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
