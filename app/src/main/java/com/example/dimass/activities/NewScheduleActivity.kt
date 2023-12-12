package com.example.dimass.activities

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.example.dimass.ui.theme.DIMASSTheme
import com.example.dimass.ui.theme.LightGreen

class NewScheduleActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DIMASSTheme {
                NewSchedulePage()
            }
        }
    }
    
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun NewSchedulePage(){
        var scheduleName by remember {
            mutableStateOf("")
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(LightGreen)
                .verticalScroll(rememberScrollState()),
            contentAlignment = Alignment.Center
        ){
            Column {
                OutlinedTextField(
                    value = scheduleName,
                    onValueChange = {scheduleName = it},
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text
                    ),
                    label = { Text("Schedule Name") },
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                )
            }

        }
    }

    @Composable
    @Preview
    fun NewSchedulePreview(){
        DIMASSTheme {
            NewSchedulePage()
        }
    }
}