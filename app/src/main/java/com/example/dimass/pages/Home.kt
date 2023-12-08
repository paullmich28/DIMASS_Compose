package com.example.dimass.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dimass.ui.theme.DIMASSTheme
import com.example.dimass.ui.theme.LightGreen

@Composable
fun HomeScreen(){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LightGreen),
        contentAlignment = Alignment.TopCenter
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Program: Diet",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
            Card(
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Green
                ),
                modifier = Modifier.padding(20.dp)
            ){
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Penjadwalan 1",
                        fontSize = 20.sp
                    )

                    Text(
                        text = "Start Date: 06/12/2023",
                        modifier = Modifier.padding(0.dp, 20.dp, 0.dp, 0.dp)
                    )

                    Text(
                        text = "End Date: 12/12/2023"
                    )
                }
            }

            Example (
                modifier = Modifier.align(Alignment.Start)
            ){ }
        }

    }
}

@Composable
fun Example(modifier: Modifier, onClick: () -> Unit) {
    FloatingActionButton(
        onClick = { onClick() },
    ) {
        Icon(Icons.Filled.Add, "Floating action button.")
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview(){
    DIMASSTheme {
        HomeScreen()
    }
}