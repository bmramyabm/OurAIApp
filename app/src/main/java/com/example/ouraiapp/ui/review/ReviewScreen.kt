package com.example.ouraiapp.ui.review

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

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
            Text("No quiz session found.", style = MaterialTheme.typography.headlineSmall)
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
                    colors = listOf(Color(0xFF06111A), Color(0xFF0A1021))
                )
            )
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Review",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${summary.difficultyLevel.title} quiz summary",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        item {
            Card(
                shape = RoundedCornerShape(26.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Score overview", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        ScoreStat("Correct", summary.correctCount.toString(), Color(0xFF20D782))
                        ScoreStat("Wrong", summary.wrongCount.toString(), Color(0xFFFF6B6B))
                        ScoreStat("Skipped", summary.skippedCount.toString(), Color(0xFFF4C95D))
                    }
                }
            }
        }
        itemsIndexed(summary.results) { index, result ->
            val aiResponse = uiState.aiResponses[result.question.id]
            val isLoading = uiState.loadingQuestionIds.contains(result.question.id)

            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Q${index + 1} • ${result.question.topic}",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = result.question.question,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        val statusText = when {
                            result.isSkipped -> "Skipped"
                            result.isCorrect -> "Correct"
                            else -> "Wrong"
                        }
                        val statusColor = when {
                            result.isSkipped -> Color(0xFFF4C95D)
                            result.isCorrect -> Color(0xFF20D782)
                            else -> Color(0xFFFF6B6B)
                        }
                        Text(
                            text = statusText,
                            color = statusColor,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "Correct answer: ${result.question.correctAnswer}",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF10382B))
                    ) {
                        Text(
                            text = result.question.explanation,
                            modifier = Modifier.padding(16.dp),
                            color = Color(0xFFD3FBE9)
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { viewModel.askAi(result) },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(Icons.Outlined.Lightbulb, contentDescription = null)
                        Text("Ask AI", modifier = Modifier.padding(start = 8.dp))
                    }
                    if (isLoading) {
                        Spacer(modifier = Modifier.height(12.dp))
                        CircularProgressIndicator()
                    }
                    if (aiResponse != null) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                            )
                        ) {
                            Text(
                                text = aiResponse,
                                modifier = Modifier.padding(16.dp),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
        item {
            Button(
                onClick = onBackHome,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Icon(Icons.Outlined.Home, contentDescription = null)
                Text("Back to Home", modifier = Modifier.padding(start = 8.dp))
            }
        }
    }
}

@Composable
private fun ScoreStat(label: String, value: String, color: Color) {
    Column {
        Text(value, style = MaterialTheme.typography.headlineMedium, color = color, fontWeight = FontWeight.Bold)
        Text(label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
