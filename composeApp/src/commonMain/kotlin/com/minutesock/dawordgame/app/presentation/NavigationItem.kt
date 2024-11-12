package com.minutesock.dawordgame.app.presentation

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun NavigationItem(
    selected: Boolean,
    icon: DrawableResource,
    title: String,
    alwaysShowLabel: Boolean = false,
) {
    Column(
        modifier = Modifier.wrapContentWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(icon),
            tint = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
            contentDescription = title
        )
        Text(
            modifier = Modifier.animateContentSize(),
            text = title,
            color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
            fontWeight = FontWeight.Medium,
            fontSize = if (selected || alwaysShowLabel) 16.sp else 0.sp
        )
    }
}