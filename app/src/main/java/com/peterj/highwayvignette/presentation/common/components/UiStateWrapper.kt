package com.peterj.highwayvignette.presentation.common.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.peterj.highwayvignette.R
import com.peterj.highwayvignette.domain.model.HighwayVignetteError
import com.peterj.highwayvignette.presentation.common.state.UiState

@Composable
fun <T> UiStateWrapper(
    state: UiState<T>,
    empty: @Composable () -> Unit = { DefaultEmpty() },
    loading: @Composable () -> Unit = { DefaultLoading() },
    error: @Composable (String) -> Unit = { DefaultError(it) },
    success: @Composable (T) -> Unit
) {
    when (state) {
        UiState.Empty -> empty()
        UiState.Loading -> loading()
        is UiState.Error -> {
            val message = when (val err = state.error) {
                is HighwayVignetteError.NetworkError -> stringResource(R.string.error_network)
                is HighwayVignetteError.Unknown -> err.message ?: stringResource(R.string.error_unknown)
            }
            error(message)
        }
        is UiState.Success -> success(state.data)
    }
}

@Composable
fun DefaultEmpty() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.no_data_available),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun DefaultLoading() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            strokeWidth = 2.dp,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun DefaultError(message: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.error
        )
    }
}
