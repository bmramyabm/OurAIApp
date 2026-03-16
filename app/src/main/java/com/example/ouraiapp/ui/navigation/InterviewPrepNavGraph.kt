package com.example.ouraiapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ouraiapp.domain.model.DifficultyLevel
import com.example.ouraiapp.ui.home.HomeScreen
import com.example.ouraiapp.ui.onboarding.OnboardingScreen
import com.example.ouraiapp.ui.quiz.QuizLengthScreen
import com.example.ouraiapp.ui.quiz.QuizScreen
import com.example.ouraiapp.ui.quiz.QuizViewModel
import com.example.ouraiapp.ui.review.ReviewScreen
import com.example.ouraiapp.ui.review.ReviewViewModel

@Composable
fun InterviewPrepNavGraph(startDestination: String) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(AppDestinations.ONBOARDING) {
            OnboardingScreen(
                onSkip = {
                    navController.navigate(AppDestinations.HOME) {
                        popUpTo(AppDestinations.ONBOARDING) { inclusive = true }
                    }
                }
            )
        }

        composable(AppDestinations.HOME) {
            HomeScreen(
                onSelectLevel = { level ->
                    navController.navigate(AppDestinations.quizLengthRoute(level))
                }
            )
        }

        composable(
            route = AppDestinations.QUIZ_LENGTH,
            arguments = listOf(navArgument("difficulty") { type = NavType.StringType })
        ) { backStackEntry ->
            val difficulty = DifficultyLevel.fromValue(backStackEntry.arguments?.getString("difficulty"))
            QuizLengthScreen(
                difficultyLevel = difficulty,
                onBack = { navController.popBackStack() },
                onSelectLength = { count ->
                    navController.navigate(AppDestinations.quizRoute(difficulty, count))
                }
            )
        }

        composable(
            route = AppDestinations.QUIZ,
            arguments = listOf(
                navArgument("difficulty") { type = NavType.StringType },
                navArgument("questionCount") { type = NavType.IntType }
            )
        ) {
            val viewModel: QuizViewModel = hiltViewModel()
            QuizScreen(
                viewModel = viewModel,
                onExit = { navController.popBackStack(AppDestinations.HOME, false) },
                onReview = {
                    navController.navigate(AppDestinations.REVIEW)
                }
            )
        }

        composable(AppDestinations.REVIEW) {
            val viewModel: ReviewViewModel = hiltViewModel()
            ReviewScreen(
                viewModel = viewModel,
                onBackHome = {
                    navController.navigate(AppDestinations.HOME) {
                        popUpTo(AppDestinations.HOME) { inclusive = true }
                    }
                }
            )
        }
    }
}
