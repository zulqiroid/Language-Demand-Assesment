package com.languageempire.assessment.presentation.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.languageempire.assessment.R
import com.languageempire.assessment.core.designsystem.component.SectionHeader
import com.languageempire.assessment.core.designsystem.theme.appDimens
import com.languageempire.assessment.core.designsystem.theme.appSpacing
import com.languageempire.assessment.presentation.call.CallFailoverAction
import com.languageempire.assessment.presentation.call.CallFailoverUiState
import com.languageempire.assessment.presentation.call.CallFailoverViewModel
import com.languageempire.assessment.presentation.call.CallProviderScenarioUiState
import com.languageempire.assessment.presentation.call.components.CallFailoverCard
import com.languageempire.assessment.presentation.dashboard.components.BookingTypeStatsCard
import com.languageempire.assessment.presentation.dashboard.components.DashboardHeroCard
import com.languageempire.assessment.presentation.dashboard.components.DashboardSummaryCard
import com.languageempire.assessment.presentation.dashboard.components.LanguageDemandCard

@Composable
fun DashboardRoute(
    modifier: Modifier = Modifier,
    dashboardViewModel: DashboardViewModel = hiltViewModel(),
    callFailoverViewModel: CallFailoverViewModel = hiltViewModel()
) {
    val dashboardUiState by dashboardViewModel.uiState.collectAsStateWithLifecycle()
    val callFailoverUiState by callFailoverViewModel.uiState.collectAsStateWithLifecycle()
    val callProviderScenarioUiState by callFailoverViewModel.scenarioUiState.collectAsStateWithLifecycle()

    DashboardScreen(
        modifier = modifier,
        dashboardUiState = dashboardUiState,
        callFailoverUiState = callFailoverUiState,
        callProviderScenarioUiState = callProviderScenarioUiState,
        onDashboardAction = dashboardViewModel::onAction,
        onCallFailoverAction = callFailoverViewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DashboardScreen(
    dashboardUiState: DashboardUiState,
    callFailoverUiState: CallFailoverUiState,
    callProviderScenarioUiState: CallProviderScenarioUiState,
    onDashboardAction: (DashboardAction) -> Unit,
    onCallFailoverAction: (CallFailoverAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        contentWindowInsets = WindowInsets.safeDrawing,
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            DashboardTopBar(
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = MaterialTheme.colorScheme.background
        ) {
            when (dashboardUiState) {
                DashboardUiState.Loading -> {
                    DashboardLoadingContent()
                }

                is DashboardUiState.Error -> {
                    DashboardErrorContent(
                        messageRes = dashboardUiState.messageRes,
                        onRetryClick = {
                            onDashboardAction(DashboardAction.Retry)
                        }
                    )
                }

                is DashboardUiState.Content -> {
                    DashboardContent(
                        dashboardContent = dashboardUiState,
                        callFailoverUiState = callFailoverUiState,
                        callProviderScenarioUiState = callProviderScenarioUiState,
                        onCallFailoverAction = onCallFailoverAction
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DashboardTopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier
) {
    LargeTopAppBar(
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        title = {
            Column(
                verticalArrangement = Arrangement.spacedBy(
                    MaterialTheme.appSpacing.extraSmall
                )
            ) {
                Text(
                    text = stringResource(id = R.string.dashboard_top_bar_title),
                    style = MaterialTheme.typography.headlineMedium
                )

                Text(
                    text = stringResource(id = R.string.dashboard_top_bar_subtitle),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    )
}

@Composable
private fun DashboardContent(
    dashboardContent: DashboardUiState.Content,
    callFailoverUiState: CallFailoverUiState,
    callProviderScenarioUiState: CallProviderScenarioUiState,
    onCallFailoverAction: (CallFailoverAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = MaterialTheme.appDimens.screenMaxWidth),
            contentPadding = PaddingValues(
                start = MaterialTheme.appSpacing.large,
                top = MaterialTheme.appSpacing.large,
                end = MaterialTheme.appSpacing.large,
                bottom = MaterialTheme.appSpacing.doubleExtraLarge
            ),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.large)
        ) {
            item(
                key = DashboardContentKey.Hero
            ) {
                DashboardHeroCard()
            }

            item(
                key = DashboardContentKey.SummaryHeader
            ) {
                SectionHeader(
                    titleRes = R.string.dashboard_summary_title
                )
            }

            item(
                key = DashboardContentKey.SummaryCard
            ) {
                DashboardSummaryCard(
                    summary = dashboardContent.summary
                )
            }

            item(
                key = DashboardContentKey.LanguageDemandHeader
            ) {
                SectionHeader(
                    titleRes = R.string.dashboard_language_demand_title
                )
            }

            if (dashboardContent.languageDemands.isEmpty()) {
                item(
                    key = DashboardContentKey.EmptyLanguages
                ) {
                    Text(
                        text = stringResource(id = R.string.dashboard_empty_title),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                items(
                    items = dashboardContent.languageDemands,
                    key = { languageDemand ->
                        languageDemand.id
                    }
                ) { languageDemand ->
                    LanguageDemandCard(
                        model = languageDemand
                    )
                }
            }

            item(
                key = DashboardContentKey.BookingTypeHeader
            ) {
                SectionHeader(
                    titleRes = R.string.dashboard_booking_type_pressure_title
                )
            }

            items(
                items = dashboardContent.bookingTypeStats,
                key = { bookingTypeStats ->
                    bookingTypeStats.bookingType.name
                }
            ) { bookingTypeStats ->
                BookingTypeStatsCard(
                    model = bookingTypeStats
                )
            }

            item(
                key = DashboardContentKey.CallFailoverCard
            ) {
                CallFailoverCard(
                    uiState = callFailoverUiState,
                    scenarioUiState = callProviderScenarioUiState,
                    onAction = onCallFailoverAction
                )
            }
        }
    }
}

@Composable
private fun DashboardLoadingContent(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(MaterialTheme.appSpacing.large),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.medium)
        ) {
            CircularProgressIndicator()

            Text(
                text = stringResource(id = R.string.dashboard_loading),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
private fun DashboardErrorContent(
    messageRes: Int,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(MaterialTheme.appSpacing.large),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.widthIn(max = MaterialTheme.appDimens.screenMaxWidth),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.large)
        ) {
            Text(
                text = stringResource(id = messageRes),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            Button(
                modifier = Modifier.heightIn(
                    min = MaterialTheme.appDimens.buttonMinHeight
                ),
                onClick = onRetryClick
            ) {
                Text(
                    text = stringResource(id = R.string.dashboard_retry)
                )
            }
        }
    }
}

private enum class DashboardContentKey {
    Hero,
    SummaryHeader,
    SummaryCard,
    LanguageDemandHeader,
    EmptyLanguages,
    BookingTypeHeader,
    CallFailoverCard
}