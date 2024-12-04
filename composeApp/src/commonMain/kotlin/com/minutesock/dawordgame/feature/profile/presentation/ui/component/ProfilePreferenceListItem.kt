package com.minutesock.dawordgame.feature.profile.presentation.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.minutesock.dawordgame.feature.profile.presentation.ProfileEvent
import com.minutesock.dawordgame.feature.profile.presentation.ui.ProfilePreferenceState

@Composable
fun ProfilePreferenceListItem(preferenceState: ProfilePreferenceState, onEvent: (ProfileEvent) -> Unit) {
    Column(
        modifier = Modifier.width(1_000.dp).padding(horizontal = 10.dp),
    ) {
        Text(
            text = preferenceState.preference.displayName.asString(),
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Switch(
                checked = preferenceState.value,
                enabled = preferenceState.enabled,
                onCheckedChange = { onEvent(ProfileEvent.PreferenceToggle(preference = preferenceState.preference)) }
            )
            Spacer(Modifier.size(16.dp))
            Text(
                text = preferenceState.preference.description.asString(),
                fontSize = 16.sp
            )
        }
    }
}