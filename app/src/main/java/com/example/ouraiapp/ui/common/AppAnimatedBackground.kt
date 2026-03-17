package com.example.ouraiapp.ui.common

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun AppAnimatedBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF070B18), Color(0xFF0A1020), Color(0xFF08101A))
                )
            )
    ) {
        StarField(modifier = Modifier.fillMaxSize(), alpha = 0.7f)
        content()
    }
}

@Composable
private fun StarField(
    modifier: Modifier = Modifier,
    alpha: Float
) {
    val transition = rememberInfiniteTransition(label = "app-stars")
    val phase by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3600, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "app-star-phase"
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

private data class Particle(
    val x: Float,
    val y: Float,
    val radius: Float,
    val alpha: Float,
    val dx: Float,
    val dy: Float
)
