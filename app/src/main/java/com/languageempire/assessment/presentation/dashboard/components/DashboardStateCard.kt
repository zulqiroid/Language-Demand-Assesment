package com.languageempire.assessment.presentation.dashboard.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.languageempire.assessment.core.designsystem.theme.appDimens
import com.languageempire.assessment.core.designsystem.theme.appSpacing

@Composable
fun DashboardStateCard(
    @StringRes titleRes: Int,
    @StringRes subtitleRes: Int,
    modifier: Modifier = Modifier,
    showProgress: Boolean = false,
    @StringRes actionLabelRes: Int? = null,
    onActionClick: (() -> Unit)? = null
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .widthIn(max = MaterialTheme.appDimens.stateCardMaxWidth),
        shape = MaterialTheme.shapes.extraLarge
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    PaddingValues(
                        horizontal = MaterialTheme.appSpacing.extraLarge,
                        vertical = MaterialTheme.appSpacing.extraLarge
                    )
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.medium)
        ) {
            if (showProgress) {
                CircularProgressIndicator()
            }

            Text(
                text = stringResource(id = titleRes),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = stringResource(id = subtitleRes),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (actionLabelRes != null && onActionClick != null) {
                Button(
                    modifier = Modifier.heightIn(
                        min = MaterialTheme.appDimens.buttonMinHeight
                    ),
                    onClick = onActionClick
                ) {
                    Text(
                        text = stringResource(id = actionLabelRes)
                    )
                }
            }
        }
    }
}