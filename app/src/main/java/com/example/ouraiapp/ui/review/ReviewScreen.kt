package com.example.ouraiapp.ui.review

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PauseCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ouraiapp.domain.model.QuestionResult
import com.example.ouraiapp.domain.model.QuizSummary

@Composable
fun ReviewScreen(
    viewModel: ReviewViewModel,
    onBackHome: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val summary = uiState.summary

    if (summary == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text("No quiz session found.", style = MaterialTheme.typography.headlineSmall, color = Color.White)
            Spacer(modifier = Modifier.height(12.dp))
            Button(onClick = onBackHome) {
                Text("Back Home")
            }
        }
        return
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF08111E), Color(0xFF0B1220), Color(0xFF0D1324))
                )
            )
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item { Spacer(modifier = Modifier.height(6.dp)) }

        item {
            ReviewHeader(
                title = "Review Answers",
                subtitle = "${summary.difficultyLevel.title} Quiz Summary",
                aiUsageCount = uiState.aiExplanationUsageCount,
                onBackHome = onBackHome
            )
        }

        item {
            ScoreOverviewCard(summary = summary)
        }

        itemsIndexed(summary.results) { index, result ->
            ReviewAnswerCard(
                index = index,
                result = result,
                aiResponse = uiState.aiResponses[result.question.id],
                isLoading = uiState.loadingQuestionIds.contains(result.question.id),
                onAskAi = { viewModel.askAi(result) }
            )
        }

        item {
            Button(
                onClick = onBackHome,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                shape = RoundedCornerShape(22.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF162033)
                )
            ) {
                Icon(Icons.Outlined.Home, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Back to Home", color = Color.White)
            }
        }
    }
}

@Composable
private fun ReviewHeader(
    title: String,
    subtitle: String,
    aiUsageCount: Int,
    onBackHome: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.displaySmall,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "AI explanations used: ${aiUsageCount.coerceAtMost(5)}/5",
                style = MaterialTheme.typography.labelLarge,
                color = if (aiUsageCount >= 5) Color(0xFFF4C95D) else Color(0xFF8B7CFF)
            )
        }
        Surface(
            shape = CircleShape,
            color = Color(0xFF151F31),
            tonalElevation = 0.dp
        ) {
            IconButton(onClick = onBackHome) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = "Close review",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
private fun ScoreOverviewCard(summary: QuizSummary) {
    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF111A2B)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(22.dp)) {
            Text(
                text = "Score Overview",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(18.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ScoreMetricCard(
                    modifier = Modifier.weight(1f),
                    label = "Correct",
                    value = summary.correctCount.toString(),
                    iconTint = Color(0xFF20D782),
                    backgroundTint = Color(0xFF113126),
                    icon = {
                        Icon(Icons.Outlined.CheckCircle, contentDescription = null, tint = Color(0xFF20D782))
                    }
                )
                ScoreMetricCard(
                    modifier = Modifier.weight(1f),
                    label = "Wrong",
                    value = summary.wrongCount.toString(),
                    iconTint = Color(0xFFFF6B6B),
                    backgroundTint = Color(0xFF351A25),
                    icon = {
                        Icon(Icons.Outlined.Close, contentDescription = null, tint = Color(0xFFFF6B6B))
                    }
                )
                ScoreMetricCard(
                    modifier = Modifier.weight(1f),
                    label = "Skipped",
                    value = summary.skippedCount.toString(),
                    iconTint = Color(0xFFF4C95D),
                    backgroundTint = Color(0xFF3A3218),
                    icon = {
                        Icon(Icons.Outlined.PauseCircle, contentDescription = null, tint = Color(0xFFF4C95D))
                    }
                )
            }
        }
    }
}

@Composable
private fun ScoreMetricCard(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    iconTint: Color,
    backgroundTint: Color,
    icon: @Composable () -> Unit
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF162033))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(backgroundTint)
                    .padding(10.dp)
            ) {
                icon()
            }
            Spacer(modifier = Modifier.height(18.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = iconTint
            )
        }
    }
}

@Composable
private fun ReviewAnswerCard(
    index: Int,
    result: QuestionResult,
    aiResponse: String?,
    isLoading: Boolean,
    onAskAi: () -> Unit
) {
    var expanded by rememberSaveable(result.question.id) { mutableStateOf(index == 0) }
    val statusLabel = when {
        result.isSkipped -> "Skipped"
        result.isCorrect -> "Correct"
        else -> "Wrong"
    }
    val statusColor = when {
        result.isSkipped -> Color(0xFFF4C95D)
        result.isCorrect -> Color(0xFF20D782)
        else -> Color(0xFFFF6B6B)
    }

    Card(
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF111A2B)),
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = statusColor.copy(alpha = 0.35f),
                shape = RoundedCornerShape(26.dp)
            )
            .clickable { expanded = !expanded }
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Question ${index + 1}",
                        style = MaterialTheme.typography.labelLarge,
                        color = statusColor
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = result.question.question,
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        maxLines = if (expanded) Int.MAX_VALUE else 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = statusColor.copy(alpha = 0.16f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(statusColor)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = statusLabel,
                            color = statusColor,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = result.question.topic,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Icon(
                    imageVector = if (expanded) Icons.Outlined.ExpandLess else Icons.Outlined.ExpandMore,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            AnimatedVisibility(visible = expanded) {
                Column {
                    Spacer(modifier = Modifier.height(16.dp))

                    Card(
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF172338))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Explanation",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = result.question.explanation,
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color(0xFFD8E3F3)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    GradientAiButton(onClick = onAskAi)

                    if (isLoading) {
                        Spacer(modifier = Modifier.height(14.dp))
                        CircularProgressIndicator(color = Color(0xFF7A5AF8))
                    }

                    if (aiResponse != null) {
                        Spacer(modifier = Modifier.height(14.dp))
                        Card(
                            shape = RoundedCornerShape(22.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF141F31))
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Outlined.AutoAwesome,
                                        contentDescription = null,
                                        tint = Color(0xFF8B7CFF)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "AI Explanation",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = Color.White,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = aiResponse,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color(0xFFD8E3F3)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun GradientAiButton(
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        ),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(Color(0xFF7A5AF8), Color(0xFF2DD4BF))
                    ),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(horizontal = 18.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Outlined.AutoAwesome, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Ask AI Explanation",
                color = Color.White,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(Icons.AutoMirrored.Outlined.ArrowForward, contentDescription = null, tint = Color.White)
        }
    }
}
