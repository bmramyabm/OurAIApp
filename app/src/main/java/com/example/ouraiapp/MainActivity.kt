package com.example.ouraiapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import com.example.ouraiapp.ui.theme.OurAiAppTheme
import com.example.ouraiapp.ui.navigation.InterviewPrepApp

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OurAiAppTheme {
                InterviewPrepApp()
            }
        }
    }
}
