package com.example.dimass.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.dimass.ui.theme.DIMASSTheme
import com.example.dimass.ui.theme.LightGreen

@Composable
fun SettingsScreen(){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LightGreen),
        contentAlignment = Alignment.Center
    ){
        Text(
            text = "Settings",
            fontSize = 32.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview(){
    DIMASSTheme {
        SettingsScreen()
    }
}