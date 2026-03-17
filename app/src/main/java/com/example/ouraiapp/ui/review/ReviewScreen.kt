package com.example.ouraiapp.ui.review

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PauseCircle
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Timer
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ouraiapp.domain.model.QuestionResult
import com.example.ouraiapp.domain.model.QuizSummary
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun ReviewScreen(
    viewModel: ReviewViewModel,
    onBackHome: () -> Unit,
    onRetryQuiz: (QuizSummary) -> Unit
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val summary = uiState.summary

    if (summary == null) {
        EmptyReviewState(onBackHome = onBackHome)
        return
    }

    val scoreFraction = if (summary.totalQuestions == 0) 0f else {
        summary.correctCount / summary.totalQuestions.toFloat()
    }
    val animatedProgress = remember { Animatable(0f) }
    var breakdownExpanded by rememberSaveable { mutableStateOf(false) }
    var expandedQuestionId by rememberSaveable { mutableIntStateOf(-1) }

    LaunchedEffect(scoreFraction) {
        animatedProgress.snapTo(0f)
        animatedProgress.animateTo(
            targetValue = scoreFraction,
            animationSpec = tween(durationMillis = 1200, easing = FastOutSlowInEasing)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF070B18), Color(0xFF0A1020), Color(0xFF08101A))
                )
            )
    ) {
        ResultParticles(
            modifier = Modifier.fillMaxSize(),
            alpha = 0.7f
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding(),
            contentPadding = PaddingValues(horizontal = 22.dp, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            item {
                TopReviewBar(
                    difficultyTitle = summary.difficultyLevel.title,
                    onBackHome = onBackHome
                )
            }

            item {
                ResultHero(
                    progress = animatedProgress.value,
                    summary = summary
                )
            }

            item {
                ScoreCards(summary = summary)
            }

            item {
                AiInsightCard(scoreFraction = scoreFraction, summary = summary)
            }

            item {
                BreakdownHeader(
                    expanded = breakdownExpanded,
                    onToggle = {
                        breakdownExpanded = !breakdownExpanded
                        if (!breakdownExpanded) expandedQuestionId = -1
                    }
                )
            }

            if (breakdownExpanded) {
                itemsIndexed(summary.results) { index, result ->
                    QuestionBreakdownItem(
                        index = index,
                        result = result,
                        expanded = expandedQuestionId == result.question.id,
                        aiResponse = uiState.aiResponses[result.question.id],
                        isLoading = uiState.loadingQuestionIds.contains(result.question.id),
                        onToggle = {
                            expandedQuestionId =
                                if (expandedQuestionId == result.question.id) -1 else result.question.id
                        },
                        onAskAi = { viewModel.askAi(result) }
                    )
                }
            }

            item {
                Button(
                    onClick = { onRetryQuiz(summary) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2ED2A8),
                        contentColor = Color(0xFF04151A)
                    )
                ) {
                    Icon(Icons.Outlined.Refresh, contentDescription = null)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Try Again",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            item {
                Button(
                    onClick = onBackHome,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF141A28),
                        contentColor = Color(0xFFB8C0CF)
                    )
                ) {
                    Icon(Icons.Outlined.Home, contentDescription = null)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Back to Levels",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun TopReviewBar(
    difficultyTitle: String,
    onBackHome: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.clickable(onClick = onBackHome),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                contentDescription = "Back",
                tint = Color(0xFFC8CEDB)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Back",
                style = MaterialTheme.typography.titleLarge,
                color = Color(0xFFC8CEDB)
            )
        }

        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color(0xFF13322F),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2ED2A8).copy(alpha = 0.45f))
        ) {
            Text(
                text = difficultyTitle,
                modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp),
                color = Color(0xFF34E4B7),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.width(54.dp))
    }
}

@Composable
private fun ResultHero(
    progress: Float,
    summary: QuizSummary
) {
    val percentage = (progress * 100).toInt()
    val scoreFraction = if (summary.totalQuestions == 0) 0f else {
        summary.correctCount / summary.totalQuestions.toFloat()
    }
    val ringColor = Color(0xFFFFB01F)
    val captionColor = scoreColor(scoreFraction)
    val (emoji, caption) = scoreCaption(scoreFraction)

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "QUIZ COMPLETE",
            style = MaterialTheme.typography.titleMedium,
            color = Color(0xFF6C7187),
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = buildAnnotatedString {
                append("Your ")
                withStyle(SpanStyle(color = Color(0xFF2ED2A8))) {
                    append("Results")
                }
            },
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(modifier = Modifier.height(26.dp))
        ScoreRing(
            progress = progress,
            accentColor = ringColor
        ) {
            Text(
                text = "${percentage}%",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = "Score",
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF9AA2B2)
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = emoji,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = caption,
                style = MaterialTheme.typography.titleLarge,
                color = captionColor,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun ScoreRing(
    progress: Float,
    accentColor: Color,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = Modifier.size(270.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 22.dp.toPx()
            val arcSize = Size(size.width - strokeWidth * 2, size.height - strokeWidth * 2)
            val arcTopLeft = Offset(strokeWidth, strokeWidth)
            val sweep = 260f * progress
            drawArc(
                color = Color(0xFF2A2230).copy(alpha = 0.7f),
                startAngle = 140f,
                sweepAngle = 260f,
                useCenter = false,
                topLeft = arcTopLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
            drawArc(
                color = accentColor.copy(alpha = 0.16f),
                startAngle = 140f,
                sweepAngle = sweep,
                useCenter = false,
                topLeft = arcTopLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth * 2.3f, cap = StrokeCap.Round)
            )
            drawArc(
                color = accentColor.copy(alpha = 0.10f),
                startAngle = 140f,
                sweepAngle = sweep,
                useCenter = false,
                topLeft = arcTopLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth * 3.2f, cap = StrokeCap.Round)
            )
            drawArc(
                brush = Brush.sweepGradient(
                    colors = listOf(
                        Color(0xFFFFD36A),
                        accentColor,
                        Color(0xFFFFA000),
                        Color(0xFFFFD36A)
                    )
                ),
                startAngle = 140f,
                sweepAngle = sweep,
                useCenter = false,
                topLeft = arcTopLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
            drawCircle(
                color = accentColor.copy(alpha = 0.22f),
                radius = size.minDimension * 0.36f,
                center = center,
                blendMode = BlendMode.Screen
            )
            drawCircle(
                color = Color(0xFFFFC85C).copy(alpha = 0.10f),
                radius = size.minDimension * 0.44f,
                center = center,
                blendMode = BlendMode.Screen
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally, content = content)
    }
}

@Composable
private fun ScoreCards(summary: QuizSummary) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        ResultStatCard(
            modifier = Modifier.weight(1f),
            icon = Icons.Outlined.CheckCircle,
            iconTint = Color(0xFF34E4B7),
            iconBackground = Color(0xFF13322F),
            value = summary.correctCount.toString(),
            label = "Correct"
        )
        ResultStatCard(
            modifier = Modifier.weight(1f),
            icon = Icons.Outlined.Cancel,
            iconTint = Color(0xFFFF616C),
            iconBackground = Color(0xFF341923),
            value = summary.wrongCount.toString(),
            label = "Wrong"
        )
        ResultStatCard(
            modifier = Modifier.weight(1f),
            icon = Icons.Outlined.Timer,
            iconTint = Color(0xFFFFB13A),
            iconBackground = Color(0xFF34281B),
            value = summary.elapsedTimeMillis.asTimeLabel(),
            label = "Time"
        )
    }
}

@Composable
private fun ResultStatCard(
    modifier: Modifier = Modifier,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color,
    iconBackground: Color,
    value: String,
    label: String
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF151A2B).copy(alpha = 0.94f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 22.dp, horizontal = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = iconBackground,
                border = androidx.compose.foundation.BorderStroke(1.dp, iconTint.copy(alpha = 0.35f))
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.padding(14.dp)
                )
            }
            Spacer(modifier = Modifier.height(18.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.titleLarge,
                color = Color(0xFF8A91A3),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun AiInsightCard(
    scoreFraction: Float,
    summary: QuizSummary
) {
    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0E2027).copy(alpha = 0.96f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2ED2A8).copy(alpha = 0.35f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(18.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = Color(0xFF153B3A),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2ED2A8).copy(alpha = 0.4f))
            ) {
                Icon(
                    imageVector = Icons.Outlined.AutoAwesome,
                    contentDescription = null,
                    tint = Color(0xFF2ED2A8),
                    modifier = Modifier.padding(14.dp)
                )
            }
            Column {
                Text(
                    text = "AI Insight",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF34E4B7),
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = insightText(scoreFraction = scoreFraction, summary = summary),
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFFD4DCE9)
                )
            }
        }
    }
}

@Composable
private fun BreakdownHeader(
    expanded: Boolean,
    onToggle: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onToggle),
        shape = RoundedCornerShape(24.dp),
        color = Color(0xFF151A2B),
        border = androidx.compose.foundation.BorderStroke(
            2.dp,
            Brush.horizontalGradient(
                colors = listOf(Color(0xFFFFFFFF), Color(0xFF2ED2A8), Color(0xFFFFFFFF))
            )
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 22.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Question Breakdown",
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.titleLarge,
                color = Color(0xFFD0D3DC),
                fontWeight = FontWeight.Bold
            )
            Icon(
                imageVector = if (expanded) Icons.Outlined.ExpandLess else Icons.Outlined.ExpandMore,
                contentDescription = null,
                tint = Color(0xFF8F95A7)
            )
        }
    }
}

@Composable
private fun QuestionBreakdownItem(
    index: Int,
    result: QuestionResult,
    expanded: Boolean,
    aiResponse: String?,
    isLoading: Boolean,
    onToggle: () -> Unit,
    onAskAi: () -> Unit
) {
    val statusColor = when {
        result.isSkipped -> Color(0xFFF4C95D)
        result.isCorrect -> Color(0xFF2EE4B5)
        else -> Color(0xFFFF6A70)
    }
    val backgroundColor = if (result.isCorrect || result.isSkipped) {
        Color(0xFF0D1A23)
    } else {
        Color(0xFF1A121F)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onToggle),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        border = androidx.compose.foundation.BorderStroke(1.dp, statusColor.copy(alpha = 0.22f))
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "%02d".format(index + 1),
                    style = MaterialTheme.typography.titleLarge,
                    color = Color(0xFF697286),
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(18.dp))
                Text(
                    text = result.question.question,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleLarge,
                    color = Color(0xFFD8DDE7),
                    fontWeight = FontWeight.SemiBold,
                    maxLines = if (expanded) 3 else 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.width(12.dp))
                Icon(
                    imageVector = when {
                        result.isSkipped -> Icons.Outlined.PauseCircle
                        result.isCorrect -> Icons.Outlined.CheckCircle
                        else -> Icons.Outlined.Cancel
                    },
                    contentDescription = null,
                    tint = statusColor
                )
            }

            AnimatedVisibility(visible = expanded) {
                Column {
                    Spacer(modifier = Modifier.height(16.dp))
                    BreakdownDetailCard(
                        title = "Your Answer",
                        value = result.selectedAnswerText()
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    BreakdownDetailCard(
                        title = "Correct Answer",
                        value = result.correctAnswerText()
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    BreakdownDetailCard(
                        title = "Explanation",
                        value = result.question.explanation
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = onAskAi,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF182235),
                            contentColor = Color(0xFF2ED2A8)
                        )
                    ) {
                        Icon(Icons.Outlined.AutoAwesome, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Ask AI Explanation")
                    }
                    if (isLoading) {
                        Spacer(modifier = Modifier.height(12.dp))
                        CircularProgressIndicator(color = Color(0xFF2ED2A8))
                    }
                    if (aiResponse != null) {
                        Spacer(modifier = Modifier.height(12.dp))
                        BreakdownDetailCard(
                            title = "AI Explanation",
                            value = aiResponse
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BreakdownDetailCard(
    title: String,
    value: String
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF101828))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = Color(0xFF7F8899),
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFFD7E0EF)
            )
        }
    }
}

@Composable
private fun EmptyReviewState(onBackHome: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF08101E))
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "No quiz session found.",
            style = MaterialTheme.typography.headlineSmall,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = onBackHome) {
            Text("Back Home")
        }
    }
}

@Composable
private fun ResultParticles(
    modifier: Modifier = Modifier,
    alpha: Float
) {
    val transition = rememberInfiniteTransition(label = "review-particles")
    val phase by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3600, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "review-phase"
    )
    val stars = remember {
        listOf(
            Particle(0.08f, 0.10f, 3.0f, 0.38f, 0.020f, 0.024f),
            Particle(0.16f, 0.18f, 2.6f, 0.34f, -0.018f, 0.017f),
            Particle(0.26f, 0.12f, 3.4f, 0.42f, 0.019f, -0.014f),
            Particle(0.34f, 0.24f, 2.8f, 0.30f, -0.017f, 0.016f),
            Particle(0.46f, 0.09f, 4.2f, 0.52f, 0.018f, 0.020f),
            Particle(0.58f, 0.16f, 3.1f, 0.40f, -0.016f, 0.018f),
            Particle(0.72f, 0.11f, 4.7f, 0.56f, 0.017f, -0.015f),
            Particle(0.84f, 0.20f, 4.0f, 0.48f, -0.018f, 0.022f),
            Particle(0.92f, 0.12f, 2.7f, 0.34f, 0.015f, -0.014f),
            Particle(0.12f, 0.32f, 3.5f, 0.40f, 0.018f, -0.013f),
            Particle(0.24f, 0.42f, 4.6f, 0.50f, -0.020f, 0.016f),
            Particle(0.38f, 0.34f, 2.9f, 0.36f, 0.019f, 0.014f),
            Particle(0.52f, 0.28f, 3.8f, 0.46f, -0.017f, 0.017f),
            Particle(0.66f, 0.40f, 3.4f, 0.38f, 0.016f, -0.015f),
            Particle(0.80f, 0.36f, 4.3f, 0.50f, -0.019f, 0.018f),
            Particle(0.90f, 0.46f, 3.0f, 0.36f, 0.017f, -0.016f),
            Particle(0.10f, 0.58f, 2.8f, 0.34f, 0.016f, 0.014f),
            Particle(0.18f, 0.70f, 4.4f, 0.48f, -0.017f, 0.017f),
            Particle(0.30f, 0.62f, 3.2f, 0.38f, 0.020f, -0.014f),
            Particle(0.42f, 0.74f, 2.7f, 0.34f, -0.016f, 0.016f),
            Particle(0.56f, 0.60f, 3.6f, 0.42f, 0.017f, -0.013f),
            Particle(0.68f, 0.72f, 4.2f, 0.46f, -0.018f, 0.017f),
            Particle(0.78f, 0.84f, 3.1f, 0.40f, 0.016f, 0.014f),
            Particle(0.90f, 0.78f, 3.5f, 0.42f, -0.017f, -0.013f),
            Particle(0.14f, 0.88f, 2.6f, 0.32f, 0.015f, 0.012f),
            Particle(0.50f, 0.88f, 2.9f, 0.34f, -0.016f, 0.013f),
            Particle(0.86f, 0.92f, 2.8f, 0.36f, 0.014f, -0.012f)
        )
    }

    Canvas(modifier = modifier.alpha(alpha)) {
        val points = stars.mapIndexed { index, particle ->
            val angle = (phase * 6.28318f) + index
            Offset(
                x = (particle.x + sin(angle) * particle.dx).coerceIn(0.06f, 0.94f) * size.width,
                y = (particle.y + cos(angle) * particle.dy).coerceIn(0.04f, 0.96f) * size.height
            )
        }
        for (i in 0 until points.lastIndex) {
            drawLine(
                color = Color(0xFF173244).copy(alpha = 0.16f),
                start = points[i],
                end = points[i + 1],
                strokeWidth = 1.2f
            )
        }
        stars.forEachIndexed { index, particle ->
            drawCircle(
                color = Color(0xFF38E5C1).copy(alpha = particle.alpha * 0.28f),
                radius = particle.radius * 5.8f,
                center = points[index],
                blendMode = BlendMode.Screen
            )
            drawCircle(
                color = Color(0xFF84FFF0).copy(alpha = particle.alpha * 0.48f),
                radius = particle.radius * 2.6f,
                center = points[index],
                blendMode = BlendMode.Screen
            )
            drawCircle(
                color = Color(0xFFB8FFF8).copy(alpha = particle.alpha),
                radius = particle.radius * 1.15f,
                center = points[index]
            )
            drawLine(
                color = Color(0xFF84FFF0).copy(alpha = particle.alpha * 0.55f),
                start = Offset(points[index].x - particle.radius * 3.1f, points[index].y),
                end = Offset(points[index].x + particle.radius * 3.1f, points[index].y),
                strokeWidth = 1.35f
            )
            drawLine(
                color = Color(0xFF84FFF0).copy(alpha = particle.alpha * 0.55f),
                start = Offset(points[index].x, points[index].y - particle.radius * 3.1f),
                end = Offset(points[index].x, points[index].y + particle.radius * 3.1f),
                strokeWidth = 1.35f
            )
        }
    }
}

private fun scoreCaption(scoreFraction: Float): Pair<String, String> {
    return when {
        scoreFraction >= 0.9f -> "🏆" to "Outstanding"
        scoreFraction >= 0.8f -> "🔥" to "Great Job"
        scoreFraction >= 0.7f -> "💪" to "Good Effort"
        scoreFraction >= 0.5f -> "📘" to "Keep Going"
        else -> "🛠️" to "Needs Practice"
    }
}

private fun scoreColor(scoreFraction: Float): Color {
    return when {
        scoreFraction >= 0.9f -> Color(0xFF2ED2A8)
        scoreFraction >= 0.8f -> Color(0xFF46D6FF)
        scoreFraction >= 0.7f -> Color(0xFFFFAE1A)
        scoreFraction >= 0.5f -> Color(0xFFF4C95D)
        else -> Color(0xFFFF6A70)
    }
}

private fun insightText(scoreFraction: Float, summary: QuizSummary): String {
    val weakTopics = summary.results
        .filter { !it.isCorrect && !it.isSkipped }
        .map { it.question.topic }
        .distinct()
        .take(2)
    val focusArea = if (weakTopics.isEmpty()) {
        "Keep reinforcing your strongest Android fundamentals."
    } else {
        "Focus on ${weakTopics.joinToString(" and ")} to push your score higher."
    }

    return when {
        scoreFraction >= 0.85f -> "Strong performance. You are interview-ready on this level. $focusArea"
        scoreFraction >= 0.7f -> "Good foundation overall. $focusArea"
        else -> "You have the basics, but consistency needs work. $focusArea"
    }
}

private fun QuestionResult.selectedAnswerText(): String {
    if (isSkipped || selectedAnswer == null) return "Skipped"
    return question.options.firstOrNull { it.id == selectedAnswer }?.text ?: "Unknown answer"
}

private fun QuestionResult.correctAnswerText(): String {
    return question.options.firstOrNull { it.id == question.correctAnswer }?.text ?: "Unknown answer"
}

private fun Long.asTimeLabel(): String {
    val totalSeconds = (this / 1000).coerceAtLeast(0)
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%d:%02d".format(minutes, seconds)
}

private data class Particle(
    val x: Float,
    val y: Float,
    val radius: Float,
    val alpha: Float,
    val dx: Float,
    val dy: Float
)
