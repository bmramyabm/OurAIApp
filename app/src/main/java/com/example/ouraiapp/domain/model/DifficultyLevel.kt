package com.example.ouraiapp.domain.model

enum class DifficultyLevel(
    val value: String,
    val title: String,
    val summary: String,
    val topics: List<String>
) {
    JUNIOR(
        value = "junior",
        title = "Junior",
        summary = "Fundamentals and core Android concepts",
        topics = listOf("Activities & Lifecycle", "Layouts & Views", "Intents & Permissions")
    ),
    MID(
        value = "mid",
        title = "Mid / Senior",
        summary = "Architecture, Jetpack, and scalable patterns",
        topics = listOf("MVVM", "Coroutines & Flow", "Jetpack Components")
    ),
    ADVANCED(
        value = "advanced",
        title = "Advanced",
        summary = "Performance, internals, and expert-level Android topics",
        topics = listOf("Performance", "Rendering Pipeline", "Compose Internals")
    );

    companion object {
        fun fromValue(value: String?): DifficultyLevel {
            return entries.firstOrNull { it.value == value } ?: JUNIOR
        }
    }
}
