package com.example.ouraiapp.domain.model

enum class QuizLengthOption(
    val questionCount: Int,
    val title: String,
    val subtitle: String,
    val estimate: String
) {
    QUICK(10, "Quick Sprint", "Fast focused practice session", "~5 min"),
    STANDARD(20, "Standard Quiz", "Balanced coverage of Android topics", "~10 min"),
    FULL(30, "Full Mock Test", "Comprehensive interview simulation", "~15 min");

    companion object {
        fun fromCount(count: Int): QuizLengthOption {
            return entries.firstOrNull { it.questionCount == count } ?: QUICK
        }
    }
}
