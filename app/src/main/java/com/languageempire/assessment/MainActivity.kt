package com.languageempire.assessment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.languageempire.assessment.core.designsystem.theme.LanguageDemandAssessmentTheme
import com.languageempire.assessment.presentation.dashboard.DashboardRoute
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            LanguageDemandAssessmentTheme {
                DashboardRoute()
            }
        }
    }
}