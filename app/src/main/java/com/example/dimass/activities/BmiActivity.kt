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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

        if(intent.extras != null){
            weight = intent.extras!!.getFloat("weight", 0f)
            height = intent.extras!!.getFloat("height", 0f)
        }

        val heightInM = height/100
        val bmi = weight / (heightInM * heightInM)

        if(bmi < 18.5){
            category = "Underweight"
            color = Blue
        }else if(bmi >= 18.5 && bmi < 25){
            category = "Normal"
            color = Green
        }else if(bmi >= 25 && bmi < 30){
            category = "Overweight"
            color = Mustard
        }else{
            category = "Obese"
            color = Red
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(LightGreen),
            contentAlignment = Alignment.Center
        ){
            BmiCard(category, color, bmi.toString())
        }
    }

    @Composable
    fun BmiCard(category: String, color: Color, bmi: String){
        Card(
            modifier = Modifier
                .size(250.dp, 100.dp),
            shape = RoundedCornerShape(5.dp),
            colors = CardDefaults.cardColors(
                containerColor = color
            )
        ){
            Column(
                modifier = Modifier
                    .defaultMinSize()
                    .padding(10.dp),
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
    }

    @Preview
    @Composable
    fun BmiReview(){
        DIMASSTheme {
            BmiText()
        }
    }
}

