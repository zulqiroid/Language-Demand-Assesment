package com.languageempire.assessment.core.designsystem.component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.languageempire.assessment.core.designsystem.theme.appSpacing

@Composable
fun SectionHeader(
    @StringRes titleRes: Int,
    modifier: Modifier = Modifier,
    @StringRes subtitleRes: Int? = null
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = androidx.compose.ui.res.stringResource(id = titleRes),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        if (subtitleRes != null) {
            Text(
                modifier = Modifier.padding(
                    top = MaterialTheme.appSpacing.extraSmall
                ),
                text = androidx.compose.ui.res.stringResource(id = subtitleRes),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}