package com.example.dimass.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dimass.ui.theme.Blue
import com.example.dimass.ui.theme.DIMASSTheme
import com.example.dimass.ui.theme.Green
import com.example.dimass.ui.theme.LightGreen
import com.example.dimass.ui.theme.Mustard
import com.example.dimass.ui.theme.Red

class BmiActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DIMASSTheme {
                // A surface container using the 'background' color from the theme
                BmiText()
            }
        }
    }

    @Composable
    fun BmiText(){
        var weight = 0f
        var height = 0f
        var color = Green
        var category = ""
        var program = ""

        if(intent.extras != null){
            weight = intent.extras!!.getFloat("weight", 0f)
            height = intent.extras!!.getFloat("height", 0f)
        }

        val heightInM = height/100
        val bmi = weight / (heightInM * heightInM)

        if(bmi < 18.5){
            category = "Underweight"
            color = Blue
            program = "Mass Gain"
        }else if(bmi >= 18.5 && bmi < 25){
            category = "Normal"
            color = Green
            program = "Bebas"
        }else if(bmi >= 25 && bmi < 30){
            category = "Overweight"
            color = Mustard
            program = "Diet"
        }else{
            category = "Obese"
            color = Red
            program = "Diet"
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(LightGreen),
            contentAlignment = Alignment.Center
        ){
            BmiCard(category, color, bmi.toString(), program)
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun BmiCard(category: String, color: Color, bmi: String, program: String){
        var isExpanded by remember {
            mutableStateOf(false)
        }

        var type by remember {
            mutableStateOf(program)
        }

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Card(
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = color
                )
            ){
                Column(
                    modifier = Modifier
                        .defaultMinSize()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Your BMI is $bmi",
                        fontSize = 15.sp
                    )
                    Text(
                        text = "You're $category",
                        modifier = Modifier.padding(0.dp, 15.dp, 0.dp, 0.dp),
                        fontSize = 25.sp
                    )
                }
            }

            Text(
                text = "We recommend you to do a $program",
                modifier = Modifier
                    .padding(0.dp, 10.dp)
            )

            ExposedDropdownMenuBox(
                expanded = isExpanded,
                onExpandedChange = {isExpanded = it}
            ) {
                TextField(
                    value = type,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
                    },
                    colors = ExposedDropdownMenuDefaults.textFieldColors(),
                    modifier = Modifier.menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = isExpanded,
                    onDismissRequest = {isExpanded = false}
                ){
                    DropdownMenuItem(
                        text = {
                               Text(program)
                        },
                        onClick = {
                            type = "Mass Gain"
                            isExpanded = false
                        }
                    )
                }
            }

            ElevatedButton(
                onClick = {}
            ) {
                Text(
                    text = "Next"
                )
            }
        }
    }

    @Preview
    @Composable
    fun BmiReview(){
        DIMASSTheme {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(LightGreen),
                contentAlignment = Alignment.Center
            ){
                BmiCard(category = "Obese", color = Red, bmi = "30", "Diet")
            }
        }
    }
}

