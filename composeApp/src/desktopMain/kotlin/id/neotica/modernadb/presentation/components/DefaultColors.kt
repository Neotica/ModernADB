package id.neotica.modernadb.presentation.components

import androidx.compose.material.SwitchDefaults
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import id.neotica.modernadb.presentation.theme.DarkPrimary
import id.neotica.modernadb.presentation.theme.DarkPrimaryTransparent2
import id.neotica.modernadb.presentation.theme.DarkPrimaryTransparent40

@Composable
fun NeoTextFieldColor() = TextFieldDefaults.colors().copy(
    unfocusedTextColor = DarkPrimary,
    focusedContainerColor = DarkPrimary,
    unfocusedContainerColor = DarkPrimaryTransparent40,
    cursorColor = Color.White,
    focusedTextColor = Color.White
)

@Composable
fun NeoSwitchColor() = SwitchDefaults.colors(
    checkedThumbColor = DarkPrimary,
    checkedTrackColor = DarkPrimaryTransparent2,
    uncheckedThumbColor = DarkPrimary,
    uncheckedTrackColor = DarkPrimaryTransparent2
)