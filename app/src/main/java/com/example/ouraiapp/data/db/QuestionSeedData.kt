package com.example.ouraiapp.data.db

import com.example.ouraiapp.domain.model.DifficultyLevel

object QuestionSeedData {

    fun sampleQuestions(): List<QuestionEntity> = listOf(
        QuestionEntity(
            id = 1,
            topic = "Activities & Lifecycle",
            difficultyLevel = DifficultyLevel.JUNIOR.value,
            question = "What does the Android R class represent?",
            optionA = "A runtime configuration registry",
            optionB = "A generated class that contains resource IDs",
            optionC = "A repository used by Room",
            optionD = "A Retrofit response wrapper",
            correctAnswer = "B",
            explanation = "R is generated during build time and provides integer IDs for layouts, strings, drawables, and other resources in the app."
        ),
        QuestionEntity(
            id = 2,
            topic = "Activities & Lifecycle",
            difficultyLevel = DifficultyLevel.JUNIOR.value,
            question = "Which callback is the best place to inflate views and do one-time Activity setup?",
            optionA = "onStart()",
            optionB = "onResume()",
            optionC = "onCreate()",
            optionD = "onStop()",
            correctAnswer = "C",
            explanation = "onCreate() is intended for initial setup such as inflating the UI, initializing dependencies, and restoring saved state."
        ),
        QuestionEntity(
            id = 3,
            topic = "Layouts & Views",
            difficultyLevel = DifficultyLevel.JUNIOR.value,
            question = "Why is ConstraintLayout often preferred over deeply nested LinearLayouts?",
            optionA = "It removes the need for XML",
            optionB = "It reduces view hierarchy depth and can improve layout performance",
            optionC = "It only works with Fragments",
            optionD = "It automatically handles pagination",
            correctAnswer = "B",
            explanation = "ConstraintLayout lets you express complex relationships in a flatter hierarchy, which usually makes measuring and layout cheaper."
        ),
        QuestionEntity(
            id = 4,
            topic = "Intents & Permissions",
            difficultyLevel = DifficultyLevel.JUNIOR.value,
            question = "What is an explicit Intent used for?",
            optionA = "Requesting runtime permissions",
            optionB = "Starting a specific component by class name",
            optionC = "Scheduling background work",
            optionD = "Binding to a content provider",
            correctAnswer = "B",
            explanation = "Explicit intents directly identify the target Activity, Service, or BroadcastReceiver you want Android to launch."
        ),
        QuestionEntity(
            id = 5,
            topic = "MVVM",
            difficultyLevel = DifficultyLevel.MID.value,
            question = "Why is ViewModel a good fit for screen state in MVVM?",
            optionA = "It survives configuration changes and keeps UI logic out of the Activity or Fragment",
            optionB = "It automatically persists data to disk",
            optionC = "It replaces the need for repositories",
            optionD = "It can directly render Compose UI",
            correctAnswer = "A",
            explanation = "ViewModel is lifecycle-aware, survives configuration changes, and is designed to own UI state and screen-level business logic."
        ),
        QuestionEntity(
            id = 6,
            topic = "Coroutines",
            difficultyLevel = DifficultyLevel.MID.value,
            question = "What does the suspend keyword indicate in Kotlin?",
            optionA = "The function can only run on the main thread",
            optionB = "The function may suspend without blocking the underlying thread",
            optionC = "The function always returns Flow",
            optionD = "The function cannot throw exceptions",
            correctAnswer = "B",
            explanation = "A suspend function can pause and resume execution while freeing the thread to do other work, which is essential for efficient async code."
        ),
        QuestionEntity(
            id = 7,
            topic = "Jetpack Components",
            difficultyLevel = DifficultyLevel.MID.value,
            question = "What is a key difference between StateFlow and LiveData?",
            optionA = "StateFlow is always cold while LiveData is hot",
            optionB = "LiveData can be collected from coroutines but StateFlow cannot",
            optionC = "StateFlow is a coroutine-based hot stream with an always-available current value",
            optionD = "LiveData supports backpressure while StateFlow does not",
            correctAnswer = "C",
            explanation = "StateFlow integrates naturally with coroutines and always exposes a current value, which makes it useful for state holders in modern Android apps."
        ),
        QuestionEntity(
            id = 8,
            topic = "Jetpack Components",
            difficultyLevel = DifficultyLevel.MID.value,
            question = "What is one major benefit of using Room over raw SQLite APIs?",
            optionA = "Room automatically uploads data to the cloud",
            optionB = "Room provides compile-time SQL validation and Kotlin-friendly APIs",
            optionC = "Room eliminates the need for migrations",
            optionD = "Room stores data in JSON instead of tables",
            correctAnswer = "B",
            explanation = "Room reduces boilerplate, validates queries during compilation, and integrates with Flow, coroutines, and entities."
        ),
        QuestionEntity(
            id = 9,
            topic = "Performance",
            difficultyLevel = DifficultyLevel.ADVANCED.value,
            question = "What problem do Baseline Profiles primarily help solve?",
            optionA = "SQL schema drift",
            optionB = "App startup and critical user journey performance on Android Runtime",
            optionC = "Missing runtime permissions",
            optionD = "Navigation graph duplication",
            correctAnswer = "B",
            explanation = "Baseline Profiles guide ART ahead-of-time compilation for important code paths, helping startup and interaction performance on user devices."
        ),
        QuestionEntity(
            id = 10,
            topic = "Compose Internals",
            difficultyLevel = DifficultyLevel.ADVANCED.value,
            question = "What is recomposition in Jetpack Compose?",
            optionA = "The process of rebuilding only the parts of UI whose state changed",
            optionB = "Restarting the Activity when state changes",
            optionC = "Serializing composables into XML",
            optionD = "Persisting state to Room automatically",
            correctAnswer = "A",
            explanation = "Compose tracks state reads and reruns only the affected composable functions when that state changes, keeping updates efficient."
        )
    )
}
