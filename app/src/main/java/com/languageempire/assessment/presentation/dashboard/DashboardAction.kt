package com.languageempire.assessment.presentation.dashboard

sealed interface DashboardAction {

    data object Retry : DashboardAction
}