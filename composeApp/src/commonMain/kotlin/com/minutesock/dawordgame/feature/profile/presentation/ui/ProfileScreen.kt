package com.minutesock.dawordgame.feature.profile.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.minutesock.dawordgame.core.domain.Version
import com.minutesock.dawordgame.core.theme.AppTheme
import com.minutesock.dawordgame.feature.profile.presentation.ProfileEvent
import com.minutesock.dawordgame.feature.profile.presentation.ProfileState
import com.minutesock.dawordgame.feature.profile.presentation.ProfileViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ProfileScreenHost(
    modifier: Modifier,
    viewModel: ProfileViewModel = viewModel {
        ProfileViewModel()
    },
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    ProfileScreen(
        modifier = modifier,
        state = state,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun ProfileScreen(
    state: ProfileState,
    onEvent: (ProfileEvent) -> Unit,
    modifier: Modifier = Modifier
) {

    Scaffold(
        bottomBar = {
            Text(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                text = Version.name,
                textAlign = TextAlign.Right,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.outline
            )
        }
    ) {
        val scrollState = rememberScrollState()
        Column(
            modifier = modifier
                .padding(top = 16.dp)
                .verticalScroll(scrollState)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Settings", // todo extract
                textAlign = TextAlign.Center,
                fontSize = 26.sp
            )
            HorizontalDivider(modifier = Modifier.padding(10.dp), thickness = 2.dp)

            Spacer(Modifier.size(10.dp))

            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Switch(
                    checked = state.darkModeToggle,
                    enabled = state.darkModeToggleEnabled,
                    onCheckedChange = { onEvent(ProfileEvent.DarkModeToggle) }
                )
                Spacer(modifier = Modifier.size(10.dp))
                Text(
                    text = "Dark Mode" // todo extract
                )
            }

            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Switch(
                    checked = state.useSystemThemeToggle,
                    onCheckedChange = { onEvent(ProfileEvent.UseSystemThemeToggle) }
                )
                Spacer(modifier = Modifier.size(10.dp))
                Text(
                    text = "Use System Theme", // todo extract
                    textAlign = TextAlign.Center,
                )
            }

            Spacer(Modifier.size(10.dp))

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Miscellaneous", // todo extract
                textAlign = TextAlign.Center,
                fontSize = 26.sp
            )
            HorizontalDivider(modifier = Modifier.padding(10.dp), thickness = 2.dp)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = { onEvent(ProfileEvent.ClickWebsite) }
                ) {
                    Text(
                        text = "More games" // todo extract
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun ProfileScreenPreview() {
    AppTheme {
        ProfileScreen(
            state = ProfileState(
                darkModeToggle = true,
                useSystemThemeToggle = true
            ),
            onEvent = {}
        )
    }
}