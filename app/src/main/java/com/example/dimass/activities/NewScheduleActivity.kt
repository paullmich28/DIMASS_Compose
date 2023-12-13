package com.example.dimass.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.dimass.ui.theme.BottleGreen
import com.example.dimass.ui.theme.DIMASSTheme
import com.example.dimass.ui.theme.Green
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

        val listProgram = listOf("Daily", "Weekly")
        var selectedOption by remember { mutableStateOf(0) }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(LightGreen)
                .verticalScroll(rememberScrollState()),
            contentAlignment = Alignment.TopCenter
        ){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
            ){
                Text(
                    text = "Add Schedule",
                    fontSize = 32.sp,
                    modifier = Modifier.padding(0.dp, 20.dp),
                    fontWeight = FontWeight.Bold
                )
                OutlinedTextField(
                    value = scheduleName,
                    onValueChange = {scheduleName = it},
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text
                    ),
                    label = { Text("Schedule Name") }
                )

                Text(
                    text = "Choose your scheduling type",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 15.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp)
                        .padding(horizontal = 16.dp),
                ){
                    listProgram.forEachIndexed {i, program ->
                        OutlinedButton(
                            onClick = {selectedOption = i},
                            modifier = when(i){
                                0 ->
                                    Modifier
                                        .offset(0.dp, 0.dp)
                                        .fillMaxWidth(0.5f)
                                        .zIndex(if (selectedOption == i) 1f else 0f)
                                else ->
                                    Modifier
                                        .offset((-1 * i).dp, 0.dp)
                                        .fillMaxWidth(1f)
                                        .zIndex(if (selectedOption == i) 1f else 0f)
                            },
                            shape = when(i){
                                0 -> RoundedCornerShape(
                                    topStart = 16.dp,
                                    topEnd = 0.dp,
                                    bottomStart = 16.dp,
                                    bottomEnd = 0.dp
                                )
                                listProgram.size - 1 -> RoundedCornerShape(
                                    topStart = 0.dp,
                                    topEnd = 16.dp,
                                    bottomStart = 0.dp,
                                    bottomEnd = 16.dp
                                )
                                else -> RoundedCornerShape(
                                    topStart = 0.dp,
                                    topEnd = 0.dp,
                                    bottomStart = 0.dp,
                                    bottomEnd = 0.dp
                                )
                            },
                            border = BorderStroke(
                                1.dp, if(selectedOption == i){
                                    Green
                                }else{
                                    Green.copy(alpha = 0.75f)
                                }
                            ),
                            colors = if(selectedOption == i){
                                ButtonDefaults.outlinedButtonColors(
                                    containerColor = Green.copy(alpha = 0.1f),
                                    contentColor = Green
                                )
                            }else{
                                ButtonDefaults.outlinedButtonColors(
                                    containerColor = MaterialTheme.colorScheme.surface,
                                    contentColor = Green
                                )
                            }
                        ) {
                            Text(
                                text = program
                            )
                        }
//                        RadioButton(
//                            selected = selectedOption == program,
//                            onClick = { selectedOption = program }
//                        )
//                        Text(
//                            text = program,
//                            modifier = Modifier.offset(y = 10.dp)
//                        )
                    }
                }

                ElevatedButton(
                    onClick = {

                    },
                    content = { Text(
                        "Submit",
                        fontSize = 16.sp
                    ) },
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = BottleGreen,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .padding(0.dp, 25.dp)
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