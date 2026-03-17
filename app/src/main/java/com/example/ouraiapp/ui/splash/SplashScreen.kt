package com.example.ouraiapp.ui.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PsychologyAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ouraiapp.ui.common.AppAnimatedBackground
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(
    onNavigateToTopics: () -> Unit
) {
    val scale = remember { Animatable(0.8f) }
    val alpha = remember { Animatable(0f) }
    val titleScale = remember { Animatable(0.65f) }
    val loadingTransition = androidx.compose.animation.core.rememberInfiniteTransition(label = "splash-loading")
    val loadingProgress by loadingTransition.animateFloat(
        initialValue = 0.12f,
        targetValue = 0.88f,
        animationSpec = androidx.compose.animation.core.infiniteRepeatable(
            animation = tween(durationMillis = 1400, easing = FastOutSlowInEasing),
            repeatMode = androidx.compose.animation.core.RepeatMode.Reverse
        ),
        label = "loading-progress"
    )

    LaunchedEffect(Unit) {
        launch {
            scale.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
            )
        }
        launch {
            alpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 800)
            )
        }
        launch {
            titleScale.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
            )
        }
        delay(1_800)
        onNavigateToTopics()
    }

    AppAnimatedBackground {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .scale(scale.value)
                .alpha(alpha.value)
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier.size(118.dp),
                shape = RoundedCornerShape(28.dp),
                color = Color(0xFF12303A).copy(alpha = 0.82f),
                tonalElevation = 0.dp,
                shadowElevation = 0.dp
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.radialGradient(
                                colors = listOf(Color(0xFF1EE6B4).copy(alpha = 0.22f), Color.Transparent)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.PsychologyAlt,
                        contentDescription = null,
                        tint = Color(0xFF27E0B8),
                        modifier = Modifier.size(34.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            Row(
                modifier = Modifier.scale(titleScale.value),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "DroidPrep",
                    style = MaterialTheme.typography.displaySmall,
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = " AI",
                    style = MaterialTheme.typography.displaySmall,
                    color = Color(0xFF26D7A9),
                    fontWeight = FontWeight.ExtraBold
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "MASTER INTERVIEWS WITH AI",
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF7B8097),
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(54.dp))

            LoadingTrack(
                progress = loadingProgress,
                modifier = Modifier.fillMaxWidth(0.72f)
            )

            Spacer(modifier = Modifier.height(26.dp))

            Text(
                text = "Powered by AI",
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF21293A),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun LoadingTrack(
    progress: Float,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier.height(12.dp)
    ) {
        val stroke = size.height
        drawLine(
            color = Color(0xFF1A2234),
            start = Offset(0f, center.y),
            end = Offset(size.width, center.y),
            strokeWidth = stroke,
            cap = StrokeCap.Round
        )
        drawLine(
            brush = Brush.horizontalGradient(
                colors = listOf(Color(0xFF1EE6B4), Color(0xFF4AD9FF))
            ),
            start = Offset(0f, center.y),
            end = Offset(size.width * progress, center.y),
            strokeWidth = stroke * 0.72f,
            cap = StrokeCap.Round
        )
        drawCircle(
            color = Color(0xFF6FFFF0),
            radius = stroke * 0.58f,
            center = Offset(size.width * progress, center.y)
        )
        drawCircle(
            color = Color(0xFF6FFFF0).copy(alpha = 0.22f),
            radius = stroke * 1.25f,
            center = Offset(size.width * progress, center.y),
            style = Stroke(width = 2.2f)
        )
    }
}
